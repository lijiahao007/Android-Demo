package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.utils.DimenUtil;

import java.util.ArrayList;

public class MenuRecyclerView extends RecyclerView {
    private MenuAdapter adapter;

    public MenuRecyclerView(@NonNull Context context) {
        super(context);
        initView();
    }

    public MenuRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MenuRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MenuAdapter(new ArrayList<>());
        setAdapter(adapter);
        addItemDecoration(new RecyclerView.ItemDecoration() {

            Paint paint = new Paint();

            {
                paint.setColor(getResources().getColor(R.color.light_gray));
            }

            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
                    c.drawRect(new Rect(0, child.getBottom(), child.getWidth(), (int) (child.getBottom() + DimenUtil.dp2px(parent.getContext(), 5))), paint);
                }
            }

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = (int) DimenUtil.dp2px(getContext(), 5);
            }
        });
    }

    public void setMenuList(ArrayList<MenuAdapter.MenuInfo> menuList) {
        adapter.setMenuList(menuList);
    }

}
