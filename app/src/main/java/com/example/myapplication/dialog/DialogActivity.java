package com.example.myapplication.dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.MenuAdapter;
import com.example.myapplication.MenuRecyclerView;
import com.example.myapplication.R;

import java.util.ArrayList;

public class DialogActivity extends AppCompatActivity {

    private MenuRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        recyclerView = findViewById(R.id.recycler_view);
        MenuAdapter adapter = new MenuAdapter(new ArrayList<MenuAdapter.MenuInfo>() {{
            add(new MenuAdapter.MenuInfo("Dialog1--Dialog + DialogFragment 弹出式", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager supportFragmentManager = getSupportFragmentManager();
                    Dialog1Fragment dialog1Fragment = new Dialog1Fragment();
                    dialog1Fragment.show(supportFragmentManager, "dialog1");
                }
            }));
            add(new MenuAdapter.MenuInfo("Dialog2--Dialog + DialogFragment 嵌入式", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 嵌入式的显示Dialog
                    FragmentManager supportFragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.add(android.R.id.content, new Dialog1Fragment(), "dialog2");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }));

            add(new MenuAdapter.MenuInfo("Dialog3--AlertDialog + DialogFragment 弹出式", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 嵌入式的显示Dialog
                    Dialog2Fragment dialog2Fragment = new Dialog2Fragment();
                    dialog2Fragment.show(getSupportFragmentManager(), "dialog3");
                }
            }));


            add(new MenuAdapter.MenuInfo("Dialog4--AlertDialog + DialogFragment 嵌入式", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 嵌入式的显示Dialog
                    Dialog2Fragment dialog2Fragment = new Dialog2Fragment();
                    FragmentManager supportFragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.add(android.R.id.content, dialog2Fragment, "dialog4");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }));

            add(new MenuAdapter.MenuInfo("Dialog5--普通Dialog，屏幕旋转会消失", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 嵌入式的显示Dialog
                    new AlertDialog.Builder(DialogActivity.this)
                            .setMessage("Dialog5")
                            .setCancelable(false)
                            .show();
                }
            }));

            add(new MenuAdapter.MenuInfo("Dialog6--Bottom Dialog", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog3Fragment dialog3Fragment = new Dialog3Fragment();
                    dialog3Fragment.show(getSupportFragmentManager(), "dialog6");
                }
            }));
        }});

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}