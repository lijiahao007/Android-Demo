package com.example.myapplication.activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;

public class ExampleActivity1 extends AppCompatActivity {

    static final String TAG = "ExampleActivity1";
    private ActivityResultLauncher<Object> exampleActivity4Launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        exampleActivity4Launcher = registerForActivityResult(new ActivityResultContract<Object, String>() {

            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, @Nullable Object input) {
                return new Intent(ExampleActivity1.this, ExampleActivity4.class);
            }

            @Override
            public String parseResult(int resultCode, @Nullable Intent intent) {
                if (resultCode == 200 && intent != null) {
                    return intent.getStringExtra("msg");
                }
                return null;
            }
        }, result -> { // callback
            Toast.makeText(this, "\nregisterForActivityResult: " + result, Toast.LENGTH_LONG).show();
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example1);

        // 1. 跳转时生命周期变化
        findViewById(R.id.lifecycle_activity).setOnClickListener(view -> {
            startActivity(new Intent(this, ExampleActivity2.class));
        });
        Log.i("Lifecycle", TAG + " onCreate()");

        // 2. 使用Intent进行参数传递
        findViewById(R.id.param_pass).setOnClickListener(view -> {
            Intent intent = new Intent(this, ExampleActivity3.class);
            intent.putExtra("name", "helloworld");
            startActivity(intent);
        });

        // 2.1 传递Serializable序列化对象
        findViewById(R.id.param_pass1).setOnClickListener(view -> {
            Intent intent = new Intent(this, ExampleActivity3.class);
            intent.putExtra("student", new Student("helloworld", 10, 170.0));
            startActivity(intent);
        });

        // 2.1 传递Parcelable序列化对象
        findViewById(R.id.param_pass2).setOnClickListener(view -> {
            Intent intent = new Intent(this, ExampleActivity3.class);
            Teacher[] list = new Teacher[3];
            list[0] = new Teacher("helloworld1", 11, 171.0);
            list[1] = new Teacher("helloworld2", 12, 172.0);
            list[2] = new Teacher("helloworld3", 13, 173.0);
            intent.putExtra("teacher", list);
            startActivity(intent);
        });

        // 3. 使用startActivityForResult接收下一个activity返回的参数
        findViewById(R.id.return_param).setOnClickListener(view -> {
            Intent intent = new Intent(this, ExampleActivity4.class);
            Teacher[] list = new Teacher[3];
            list[0] = new Teacher("helloworld1", 11, 171.0);
            list[1] = new Teacher("helloworld2", 12, 172.0);
            list[2] = new Teacher("helloworld3", 13, 173.0);
            intent.putExtra("teacher", list);
            startActivityForResult(intent, 1);
        });


        // 4. 使用startActivityForResult接收下一个activity返回的参数
        findViewById(R.id.return_param1).setOnClickListener(view -> {
            exampleActivity4Launcher.launch(null);
        });

        // 5. singleTask 启动模式
        findViewById(R.id.launch_mode1).setOnClickListener(view -> {
            Intent intent = new Intent(this, LaunchModeActivity1.class);
            startActivity(intent);
        });

        // 6. singleTop 启动模式
        findViewById(R.id.launch_mode2).setOnClickListener(view -> {
            Intent intent = new Intent(this, LaunchModeActivity3.class);
            startActivity(intent);
        });

        // 7. singleInstance 启动模式
        findViewById(R.id.launch_mode3).setOnClickListener(view -> {
            Intent intent = new Intent(this, LaunchModeActivity4.class);
            intent.putExtra("prevTaskId", getTaskId());
            startActivity(intent);
        });

        // 8. 使用Intent Flag 来使用各种启动模式启动Activity
        findViewById(R.id.intent_launch_mode1).setOnClickListener(view -> {
            Intent intent = new Intent(this, IntentFlagActivity.class);
            startActivity(intent);
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                if (resultCode == 200 && data != null) {
                    Toast.makeText(this, data.getStringExtra("msg"), Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Lifecycle", TAG + " onStart()");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Lifecycle", TAG + " onResume()");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Lifecycle", TAG + " onPause()");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Lifecycle", TAG + " onStop()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Lifecycle", TAG + " onDestroy()");
    }
}