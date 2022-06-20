package com.example.myapplication.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;

import java.io.FileNotFoundException;
import java.io.OutputStream;

public class HuaweiQRCode2Activity extends AppCompatActivity {

    private CustomQRCodeView qrcode;
    private ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huawei_qrcode2);
        EditText etMessage = findViewById(R.id.et_message);
        qrcode = findViewById(R.id.custom_view_qrcode);
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_192);

        contentResolver = getContentResolver();
        findViewById(R.id.btn_create_qrcode).setOnClickListener(view -> {
            String msg = etMessage.getText().toString();
            int width = getResources().getDimensionPixelOffset(R.dimen.one_dp) * 200;
            int height = width;
            HmsBuildBitmapOption options = new HmsBuildBitmapOption.Creator()
                    .setBitmapBackgroundColor(Color.WHITE)
                    .setBitmapColor(Color.BLACK)
                    .setQRLogoBitmap(logoBitmap)
                    .setBitmapMargin(10).create();

            try {
                // 如果未设置HmsBuildBitmapOption对象，生成二维码参数options置null。
                Bitmap qrBitmap = ScanUtil.buildBitmap(msg, HmsScan.QRCODE_SCAN_TYPE, width, height, options);
                qrcode.setBitmap(qrBitmap);
            } catch (WriterException e) {
                Log.w("buildBitmap", e);
            }
        });


        findViewById(R.id.btn_save_qrcode).setOnClickListener(view -> {
            Bitmap qrBitmap = qrcode.loadBitmapFromViewBySystem();
            if (qrBitmap != null) {
                ContentValues values = new ContentValues();
                Uri insertUri = contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values
                );
                try {
                    OutputStream outputStream = contentResolver.openOutputStream(insertUri);
                    boolean compress = qrBitmap.compress(Bitmap.CompressFormat.PNG, 1, outputStream);
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