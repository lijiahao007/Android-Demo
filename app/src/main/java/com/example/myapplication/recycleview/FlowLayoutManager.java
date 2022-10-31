package com.example.myapplication.recycleview;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.macrovideo.sdk.tools.LogUtils;

// TODO: 回收

public class FlowLayoutManager extends RecyclerView.LayoutManager {

    private static final String TAG = "FlowLayoutManager";


    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    // 显示初始内容
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);

        int beginIndex = 0;
        int fixOffset = 0;
        // 非第一次调用onLayoutChild时，设置开始元素和垂直修正偏移
        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childAt.getLayoutParams();
            beginIndex = getPosition(childAt);
            fixOffset = getDecoratedTop(childAt) - layoutParams.topMargin;
            Log.i(TAG, "childCount = " + getChildCount() + "  beginIndex=" + beginIndex + "  fixOffset=" + fixOffset);
        }


        //这里是在布局前将所有在显示的HoldView从RecyclerView中剥离，将其放在缓存中，方便重新布局时使用
        detachAndScrapAttachedViews(recycler);

        // 剩余高度足够才会填充View
        int totalHeight = getHeight();

        //curLineWidth 累加item布局时的x轴偏移
        int curLineWidth = 0;
        //curLineTop 累加item布局时的y轴偏移
        int curLineTop = 0;
        int lastLineMaxHeight = 0;


        for (int i = beginIndex; i < getItemCount() && totalHeight > 0; i++) {
            View view = recycler.getViewForPosition(i);
            //获取每个item的布局参数，计算每个item的占用位置时需要加上margin
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            // 添加每一个View
            addView(view);
            // 测量View的宽高 (支持margin)
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin;
            int height = getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin;
            //累加当前行已有item的宽度
            curLineWidth += width;
            if (curLineWidth <= getWidth()) {
                //如果累加的宽度小于等于RecyclerView的宽度，不需要换行
                layoutDecorated(view,
                        curLineWidth - width + params.leftMargin, // 前一个Right + leftMargin
                        curLineTop + params.topMargin,  // 上一行bottom + bottomMargin + topMargin
                        curLineWidth - params.rightMargin, // 前一个Right + leftMargin + width
                        curLineTop + height - params.bottomMargin); // 上一行bottom + height - topMargin
                lastLineMaxHeight = Math.max(lastLineMaxHeight, height);
            } else {
                totalHeight -= height;
                // 换行
                curLineWidth = width;
                if (lastLineMaxHeight == 0) {
                    lastLineMaxHeight = height;
                }
                //记录当前行top
                curLineTop += lastLineMaxHeight;
                layoutDecorated(
                        view,
                        params.leftMargin,
                        curLineTop + params.topMargin,
                        width - params.rightMargin,
                        curLineTop + height - params.bottomMargin
                );
                lastLineMaxHeight = height;
            }
        }
        offsetChildrenVertical(fixOffset);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }


    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // dy > 0 --> 手指往上滑， 需要填充下方元素
        // dy < 0 --> 手指往下滑， 需要填充上方的元素

//        Log.i("FlowLayoutManager", "dy=" + dy);

        // 1. 填充子View
        fill(dy, recycler);

        // 2. 滚动子View
        offsetChildrenVertical(-dy);

        // 3. 回收子View
        recycler(dy, recycler);

//        Log.i(TAG, "scrapList.size=" + recycler.getScrapList().size() + " childCount=" + getChildCount());

        return dy;
    }


    private void fill(int dy, RecyclerView.Recycler recycler) {
        // 1. 获取需要填充区域的总高度
        int totalHeight = Math.abs(dy);

        if (dy > 0) {
            // 填充尾部内容
            while (true) {
                // 1. 找到锚点View
                int childCount = getChildCount();
                View childAt = getChildAt(childCount - 1);
                int decoratedRight = getDecoratedRight(childAt);
                int decoratedTop = getDecoratedTop(childAt);
                int decoratedBottom = getDecoratedBottom(childAt);
                int adapterPosition = getPosition(childAt);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childAt.getLayoutParams();
                int anchorViewRightMargin = layoutParams.rightMargin;
                int anchorViewBottomMargin = layoutParams.bottomMargin;

                int fillPosition = adapterPosition + 1; // 被填充的Item在Adapter中的位置。

                // 2. addView
                View viewForPosition = recycler.getViewForPosition(fillPosition);
                addView(viewForPosition);
                RecyclerView.LayoutParams insertViewParam = (RecyclerView.LayoutParams) viewForPosition.getLayoutParams();


                // 3. 计算View的大小
                measureChildWithMargins(viewForPosition, 0, 0);
                int width = getDecoratedMeasuredWidth(viewForPosition);
                int height = getDecoratedMeasuredHeight(viewForPosition);

                // 一直添加View直到View超出了屏幕范围
                int topMargin = insertViewParam.topMargin;
                int leftMargin = insertViewParam.leftMargin;

                if (decoratedRight + width > getWidth() && decoratedBottom + topMargin > getHeight()) {
                    removeAndRecycleView(viewForPosition, recycler);
                    break;
                }

                // 4. layoutView
                if (decoratedRight + width <= getWidth()) {
                    // 不用换行
                    layoutDecorated(viewForPosition,
                            decoratedRight + anchorViewRightMargin + leftMargin,
                            decoratedTop,
                            decoratedRight + anchorViewRightMargin + leftMargin + width,
                            decoratedTop + height);
                } else {
                    // 需要换行
                    layoutDecorated(viewForPosition,
                            leftMargin,
                            decoratedBottom + anchorViewBottomMargin + topMargin,
                            leftMargin + width,
                            decoratedBottom + topMargin + height);
                }
            }
        } else {
            // 填充头部元素
            while (true) {
                // 1. 找到锚点元素
                View childAt = getChildAt(0);
                int firstPosition = getPosition(childAt);
                int fillPosition = firstPosition - 1;

                // 查看锚点元素那一行还有没有空位，如果有，则插入到那一行的第一个。如果没有则插入到上一行的第一个




            }
        }
    }

    /**
     * 把超出范围的回收掉
     *
     * @param dy
     * @param recycler
     */
    private void recycler(int dy, RecyclerView.Recycler recycler) {
        if (dy > 0) {
            // 向上滑，回收顶部元素
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                // 获取该元素的底部位置，如果
                if (childAt == null) {
                    continue;
                }
                int decoratedBottom = getDecoratedBottom(childAt);

                // 从第一个元素遍历，如果遇到某个bottom大于0的元素，说明后续元素都是可见的。
                if (decoratedBottom > 0) {
                    break;
                }
                removeAndRecycleView(childAt, recycler);
            }
        } else {
            // 向下滑，回收尾部元素
            for (int i = getChildCount() - 1; i > 0; i--) {
                View childAt = getChildAt(i);
                if (childAt == null) {
                    continue;
                }

                int decoratedTop = getDecoratedTop(childAt);
                if (decoratedTop < getHeight()) {
                    break;
                }
                removeAndRecycleView(childAt, recycler);
            }
        }
    }
}
