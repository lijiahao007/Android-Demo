package com.example.myapplication.album;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.myapplication.R;
import com.example.myapplication.album.bean.MediaBean;
import com.example.myapplication.album.bean.MediaBeanDBHelper;
import com.example.myapplication.album.bean.MediaType;
import com.example.myapplication.album.utils.Action;
import com.example.myapplication.album.utils.BitmapTools;
import com.example.myapplication.album.utils.PicHandlerThread;
import com.example.myapplication.album.utils.UriUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class PhotoVideoDemoActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "PhotoVideoDemoActivity";

    private String[] permissions;

    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
        } else {
            permissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
        }
    }


    private final int PERMISSION_REQUEST_CODE = 1;
    private Button btnVideo;
    private Button btnPhoto;
    private ImageView ivShow1;
    private ImageView ivShow;
    private VideoView vvShow;
    private Button btnScreenShot;


    // 拍照获取图片
    private String curImageFilePath = null;
    private Uri curImageUri = null;
    ActivityResultLauncher<Void> photoBtnLaunch = registerForActivityResult(new ActivityResultContract<Void, Bitmap>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
                try {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
                    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File tmpFile = File.createTempFile(timeStamp, ".jpg", storageDir);
                    Uri uriForImage = FileProvider.getUriForFile(PhotoVideoDemoActivity.this, "com.android.application.fileprovider", tmpFile);
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriForImage);
                    curImageFilePath = tmpFile.getAbsolutePath();
                    curImageUri = uriForImage;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return takePhotoIntent;
        }

        @Override
        @Nullable
        public Bitmap parseResult(int resultCode, @Nullable Intent intent) {
            if (intent != null) {
                return (Bitmap) intent.getExtras().get("data");
            }
            return null;
        }
    }, new ActivityResultCallback<Bitmap>() {
        @Override
        public void onActivityResult(Bitmap result) {
            if (result != null) {
                // 图片没有存储
                ivShow.setImageBitmap(result);
            } else {
                Bitmap scaleBitmap = BitmapTools.getScaleBitmap(curImageFilePath, 550, 550);

                if (scaleBitmap == null) {
                    // 如果没有拍照，bitmap就会变成null。需要将创建的文件删除
                    getContentResolver().delete(curImageUri, null, null);
                    return;
                }

                ivShow1.setImageBitmap(scaleBitmap);
                MediaBean bean = new MediaBean(curImageUri, curImageFilePath, MediaType.IMAGE);
                insertMediaBean(bean);
                Log.i(TAG, "scaleBitmap:" + scaleBitmap.getByteCount() + "byte");
            }
        }
    });

    private MediaPlayer mediaPlayer = null;
    // 录像
    ActivityResultLauncher<Void> videoBtnLaunch = registerForActivityResult(new ActivityResultContract<Void, Uri>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        }

        @Override
        public Uri parseResult(int resultCode, @Nullable Intent intent) {
            if (intent != null) {
                return intent.getData();
            }
            return null;
        }
    }, new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if (result == null) return;
            vvShow.setVideoURI(result);
            vvShow.start();
            String fileAbsolutePath = UriUtils.getFileAbsolutePath(PhotoVideoDemoActivity.this, result);
            MediaBean bean = new MediaBean(result, fileAbsolutePath, MediaType.VIDEO);

            if (mediaPlayer == null)
                mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(PhotoVideoDemoActivity.this, result);
                mediaPlayer.prepare();
                int duration = mediaPlayer.getDuration();
                bean.setDuration(duration);
                insertMediaBean(bean);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    // 截屏  TODO:Android10以后截屏需要使用一个前台服务。这部分需要进行适配。
    private ImageReader imageReader = null;
    private VirtualDisplay virtualDisplay = null;
    private MediaProjection mediaProjection = null;
    ActivityResultLauncher<Void> screenShotLaunch = registerForActivityResult(new ActivityResultContract<Void, Bitmap>() {

        private MediaProjectionManager projectionManager;

        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
            return projectionManager.createScreenCaptureIntent();
        }

        @SuppressLint("WrongConstant")
        @RequiresApi(api = Build.VERSION_CODES.R)
        @Override
        public Bitmap parseResult(int resultCode, @Nullable Intent intent) {
            // 1. 获取宽高
            WindowManager windowManager = getWindowManager();
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(outMetrics);
            int screenWidth = outMetrics.widthPixels;
            int screenHeight = outMetrics.heightPixels;
            int dpi = outMetrics.densityDpi;

            mediaProjection = projectionManager.getMediaProjection(resultCode, intent);
            imageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 1);
            // 构建VirtualDisplay，将屏幕每一帧输出到imageReader的surface中。
            virtualDisplay = mediaProjection.createVirtualDisplay("ScreenShot", screenWidth, screenHeight, dpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader.getSurface(), null, null);
            new Handler(Looper.getMainLooper()).postDelayed(picHandleThread::action, 300);
            return null;
        }
    }, new ActivityResultCallback<Bitmap>() {
        @Override
        public void onActivityResult(Bitmap result) {
        }
    });
    private final PicHandlerThread picHandleThread = new PicHandlerThread("图片处理线程") {
        private Handler handler = null;

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            handler = new Handler(getLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    Image image = imageReader.acquireLatestImage();
                    Image.Plane[] planes = image.getPlanes();
                    ByteBuffer buffer = planes[0].getBuffer();
                    int pixelStride = planes[0].getPixelStride(); // 两个像素头部的距离,(一个像素及其间隙所占用字节大小)
                    int rowStride = planes[0].getRowStride(); // 两行像素头部的距离 （一行机器间隙所占用字节大小）
                    int width = image.getWidth(); // image的长宽是像素格式
                    int height = image.getHeight();
                    int rowPadding = rowStride - pixelStride * image.getWidth(); // 因为内存对齐的原因，所以每行会有一些空余。这个值也是字节格式的。
                    Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888); // bitmap接收的是像素格式
                    bitmap.copyPixelsFromBuffer(buffer);
                    image.close();
                    virtualDisplay.release();
                    virtualDisplay = null;
                    imageReader.close();
                    mediaProjection.stop();
                    Log.i(TAG, "scaleBitmap:" + bitmap.getByteCount() + "byte");

                    // 保存截图
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    timeStamp = "ScreenShot_" + timeStamp;
                    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    try {
                        File tempFile = File.createTempFile(timeStamp, ".jpg", storageDir);
                        Uri uriForFile = FileProvider.getUriForFile(PhotoVideoDemoActivity.this, "com.android.application.fileprovider", tempFile);
                        OutputStream outputStream = getContentResolver().openOutputStream(uriForFile);
                        boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                        MediaBean bean = new MediaBean(uriForFile, tempFile.getAbsolutePath(), MediaType.IMAGE);

                        insertMediaBean(bean);

                        runOnUiThread(() -> {
                            if (compress) {
                                Toast.makeText(PhotoVideoDemoActivity.this, "截图保存完成", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PhotoVideoDemoActivity.this, "截图保存失败", Toast.LENGTH_SHORT).show();
                            }
                            ivShow.setImageBitmap(bitmap);
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        }

        @Override
        public void action() {
            handler.obtainMessage().sendToTarget();
        }
    };
    private SQLiteDatabase database;
    private MediaBeanDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_video_demo);
        vvShow = findViewById(R.id.vv_show);
        ivShow = findViewById(R.id.iv_show);
        ivShow1 = findViewById(R.id.iv_show1);
        btnPhoto = findViewById(R.id.btn_photo);
        btnVideo = findViewById(R.id.btn_video);
        btnScreenShot = findViewById(R.id.btn_screen_shot);
        MediaController mediaController = new MediaController(this);
        vvShow.setMediaController(mediaController);
        vvShow.setOnPreparedListener(mp -> {
            mediaController.setAnchorView(vvShow);
        });

        btnPhoto.setOnClickListener(view -> {
            actionAfterCheckPermission(() -> {
                photoBtnLaunch.launch(null);
            });
        });

        btnVideo.setOnClickListener(view -> {
            actionAfterCheckPermission(() -> {
                videoBtnLaunch.launch(null);
            });
        });

        picHandleThread.start(); // 开启截图保存线程
        btnScreenShot.setOnClickListener(view -> {
            actionAfterCheckPermission(() -> {
                screenShotLaunch.launch(null);
            });
        });

        // 初始化数据库
        helper = new MediaBeanDBHelper(this);
        database = helper.getWritableDatabase();

    }

    private long insertMediaBean(MediaBean bean) {
        ContentValues values = new ContentValues();
        values.put(MediaBean.Entry.URI, bean.getUri().toString());
        values.put(MediaBean.Entry.TIMESTAMP, bean.getTimestamp());
        values.put(MediaBean.Entry.FILENAME, bean.getFileName());
        values.put(MediaBean.Entry.TYPE, bean.getType().toString());
        values.put(MediaBean.Entry.DURATION, bean.getDuration());
        values.put(MediaBean.Entry.DATE, bean.getDate());
        long res = database.insert(MediaBean.Entry.TABLE_NAME, null, values);
        Log.i(TAG, "插入结果:" + res);
        return res;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
        helper.close();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    private void actionAfterCheckPermission(Action action) {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            action.run();
        } else {
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, PERMISSION_REQUEST_CODE, permissions)
                            .setNegativeButtonText("取消")
                            .setPositiveButtonText("设置")
                            .setRationale("需要【相机】、【读写拓展存储空间】权限")
                            .build()
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // 永久拒绝的话就弹窗提醒
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("权限申请")
                    .setRationale("需要【相机】、【读写拓展存储空间】权限才可以完成本页面功能")
                    .setNegativeButton("拒绝")
                    .setPositiveButton("设置")
                    .build()
                    .show();
        }
    }
}