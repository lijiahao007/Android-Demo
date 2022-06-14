package com.example.myapplication.recycleview.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class PaddingItemDecoration extends RecyclerView.ItemDecoration {

    private final int dividerSize;
    private final Paint dividerPaint1;
    private final Paint dividerPaint3;
    private final Paint dividerPaint2;


    public PaddingItemDecoration(Context context) {
        super();
        dividerSize = context.getResources().getDimensionPixelSize(R.dimen.item_decoration_padding);
        dividerPaint1 = new Paint();
        dividerPaint1.setColor(context.getResources().getColor(R.color.item_decoration));
        dividerPaint1.setAlpha(50);

        dividerPaint2 = new Paint();
        dividerPaint2.setColor(context.getResources().getColor(R.color.item_decoration1));
        dividerPaint2.setAlpha(50);

        dividerPaint3 = new Paint();
        dividerPaint3.setColor(context.getResources().getColor(R.color.item_decoration2));
        dividerPaint3.setAlpha(50);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 使用outRect拓展view所占用的空间。
        outRect.bottom = dividerSize;
        outRect.left = dividerSize;
        outRect.right = dividerSize;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        // 在使用 getItemOffset 让出空间后，可以另外绘制背景

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i); // 获取每个childView

            // 1. 底部divider
            int left = view.getLeft();
            int right = view.getRight();
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerSize;
            c.drawRect(left, top, right, bottom, dividerPaint1);

            // 2. 右侧divider
            int top1 = view.getTop() - dividerSize / 2;
            int bottom1 = view.getBottom() + dividerSize / 2;
            int left1 = view.getRight();
            int right1 = view.getRight() + dividerSize;
            c.drawRect(left1, top1, right1, bottom1, dividerPaint2);

            // 3. 左侧divider
            int top2 = view.getTop() - dividerSize / 2;
            int bottom2 = view.getBottom() + dividerSize / 2;
            int left2 = view.getLeft() - dividerSize;
            int right2 = view.getLeft();
            c.drawRect(left2, top2, right2, bottom2, dividerPaint3);

        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
}
