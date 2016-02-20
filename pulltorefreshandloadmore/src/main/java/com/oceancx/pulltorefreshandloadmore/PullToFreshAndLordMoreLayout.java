package com.oceancx.pulltorefreshandloadmore;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * 实现RecyclerView的下拉刷新上拉加载更多
 * 今天最低目标:
 * 实现下拉悬停
 * Created by oceancx on 15/11/12.
 */
public class PullToFreshAndLordMoreLayout extends FrameLayout {

    float initDownY, initDownX, lastDownX, lastDownY;
    boolean shouldIntercept = false;
    boolean mRvReachTopWhenDown = false;
    boolean mRvReachBottomWhenDown = false;
    /**
     * child == 0 , 代表的是linearlayout
     */
    boolean setOnce = false;
    boolean setOnce1 = false;
    private int mTouchSlop;
    private ViewDragHelper mViewDragHelper;
    private RecyclerView mRecyclerView;
    private View header;
    private View footer;
    private View body;
    private RefreshAndLoadMoreListener mRLListener;
    private ProgressBar header_pgb;
    private TextView header_tv;
    private View header_hanging_layout;
    private ImageView header_down_arrow;
    private int offset_top = 0;

    public PullToFreshAndLordMoreLayout(Context context) {
        this(context, null);
    }


    public PullToFreshAndLordMoreLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToFreshAndLordMoreLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mViewDragHelper = ViewDragHelper.create(this, new DragHelperCallback());

    }

    public void setRefreshAndLoadMoreListener(RefreshAndLoadMoreListener mRLListener) {
        this.mRLListener = mRLListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //    initData();
        header = findViewById(R.id.header);
        body = findViewById(R.id.body);
        footer = findViewById(R.id.footer);
        mRecyclerView = (RecyclerView) body.findViewById(R.id.ryc_views);
        header_hanging_layout = header.findViewById(R.id.header_hanging_layout);
        header_pgb = (ProgressBar) header.findViewById(R.id.header_pgb);
        header_down_arrow = (ImageView) header.findViewById(R.id.header_down_arrow_img);
        header_tv = (TextView) header.findViewById(R.id.pull_to_refresh_tv);
        DebugLog.e("header:" + header + " body:" + body + " footer:" + footer);


    }

    /**
     * 测量两次 确定最终长度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = header.getMeasuredHeight() + footer.getMeasuredHeight() + getMeasuredHeight();
        int heightMode = MeasureSpec.EXACTLY;
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightSize, heightMode));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //onlyChild.layout(left, top, right, bottom);
        super.onLayout(changed, left, top, right, bottom);
        DebugLog.e("header height: " + header.getMeasuredHeight() + " scale:" + getResources().getDisplayMetrics().scaledDensity);
        offsetTopAndBottom(-header.getMeasuredHeight());
    }

    /**
     * 判断RcyclerView是否在顶点
     *
     * @return
     */
    private boolean isRvReachTop() {
        int checkPos = 0;

        View mFirstChild = mRecyclerView.getChildAt(0);
        int rvPos = mRecyclerView.getChildLayoutPosition(mFirstChild);
        // 当且仅当第一个可视节点==0 切其top==0的时候 此时rv到达顶部
        DebugLog.e("child top : " + mFirstChild.getTop());
        if (rvPos == checkPos && mFirstChild.getTop() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断RcyclerView是否在底部
     *
     * @return
     */
    private boolean isRvReachBottom() {
        int checkPos = mRecyclerView.getAdapter().getItemCount() - 1;

        View mLastChild = mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1);
        int rvPos = mRecyclerView.getChildAdapterPosition(mLastChild);
        // 当且仅当第一个可视节点==0 切其top==0的时候 此时rv到达顶部
        DebugLog.e("rvPos:" + rvPos + " child bottom: " + mLastChild.getBottom() + "rv B: " + mRecyclerView.getBottom() + " meight:" + getMeasuredHeight() + " body heifghtg:" + body.getMeasuredHeight());
        if (rvPos == checkPos && mLastChild.getBottom() - mLastChild.getTop() == mLastChild.getMeasuredHeight()) {
            DebugLog.e("reach bootom");
            return true;
        }
        return false;
    }

    /**
     * 刚开始的时候 Intercept == false
     * RecyclerView可以进行滑动
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        shouldIntercept = false;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                initDownY = ev.getY();
                initDownX = ev.getX();
                lastDownX = initDownX;
                lastDownY = initDownY;
                mViewDragHelper.processTouchEvent(ev);
                mRvReachTopWhenDown = isRvReachTop();
                mRvReachBottomWhenDown = isRvReachBottom();

            }
            break;
            case MotionEvent.ACTION_MOVE: {
                float my = ev.getY();
                float mx = ev.getX();
                float dx = mx - initDownX;
                float dy = my - initDownY;

                /**
                 * 判断是否为下移,如果是下移,切在down的时候RvReachTop,那么就要开始Intercept
                 * 下移的条件是  dy > touchSlop && checkDegree >45
                 */
                DebugLog.e("check Degree :" + checkDegree(dx, dy) + " dy : " + dy + " mRcouTop:" + mRvReachTopWhenDown);
                if (!shouldIntercept && mRvReachTopWhenDown && dy > mTouchSlop && (checkDegree(dx, dy) > 45 || checkDegree(dx, dy) < -45)) {
                    DebugLog.e("reach touch true" + "  dd : " + checkDegree(dx, dy));
                    shouldIntercept = true;
                    mViewDragHelper.processTouchEvent(ev);
                } else if (!shouldIntercept && mRvReachBottomWhenDown && -dy > mTouchSlop && (checkDegree(dx, dy) > 45 || checkDegree(dx, dy) < -45)) {
                    shouldIntercept = true;
                    mViewDragHelper.processTouchEvent(ev);
                }
            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                shouldIntercept = false;
            }
            break;
        }
        return shouldIntercept && mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    private int checkDegree(float dx, float dy) {
        return (int) (Math.atan(dy / dx) * 180 / Math.PI);
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mRvReachTopWhenDown) {
                    /**
                     * down事件的时候,Rv处于顶部,因此此处是下拉刷新
                     * 要做的事情:
                     * 1. 判断拉下来的高度是否超过hanging layout 的高度
                     *      如果超过了,那么就可以将hanging layout给hang了
                     *      如果没有超过,那么就直接把rv推上去
                     * 2. 在当次动画没有完结之前,对后来的Up事件不响应
                     * 3. 如果超过了hanging layout 的高度, 那么就等通知,等到rv通知后,将rv顶到顶部即可
                     */
                    int hanging_h = header_hanging_layout.getMeasuredHeight();
                    DebugLog.e("offset_top:" + offset_top + " hanging_h:" + hanging_h + " header height:" + header.getMeasuredHeight());

                    if (offset_top >= hanging_h) {

                        /**
                         * 不将rv上滑
                         */
                        if (offset_top == header.getMeasuredHeight() || mViewDragHelper.smoothSlideViewTo(getChildAt(0), 0, header.getMeasuredHeight())) {
                            ViewCompat.postInvalidateOnAnimation(this);
                            if (mRLListener != null) {
                                header_pgb.setVisibility(VISIBLE);
                                header_down_arrow.setVisibility(INVISIBLE);
                                mRLListener.onRefresh();
                            }
                        }

                    } else {
                        if (mViewDragHelper.smoothSlideViewTo(getChildAt(0), 0, 0)) {
                            mRvReachTopWhenDown = false;
                            offset_top = 0;
                            ViewCompat.postInvalidateOnAnimation(this);
                        }
                    }

//                    if (mViewDragHelper.smoothSlideViewTo(getChildAt(0), 0, 0)) {
//                        mRvReachTopWhenDown = false;
//                        ViewCompat.postInvalidateOnAnimation(this);
//                        mRLListener.onRefreshComplete();
//                    }
                } else if (mRvReachBottomWhenDown) {
                    if (mRLListener != null) {
                        mRLListener.onLoadMore();
                    }
                    if (mViewDragHelper.smoothSlideViewTo(getChildAt(0), 0, 0)) {
                        mRvReachBottomWhenDown = false;
                        ViewCompat.postInvalidateOnAnimation(this);
                        mRLListener.onLoadMoreComplete();
                    }
                }
                return true;
        }
        return true;
    }

    public void onFinishRefresh() {
        /**
         * 这里才把hanginglayout的高度给顶回去
         */
        if (mViewDragHelper.smoothSlideViewTo(getChildAt(0), 0, 0)) {
            mRvReachTopWhenDown = false;
            ViewCompat.postInvalidateOnAnimation(this);
            header_pgb.setVisibility(INVISIBLE);
            header_down_arrow.setRotation(0);
            header_down_arrow.setVisibility(VISIBLE);
            //header_tv.setText("下拉刷新");
            setOnce = false;
            setOnce1 = false;
        }

    }

    public interface RefreshAndLoadMoreListener {
        public void onLoadMore();

        public void onRefresh();

        public void onLoadMoreComplete();

        public void onRefreshComplete();
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == getChildAt(0);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            offset_top = top;
            int hanging_h = header_hanging_layout.getMeasuredHeight();
            if (offset_top > hanging_h && dy > 0 && !setOnce) {
                /**
                 * 下拉
                 */
//                header_tv.setText("释放刷新");
                header_down_arrow.setRotation(180);
                setOnce = true;
                setOnce1 = false;

            } else if (offset_top < hanging_h && dy <= 0 && !setOnce1) {
                /**
                 * 上滚
                 */
                header_down_arrow.setRotation(0);
                setOnce1 = true;
                setOnce = false;
//                header_tv.setText("下拉刷新");
            }
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            DebugLog.e("header : height:" + header.getMeasuredHeight() + "  footer height:" + footer.getMeasuredHeight() + "  top:" + top);
            if (top > header.getMeasuredHeight()) {
                top = header.getMeasuredHeight();
            }
            return top;
        }
    }
}