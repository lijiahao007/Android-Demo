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

    private final String[] permissions;

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


    // ??????????????????
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
                    File filesDir = getFilesDir();
                    File cacheDir = getCacheDir();
                    File externalFilesDir = getExternalFilesDir(null);
                    File externalCacheDir = getExternalCacheDir();

                    File externalStorageDirectory = Environment.getExternalStorageDirectory();

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
                // ??????????????????
                ivShow.setImageBitmap(result);
            } else {
                Bitmap scaleBitmap = BitmapTools.getScaleBitmap(curImageFilePath, 550, 550);

                if (scaleBitmap == null) {
                    // ?????????????????????bitmap????????????null?????????????????????????????????
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
    // ??????
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

    // ??????  TODO:Android10???????????????????????????????????????????????????????????????????????????
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
            // 1. ????????????
            WindowManager windowManager = getWindowManager();
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(outMetrics);
            int screenWidth = outMetrics.widthPixels;
            int screenHeight = outMetrics.heightPixels;
            int dpi = outMetrics.densityDpi;

            mediaProjection = projectionManager.getMediaProjection(resultCode, intent);
            imageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 1);
            // ??????VirtualDisplay??????????????????????????????imageReader???surface??????
            virtualDisplay = mediaProjection.createVirtualDisplay("ScreenShot", screenWidth, screenHeight, dpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader.getSurface(), null, null);
            new Handler(Looper.getMainLooper()).postDelayed(picHandleThread::action, 300);
            return null;
        }
    }, new ActivityResultCallback<Bitmap>() {
        @Override
        public void onActivityResult(Bitmap result) {
        }
    });
    private final PicHandlerThread picHandleThread = new PicHandlerThread("??????????????????") {
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
                    int pixelStride = planes[0].getPixelStride(); // ???????????????????????????,(?????????????????????????????????????????????)
                    int rowStride = planes[0].getRowStride(); // ??????????????????????????? ?????????????????????????????????????????????
                    int width = image.getWidth(); // image????????????????????????
                    int height = image.getHeight();
                    int rowPadding = rowStride - pixelStride * image.getWidth(); // ????????????????????????????????????????????????????????????????????????????????????????????????
                    Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888); // bitmap????????????????????????
                    bitmap.copyPixelsFromBuffer(buffer);
                    image.close();
                    virtualDisplay.release();
                    virtualDisplay = null;
                    imageReader.close();
                    mediaProjection.stop();
                    Log.i(TAG, "scaleBitmap:" + bitmap.getByteCount() + "byte");

                    // ????????????
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
                                Toast.makeText(PhotoVideoDemoActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PhotoVideoDemoActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
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

        picHandleThread.start(); // ????????????????????????
        btnScreenShot.setOnClickListener(view -> {
            actionAfterCheckPermission(() -> {
                screenShotLaunch.launch(null);
            });
        });

        // ??????????????????
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
        Log.i(TAG, "????????????:" + res);
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
                            .setNegativeButtonText("??????")
                            .setPositiveButtonText("??????")
                            .setRationale("?????????????????????????????????????????????????????????")
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
        // ?????????????????????????????????
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("????????????")
                    .setRationale("???????????????????????????????????????????????????????????????????????????????????????")
                    .setNegativeButton("??????")
                    .setPositiveButton("??????")
                    .build()
                    .show();
        }
    }
}