package com.example.myapplication.recycleview.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class SpecialItemDecoration extends RecyclerView.ItemDecoration {

    private final int dividerSize;
    private final Paint dividerPaint1;

    public SpecialItemDecoration(Context context) {
        super();
        dividerSize = context.getResources().getDimensionPixelSize(R.dimen.item_decoration_padding);
        dividerPaint1 = new Paint();
        dividerPaint1.setColor(context.getResources().getColor(R.color.item_decoration));
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 使用outRect拓展view所占用的空间。
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        // 在使用 getItemOffset 让出空间后，可以另外绘制背景
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i); // 获取每个childView
            int position = parent.getChildAdapterPosition(view);
            if (position % 3 == 0) {
                int top = view.getTop() + dividerSize * 2;
                int bottom = top + dividerSize * 3;
                int left = view.getLeft() + dividerSize * 2;
                int right = left + dividerSize * 3;
                c.drawRect(left, top, right, bottom, dividerPaint1);
            }

        }
    }


}
