package com.example.myapplication.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.FragmentManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.DialogLoadingWhiteBinding;
import com.macrovideo.sdk.tools.LogUtils;

/**
 * Created by admin on 2018/1/30.
 */

public class LoadingDialog extends BaseDialogFragment</*DialogLoadingNewBinding*/DialogLoadingWhiteBinding> {

    private static final String TAG = "LoadingDialog";
    private static final String KEY_CAN_CANCEL_OUDSIDE = "KEY_CAN_CANCEL_OUDSIDE";
    private static final String KEY_DIALOG_TEXT = "KEY_DIALOG_TEXT";
    private static final String KEY_IS_DARK = "KEY_IS_DARK";
    private static final String KEY_IS_HORIZONTAL = "KEY_IS_HORIZONTAL";

    private Activity mAttachActivity;
    private String mDialogText;
    private boolean mIsDark = false;
    private boolean mIsHorizontal = false;
    private /*static*/ LoadingDialogOnCancelListener mLoadingDialogOnCancelListener;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private OnCancelling onCancelling;

    public static LoadingDialog newInstance(boolean canCancelOutside, String dialogText) {
        LoadingDialog loadingDialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_CAN_CANCEL_OUDSIDE, canCancelOutside);
        bundle.putString(KEY_DIALOG_TEXT, dialogText);
        loadingDialog.setArguments(bundle);
        return loadingDialog;
    }

    public static LoadingDialog newInstance(boolean canCancelOutside, String dialogText, boolean isDark) {
        LoadingDialog loadingDialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_CAN_CANCEL_OUDSIDE, canCancelOutside);
        bundle.putString(KEY_DIALOG_TEXT, dialogText);
        bundle.putBoolean(KEY_IS_DARK, isDark);
        loadingDialog.setArguments(bundle);
        return loadingDialog;
    }

    //add by xjw 190803 增加控制是否点击返回键也不dimiss, 目前主要用于一些绑定操作
    public static LoadingDialog newInstance(boolean canCancel, boolean canCancelOutside, String dialogText) {
        LoadingDialog loadingDialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_CAN_CANCEL_OUDSIDE, canCancelOutside);
        bundle.putString(KEY_DIALOG_TEXT, dialogText);
        loadingDialog.setArguments(bundle);
        loadingDialog.setCancelable(canCancel);
        return loadingDialog;
    }

    public static LoadingDialog newInstance(boolean canCancel, boolean canCancelOutside, boolean isDark,
                                            String dialogText, boolean isHorizontal) {
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.setCancelable(canCancel);
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_CAN_CANCEL_OUDSIDE, canCancelOutside);
        bundle.putBoolean(KEY_IS_DARK, isDark);
        bundle.putString(KEY_DIALOG_TEXT, dialogText);
        bundle.putBoolean(KEY_IS_HORIZONTAL, isHorizontal);
        loadingDialog.setArguments(bundle);
        return loadingDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAttachActivity = (Activity) context;
    }
    boolean isFirstPressBack = true;

    @Override
    protected void initView(View view) {
        isFirstPressBack = true;
        Bundle bundle = getArguments();
        if (bundle != null) {
            getDialog().setCanceledOnTouchOutside(bundle.getBoolean(KEY_CAN_CANCEL_OUDSIDE));
            mDialogText = bundle.getString(KEY_DIALOG_TEXT);
            mIsDark = bundle.getBoolean(KEY_IS_DARK, false);
            mIsHorizontal = bundle.getBoolean(KEY_IS_HORIZONTAL, false);
        }
//        view.setAlpha(0.55f);
        view.setAlpha(1.0f);
        if (mIsHorizontal) {
            binding.llVertical.setVisibility(View.GONE);
            binding.llHorizontal.setVisibility(View.VISIBLE);
            if (mIsDark) {
                binding.llParentLayout.setBackground(getResources().getDrawable(R.drawable.dialog_loading_bg_dark));
            }
            if (mDialogText != null && !"".equals(mDialogText)) {
                binding.tvContent.setText(mDialogText);
            }
        } else {
            if (!mDialogText.equals("")) {
                binding.txtLoadingDialog.setText(mDialogText);
            }
            if (mIsDark) {
                binding.txtLoadingDialog.setTextColor(getResources().getColor(R.color.color_invariant_ffffff));
                binding.llParentLayout.setBackground(getResources().getDrawable(R.drawable.dialog_loading_bg_dark));
            }
        }
    }

    @Override
    protected int[] bindClickId() {
        return new int[0];
    }

    @Override
    protected void onClicked(View view) {

    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
            }
            DisplayMetrics dm = new DisplayMetrics();
            mAttachActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && isFirstPressBack) {
                        isFirstPressBack = false;
                        binding.txtLoadingDialog.setText("取消中");
                        binding.tvContent.setText("取消中");
                        if (onCancelling != null) {
                            onCancelling.onCancelling();
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.e(TAG, "onDismiss");
        mIsShowing = false;
        super.onDismiss(dialog);
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Log.e(TAG, "onCancel");
        mIsShowing = false;
        super.onCancel(dialog);
        if (mLoadingDialogOnCancelListener != null) {
            mLoadingDialogOnCancelListener.onCancel();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (mAttachActivity != null) {
            mAttachActivity = null;
        }
        if (mLoadingDialogOnCancelListener != null) {
            mLoadingDialogOnCancelListener = null;
        }
        if (mOnDismissListener != null) {
            mOnDismissListener = null;
        }
        super.onDestroy();
    }

    public void setLoadingDialogOnCancelListener(LoadingDialogOnCancelListener onCancelListener) {
        mLoadingDialogOnCancelListener = onCancelListener;
    }

    public interface LoadingDialogOnCancelListener {
        void onCancel();
    }

    public interface OnCancelling {
        void onCancelling();
    }

    private boolean mIsShowing = false;

    @Override
    public void show(FragmentManager manager, String tag) {
        mIsShowing = true;
        DialogFragmentUtlis.show(this, manager, tag);
    }

    @Override
    public void dismiss() {
        mIsShowing = false;
//        super.dismiss();
        super.dismissAllowingStateLoss();
    }


    public void cancel() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.cancel();
        }
    }

    public boolean isShowing() {
        return mIsShowing;
    }

    public OnCancelling getOnCancelling() {
        return onCancelling;
    }

    public void setOnCancelling(OnCancelling onCancelling) {
        this.onCancelling = onCancelling;
    }
}
