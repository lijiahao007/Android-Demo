package com.example.myapplication.album;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class MediaItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private int dividerHeight;
    private Paint dividerPaint;
    private Paint textPaint;

    public MediaItemDecoration(Context context) {
        super();
        this.mContext = context;
        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.media_divider_height);
        dividerPaint = new Paint();
        dividerPaint.setColor(ResourcesCompat.getColor(context.getResources(), R.color.purple_500, null));
        textPaint = new Paint();
        textPaint.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.white, null));
        textPaint.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.media_divider_text_size));
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        int visibleChildCount = parent.getChildCount();
        for (int i = 0; i < visibleChildCount; i++) {
            View view = parent.getChildAt(i);
            int childAdapterPosition = parent.getChildAdapterPosition(view);
            canvas.drawRect(view.getLeft(), view.getTop() - dividerHeight, parent.getRight(), view.getTop(), dividerPaint);
            canvas.drawText(String.valueOf(childAdapterPosition), view.getLeft() + view.getWidth()/2, view.getTop()- dividerHeight /2 , textPaint);
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        View firstVisibleView = parent.getChildAt(0); // 第一个可见的view
        int childAdapterPosition = parent.getChildAdapterPosition(firstVisibleView);
        if (firstVisibleView.getBottom() <= dividerHeight) { // 如果第一个View的底部再dividerSize之上，（第二个View的divider触碰到顶部divider）(表层的View向上移，底层的View会跟着RecyclerView向上移动)
            canvas.drawRect(firstVisibleView.getLeft(), 0, parent.getRight(), firstVisibleView.getBottom(), dividerPaint);
            canvas.drawText(String.valueOf(childAdapterPosition), firstVisibleView.getLeft() + firstVisibleView.getWidth() / 2, firstVisibleView.getBottom() - dividerHeight/2, textPaint);
        } else {
            canvas.drawRect(0, 0, parent.getRight(), dividerHeight, dividerPaint);
            canvas.drawText(String.valueOf(childAdapterPosition), firstVisibleView.getLeft() + firstVisibleView.getWidth()/2, dividerHeight/2 , textPaint);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = dividerHeight;

    }
}
