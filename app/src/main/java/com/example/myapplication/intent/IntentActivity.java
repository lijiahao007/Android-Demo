package com.example.myapplication.intent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;

public class IntentActivity extends AppCompatActivity {

    private EditText editText;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_email);

        editText = findViewById(R.id.et_message);

        // 1. 点击将消息发送到其他软件中。（隐式Intent， 没有指定应用软件。）
        findViewById(R.id.btn_send).setOnClickListener(view -> {
            String textMessage = getEditText();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
            startActivity(sendIntent);
        });

        // 2. 打电话
        findViewById(R.id.btn_call).setOnClickListener(view -> {
            String phone = getEditText();
            if (!TextUtils.isDigitsOnly(phone)) {
                Toast.makeText(this, "格式错误", Toast.LENGTH_SHORT).show();
            } else {
                Intent callIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
        });

        // 2.1 打电话1 (与2相同)
        findViewById(R.id.btn_call1).setOnClickListener(view -> {
            String phone = getEditText();
            if (!TextUtils.isDigitsOnly(phone)) {
                Toast.makeText(this, "格式错误", Toast.LENGTH_SHORT).show();
            } else {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
        });

        // 3. 启动浏览器
        findViewById(R.id.btn_browser).setOnClickListener(view -> {
            String url = getEditText();
            Intent showIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(showIntent);
        });

        // 4. 打开语音助手
        findViewById(R.id.btn_voice).setOnClickListener(view -> {
            Intent voiceCommandIntent = new Intent(Intent.ACTION_VOICE_COMMAND);
            startActivity(voiceCommandIntent);
        });

        // 5. 网络搜索
        findViewById(R.id.btn_web_search).setOnClickListener(view -> {
            String msg = getEditText(); // 如果是url，就打开网站。 否则搜索对应字符串
            Intent searchIntent = new Intent(Intent.ACTION_WEB_SEARCH);
            searchIntent.putExtra(SearchManager.QUERY, msg);
            startActivity(searchIntent);
        });

        // 6. 显示联系人
        findViewById(R.id.btn_content).setOnClickListener(view -> {
            Intent contentIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/4"));
            startActivity(contentIntent);
        });

    }



    private String getEditText() {
        return editText.getText().toString();
    }
}