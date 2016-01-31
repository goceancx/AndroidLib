package com.oceancx.androidlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.oceancx.androidlib.DebugLog;
import com.oceancx.androidlib.R;


/**
 * Created by oceancx on 16/1/8.
 */
public class RangeSeekBar extends FrameLayout {

    View left_img;
    View right_img;
    ImageView line_img;

    Paint paint;
    TextView left_tv, right_tv;


    int dur;

    int maxValue, minValue;

    public RangeSeekBar(Context context) {
        this(context, null);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    ViewDragHelper helper;
    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == left_img || child == right_img;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {

            return getPaddingTop();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == left_img) {
                if (left < getPaddingLeft()) left = getPaddingLeft();
                if (left > getMeasuredWidth() - right_img.getMeasuredWidth() - left_img.getMeasuredWidth() - getPaddingRight())
                    left = getMeasuredWidth() - right_img.getMeasuredWidth() - left_img.getMeasuredWidth() - getPaddingLeft();

            } else if (child == right_img) {
                if (left < left_img.getMeasuredWidth() + getPaddingLeft()) {
                    left = left_img.getMeasuredWidth() + getPaddingLeft();
                }
                if (left > getMeasuredWidth() - right_img.getMeasuredWidth() - getPaddingRight()) {
                    left = getMeasuredWidth() - right_img.getMeasuredWidth() - getPaddingRight();
                }
            }

            invalidate();
            return left;
        }


        boolean hunt_sec = true;
        boolean hunt_first = true;

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            DebugLog.e("changedView :" + changedView.getLeft() + " left:" + left + " top:" + top + " dx:" + dx + " dy:" + dy);
            if (changedView == left_img) {
                if (left + left_img.getMeasuredWidth() - dx == right_img.getLeft() && dx > 0) {
                    if (hunt_sec) {
                        right_img.offsetLeftAndRight(dx);
                        return;
                    }
                }
                if (-(left + left_img.getMeasuredWidth() - right_img.getLeft()) <= helper.getTouchSlop() && dx > 0) {
                    left_img.offsetLeftAndRight(-(left + left_img.getMeasuredWidth() - right_img.getLeft()));
                    hunt_sec = true;
                } else {
                    hunt_sec = false;
                }
            } else if (changedView == right_img) {
                if (left - dx == left_img.getRight() && dx < 0) {
                    if (hunt_first) {
                        left_img.offsetLeftAndRight(dx);
                        return;
                    }
                }
                if ((left - left_img.getRight()) <= helper.getTouchSlop() && dx < 0) {
                    right_img.offsetLeftAndRight(-(left - left_img.getRight()));
                    hunt_first = true;
                } else {
                    hunt_first = false;
                }
            }

        }

    };

    private void init(Context context) {
        helper = ViewDragHelper.create(this, callback);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff486cdc);
        DebugLog.e("context.getResources().getDimension(R.dimen._5dp):" + context.getResources().getDimension(R.dimen._5dp));

        paint.setStrokeWidth(context.getResources().getDimension(R.dimen._5dp));
        setWillNotDraw(false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() < 2) throw new IllegalArgumentException("孩子节点要超过2个");
        left_img = getChildAt(1);
        right_img = getChildAt(2);
        line_img = (ImageView) getChildAt(0);
        minValue = 0;
        maxValue = 24 * 60 * 60;

        left_tv = new TextView(getContext());
        right_tv = new TextView(getContext());
//        left_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.choose_time_tv_bg));
//        right_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.choose_time_tv_bg));


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return helper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        helper.processTouchEvent(event);
        if (helper.isCapturedViewUnder((int) event.getX(), (int) event.getY())) {
            View view = findScrollView(this);
            if (view != null) {
                ((ViewGroup) view).requestDisallowInterceptTouchEvent(true);
            }
        }
        return true;
    }

    public ViewGroup findScrollView(View view) {
        if (view instanceof NestedScrollView) {
            return (ViewGroup) view;
        } else if (view == null) return null;
        else if (view.getParent() instanceof ViewGroup)
            return findScrollView((View) view.getParent());
        else return null;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        helper = null;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (lockDraw) return;

        paint.reset();
        paint.setColor(0xff486cdc);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(line_img.getMeasuredHeight());

        canvas.drawLine(left_img.getRight() , line_img.getTop() + line_img.getMeasuredHeight() / 2,
                right_img.getLeft() ,
                line_img.getTop() + line_img.getMeasuredHeight() / 2, paint);


    }


    boolean lockDraw = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lockDraw) return;

    }
}
