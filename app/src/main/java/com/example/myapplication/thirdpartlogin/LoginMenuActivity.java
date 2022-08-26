package com.example.myapplication.thirdpartlogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.MenuAdapter;
import com.example.myapplication.MenuRecyclerView;
import com.example.myapplication.R;

import java.util.ArrayList;

public class LoginMenuActivity extends AppCompatActivity {

    private MenuRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setMenuList(new ArrayList<MenuAdapter.MenuInfo>() {{
            add(new MenuAdapter.MenuInfo("Google 登录", GoogleLoginActivity.class));
        }});
    }
}