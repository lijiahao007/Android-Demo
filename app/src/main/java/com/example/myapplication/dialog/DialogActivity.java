package com.example.myapplication.dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.MenuAdapter;
import com.example.myapplication.MenuRecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityDialogBinding;

import java.util.ArrayList;

public class DialogActivity extends BaseActivity<ActivityDialogBinding> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    dialog2Fragment.setCancelable(false);
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

            add(new MenuAdapter.MenuInfo("Loading Dialog", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadingDialog loadingDialog = LoadingDialog.newInstance(false, false, "加载中");
                    loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Log.e(TAG, "onDismiss");
                        }
                    });
                    loadingDialog.setLoadingDialogOnCancelListener(new LoadingDialog.LoadingDialogOnCancelListener() {
                        @Override
                        public void onCancel() {
                            Log.e(TAG, "onCancel");
                        }
                    });

                    loadingDialog.setOnCancelling(new LoadingDialog.OnCancelling() {
                        @Override
                        public void onCancelling() {
                            Log.e(TAG, "onCancelling");
                            mBaseActivityHandler.postDelayed(() -> {
                                loadingDialog.dismiss();
                            }, 2000);
                        }
                    });
                    loadingDialog.show(getSupportFragmentManager(), LoadingDialog.class.getSimpleName());
                }
            }));

        }});

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}