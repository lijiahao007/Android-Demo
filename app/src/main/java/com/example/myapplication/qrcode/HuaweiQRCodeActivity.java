package com.example.myapplication.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;

import java.io.FileNotFoundException;
import java.io.OutputStream;

public class HuaweiQRCodeActivity extends AppCompatActivity {

    private EditText etMessage;
    private ImageView ivCode;
    private Bitmap qrBitmap = null;
    private ContentResolver contentResolver;
    private Bitmap logoBitmap;
    private Bitmap backGroundBitmap;
    private Canvas canvas;
    private Paint mPaint;
    private Bitmap wholeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huawei_qrcode);

        etMessage = findViewById(R.id.et_message);
        ivCode = findViewById(R.id.iv_code);
        contentResolver = getContentResolver();
        logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_192);
        backGroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.set_img_gzhbg2);
        wholeBitmap = Bitmap.createBitmap(backGroundBitmap.getWidth(), backGroundBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(wholeBitmap);
        mPaint = new Paint();
        mPaint.setTextSize(backGroundBitmap.getHeight() * 0.07f);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.WHITE);
        canvas.drawBitmap(backGroundBitmap, 0, 0, mPaint);
        canvas.drawText("微信扫一扫", backGroundBitmap.getWidth() / 2.0f, backGroundBitmap.getHeight() * 0.15f, mPaint);
        canvas.drawText("关注“V380小亮助手”公众号", backGroundBitmap.getWidth() / 2.0f, backGroundBitmap.getHeight() * 0.25f, mPaint);


        findViewById(R.id.btn_create_qrcode).setOnClickListener(view -> {
            String msg = etMessage.getText().toString();
            int width = (int) (backGroundBitmap.getWidth() * 0.6);
            int height = (int) (backGroundBitmap.getHeight() * 0.6);
            float paddingLeft = (float) (backGroundBitmap.getWidth() * 0.4 / 2);
            HmsBuildBitmapOption options = new HmsBuildBitmapOption.Creator()
                    .setBitmapBackgroundColor(Color.WHITE)
                    .setBitmapColor(Color.BLACK)
                    .setQRLogoBitmap(logoBitmap)
                    .setBitmapMargin(10).create();
            try {
                // 如果未设置HmsBuildBitmapOption对象，生成二维码参数options置null。
                qrBitmap = ScanUtil.buildBitmap(msg, HmsScan.QRCODE_SCAN_TYPE, width, height, options);
                canvas.drawBitmap(qrBitmap, paddingLeft, backGroundBitmap.getHeight() * 0.30f, mPaint);
                ivCode.setImageBitmap(wholeBitmap);
            } catch (WriterException e) {
                Log.w("buildBitmap", e);
            }
        });

        findViewById(R.id.btn_save_qrcode).setOnClickListener(view -> {
            if (qrBitmap != null) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "V380 Pro 二维码");
                Uri insertUri = contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values
                );
                try {
                    OutputStream outputStream = contentResolver.openOutputStream(insertUri);
                    boolean compress = wholeBitmap.compress(Bitmap.CompressFormat.PNG, 1, outputStream);
                    if (compress) {
                        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}