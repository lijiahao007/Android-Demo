package com.example.myapplication.recycleview;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class FlowLayoutManager extends RecyclerView.LayoutManager {

    private static final String TAG = "FlowLayoutManager";

    // 是否开启铺平整行的功能
    private boolean isOpenFlatLine = false;


    public FlowLayoutManager(boolean isOpenFlatLine) {
        this.isOpenFlatLine = isOpenFlatLine;
    }

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

        View lastView = null;
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
                if (lastView != null) {
                    flatLineView(lastView);
                }
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
            lastView = view;
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

        // 1. 填充子View
        int consume = fill(dy, recycler);

        // 2. 滚动子View
        offsetChildrenVertical(-consume);

        // 3. 回收子View
        recycler(consume, recycler);


        return dy;
    }


    private int fill(int dy, RecyclerView.Recycler recycler) {
        if (dy == 0 || getItemCount() == 0) {
            return 0;
        }


        if (dy > 0) {
            // 填充尾部内容
            while (true) {
                // 1. 找到锚点View
                int childCount = getChildCount();
                View anchorView = getChildAt(childCount - 1);
                int decoratedRight = getDecoratedRight(anchorView);
                int decoratedTop = getDecoratedTop(anchorView);
                int decoratedBottom = getDecoratedBottom(anchorView);
                int adapterPosition = getPosition(anchorView);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) anchorView.getLayoutParams();
                int anchorViewRightMargin = layoutParams.rightMargin;
                int anchorViewBottomMargin = layoutParams.bottomMargin;

                int fillPosition = adapterPosition + 1; // 被填充的Item在Adapter中的位置。

                // 边缘检测
                if (fillPosition >= getItemCount()) {
                    if (decoratedBottom + anchorViewBottomMargin - dy < getHeight()) {
                        // 如果View的底线超过了decoratedBottom + anchorViewBottomMargin, 就修正具体滑动的距离。
                        return decoratedBottom + anchorViewBottomMargin - getHeight();
                    }
                    return dy;
                }

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
                if (decoratedRight + anchorViewRightMargin + width + leftMargin <= getWidth()) {
                    // 不用换行
                    layoutDecorated(viewForPosition,
                            decoratedRight + anchorViewRightMargin + leftMargin,
                            decoratedTop,
                            decoratedRight + anchorViewRightMargin + leftMargin + width,
                            decoratedTop + height);
                } else {
                    flatLineView(anchorView);
                    // 需要换行
                    layoutDecorated(viewForPosition,
                            leftMargin,
                            decoratedBottom + anchorViewBottomMargin + topMargin,
                            leftMargin + width,
                            decoratedBottom + anchorViewBottomMargin + topMargin + height);
                }
            }
        } else {
            // 填充头部元素
            while (true) {
                // 1. 找到锚点元素
                View anchorView = getChildAt(0);
                int anchorPosition = getPosition(anchorView);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) anchorView.getLayoutParams();
                int anchorTopMargin = layoutParams.topMargin;
                int decoratedTop = getDecoratedTop(anchorView);
                int fillPosition = anchorPosition - 1;

                // 边缘检测
                if (fillPosition < 0) {
                    if (decoratedTop - anchorTopMargin - dy > 0) {
                        return decoratedTop - anchorTopMargin;
                    }
                    return dy;
                }

                // 2. 从Recycler获取目标View,
                View viewForPosition = recycler.getViewForPosition(fillPosition);
                RecyclerView.LayoutParams targetViewParam = (RecyclerView.LayoutParams) viewForPosition.getLayoutParams();
                int leftMargin = targetViewParam.leftMargin;
                int rightMargin = targetViewParam.rightMargin;
                int bottomMargin = targetViewParam.bottomMargin;

                // 3. 测量View的大小
                addView(viewForPosition, 0);
                measureChildWithMargins(viewForPosition, 0, 0);
                int height = getDecoratedMeasuredHeight(viewForPosition);
                int width = getDecoratedMeasuredWidth(viewForPosition);


                // 4. 查看锚点元素那一行还有没有空位，如果有，则插入到那一行的第一个。如果没有则插入到上一行的第一个
                ArrayList<View> currentLineViews = getCurrentLineView(anchorView);
                Log.i(TAG, currentLineViews.toString());
                View lastView = currentLineViews.get(currentLineViews.size() - 1);
                int rightestInLine = getDecoratedRight(lastView);

                if (rightestInLine + width + leftMargin + rightMargin <= getWidth()) {
                    // 插入当前行的第一个。
                    layoutDecorated(viewForPosition,
                            leftMargin,
                            decoratedTop,
                            leftMargin + width,
                            decoratedTop + height);

                    int lineStart = leftMargin + width + rightMargin;
                    // 同一行的后续Item，重新layout
                    for (int i = 0; i < currentLineViews.size(); i++) {
                        View curLine = currentLineViews.get(i);
                        RecyclerView.LayoutParams layoutParam = (RecyclerView.LayoutParams) curLine.getLayoutParams();
                        int viewHeight = getDecoratedMeasuredHeight(curLine);
                        int viewWidth = getDecoratedMeasuredWidth(curLine);

                        layoutDecorated(curLine,
                                lineStart + layoutParam.leftMargin,
                                decoratedTop,
                                lineStart + layoutParam.leftMargin + viewWidth,
                                decoratedTop + viewHeight
                        );

                        lineStart += layoutParam.leftMargin + viewWidth + layoutParam.rightMargin;
                    }
                } else {
                    // 插入上一行的第一个

                    // 如果超出范围，则不添加
                    if (decoratedTop - anchorTopMargin - bottomMargin < 0) {
                        removeAndRecycleView(viewForPosition, recycler);
                        break;
                    }

                    flatLineView(anchorView);

                    layoutDecorated(viewForPosition,
                            leftMargin,
                            decoratedTop - anchorTopMargin - bottomMargin - height,
                            leftMargin + width,
                            decoratedTop - anchorTopMargin - bottomMargin
                    );
                }
            }
        }
        return dy;
    }

    /**
     * @param anchorView 返回与anchorView同一行的View
     * @return
     */
    private ArrayList<View> getCurrentLineView(View anchorView) {
        int anchorTop = getDecoratedTop(anchorView);
        ArrayList<View> res = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            int decoratedTop = getDecoratedTop(childAt);
            if (decoratedTop == anchorTop) {
                res.add(childAt);
            }
        }
        return res;
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

    /**
     * 将anchorView所在的行中的每个View扩宽，以填满整行
     *
     * @param anchorView
     */
    private void flatLineView(View anchorView) {
        if (!isOpenFlatLine) {
            return;
        }

        ArrayList<View> currentLineViews = getCurrentLineView(anchorView);
        View lastViewInLine = currentLineViews.get(currentLineViews.size() - 1);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) lastViewInLine.getLayoutParams();
        int decoratedRight = getDecoratedRight(lastViewInLine);
        int decoratedTop = getDecoratedTop(lastViewInLine);
        // 这一行右边的空位
        int dis = getWidth() - decoratedRight - layoutParams.rightMargin;
        int disPerView = dis / currentLineViews.size();

        int lineStart = 0;
        for (int i = 0; i < currentLineViews.size(); i++) {
            View curView = currentLineViews.get(i);
            RecyclerView.LayoutParams curParam = (RecyclerView.LayoutParams) curView.getLayoutParams();
            int width = getDecoratedMeasuredWidth(curView);
            int height = getDecoratedMeasuredHeight(curView);
            layoutDecorated(curView,
                    lineStart + curParam.leftMargin,
                    decoratedTop,
                    lineStart + curParam.leftMargin + width + disPerView,
                    decoratedTop + height);
            lineStart += curParam.leftMargin + width + curParam.rightMargin + disPerView;
        }
    }
}
