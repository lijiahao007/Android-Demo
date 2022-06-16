package com.example.myapplication.album;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class MediaItemDecoration extends RecyclerView.ItemDecoration {

    private final Bitmap rightBitmap;
    private final float textHeight;
    private Bitmap radioBitmap;
    private Context mContext;
    private int dividerHeight;
    private int normalDividerHeight;
    private Paint dividerPaint;
    private Paint textPaint;
    private Paint bitmapPaint;
    private Paint fadeOutPaint;
    private int radioBitmapWidth;

    public MediaItemDecoration(Context context) {
        super();
        this.mContext = context;
        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.media_divider_height);
        normalDividerHeight = context.getResources().getDimensionPixelSize(R.dimen.media_normal_divider_height);
        radioBitmapWidth = context.getResources().getDimensionPixelSize(R.dimen.media_normal_divider_height);
        dividerPaint = new Paint();
        dividerPaint.setColor(getColor(R.color.light_gray));
        textPaint = new Paint();
        textPaint.setColor(getColor(R.color.dark_gray));
        textPaint.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.media_divider_text_size));
        bitmapPaint = new Paint();
        radioBitmap = getBitmap(R.drawable.ic_radio_button_select);
        Matrix matrix = new Matrix();
        matrix.postScale(((float) radioBitmapWidth) / radioBitmap.getWidth(), ((float) radioBitmapWidth / radioBitmap.getHeight()));
        radioBitmap = Bitmap.createBitmap(radioBitmap, 0, 0, radioBitmap.getWidth(), radioBitmap.getHeight(), matrix, true);
        rightBitmap = getBitmap(R.drawable.ic_right);
        fadeOutPaint = new Paint();
        fadeOutPaint.setColor(getColor(R.color.dark_gray));
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textHeight = fontMetrics.bottom - fontMetrics.top;
    }


    private Bitmap getBitmap(int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(mContext.getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }


    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        int visibleChildCount = parent.getChildCount();
        MediaBeanAdapter adapter = (MediaBeanAdapter) parent.getAdapter();

        if (adapter == null) return;
        for (int i = 0; i < visibleChildCount; i++) {
            View view = parent.getChildAt(i);
            if (adapter.isFirstInDate(view)) {
                String date = (String) view.getTag();
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                canvas.drawRect(view.getLeft(), view.getTop() - dividerHeight, parent.getRight(), view.getTop(), dividerPaint);
                canvas.drawBitmap(radioBitmap, view.getLeft(), view.getTop() - radioBitmapWidth - (dividerHeight - radioBitmapWidth) / 2.0f, bitmapPaint);
                canvas.drawBitmap(rightBitmap, parent.getRight() - rightBitmap.getWidth(), view.getTop() - dividerHeight, bitmapPaint);
                canvas.drawText(date, view.getLeft() + radioBitmap.getWidth(), view.getTop() - (dividerHeight - textHeight) / 1.1f, textPaint);
            }
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        View firstVisibleView = parent.getChildAt(0); // 第一个可见的view
        if (firstVisibleView == null) {
            return;
        }
        String date = (String) firstVisibleView.getTag();
        MediaBeanAdapter adapter = (MediaBeanAdapter) parent.getAdapter();

        if (adapter == null) return;

        View lowestView = adapter.getLowestView(date);

        if (lowestView == null) {
            lowestView = firstVisibleView;
        }
        Log.i("MediaItemDecoration", "data:" + date + "  lowest bottom:" + lowestView.getBottom() + "/" + dividerHeight);

        if (lowestView.getBottom() <= dividerHeight && adapter.isLastLineInDate(lowestView)) { // 如果第一个View的底部再dividerSize之上，（第二个View的divider触碰到顶部divider）(表层的View向上移，底层的View会跟着RecyclerView向上移动)
            float fraction = 1 - lowestView.getBottom() / (dividerHeight * 1.0f);
            int currentColor = getCurrentColor(fraction, getColor(R.color.media_item_decoration_start_color), getColor(R.color.media_item_decoration_end_color));
            fadeOutPaint.setColor(currentColor);
            canvas.drawRect(lowestView.getLeft(), 0, parent.getRight(), lowestView.getBottom(), dividerPaint);
            canvas.drawBitmap(radioBitmap, parent.getLeft(), lowestView.getBottom() - radioBitmapWidth - (dividerHeight - radioBitmapWidth) / 2.0f, bitmapPaint);
            canvas.drawBitmap(rightBitmap, parent.getRight() - rightBitmap.getWidth(), lowestView.getBottom() - dividerHeight, bitmapPaint);
            canvas.drawText(date, lowestView.getLeft() + radioBitmap.getWidth(), lowestView.getBottom() - (dividerHeight - textHeight), textPaint);
            canvas.drawRect(lowestView.getLeft(), 0, parent.getRight(), lowestView.getBottom(), fadeOutPaint);
        } else {
            canvas.drawRect(0, 0, parent.getRight(), dividerHeight, dividerPaint);
            canvas.drawText(date, parent.getLeft() + radioBitmap.getWidth(), (dividerHeight - textHeight) + textHeight / 2, textPaint);
            canvas.drawBitmap(radioBitmap, parent.getLeft(), (dividerHeight - radioBitmapWidth) / 2.0f, bitmapPaint);
            canvas.drawBitmap(rightBitmap, parent.getRight() - rightBitmap.getWidth(), 0, bitmapPaint);
        }
    }

    private int getColor(int id) {
        return ResourcesCompat.getColor(mContext.getResources(), id, null);
    }

    private int getCurrentColor(float fraction, int startColor, int endColor) {
        int redCurrent;
        int blueCurrent;
        int greenCurrent;
        int alphaCurrent;

        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaStart = Color.alpha(startColor);

        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaEnd = Color.alpha(endColor);

        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaDifference = alphaEnd - alphaStart;

        redCurrent = (int) (redStart + fraction * redDifference);
        blueCurrent = (int) (blueStart + fraction * blueDifference);
        greenCurrent = (int) (greenStart + fraction * greenDifference);
        alphaCurrent = (int) (alphaStart + fraction * alphaDifference);
        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        MediaBeanAdapter adapter = (MediaBeanAdapter) parent.getAdapter();

        if (adapter.isInFirstLineInDate(view)) {
            outRect.top = dividerHeight;
        } else {
            outRect.top = normalDividerHeight;
        }
    }
}
