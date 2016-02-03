package com.oceancx.androidlib.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.oceancx.androidlib.DebugLog;


/**
 * Created by oceancx on 16/1/8.
 */
public class RangeSeekBar extends FrameLayout {

    View left_img;
    View right_img;
    ImageView line_img;
    Paint paint;
    int maxValue, minValue;
    ViewDragHelper helper;
    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        boolean hit_sec = true;
        boolean hit_first = true;

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
                    left = getMeasuredWidth() - right_img.getMeasuredWidth() - left_img.getMeasuredWidth() - getPaddingRight();
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

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            DebugLog.e("changedView :" + changedView.getLeft() + " left:" + left + " top:" + top + " dx:" + dx + " dy:" + dy);
            if (changedView == left_img) {
                //left_img右滑撞到right_img
                if (left + left_img.getMeasuredWidth() - dx == right_img.getLeft() && dx > 0) {
                    if (hit_sec) {
                        right_img.offsetLeftAndRight(dx);
                        return;
                    }
                }
                if (-(left + left_img.getMeasuredWidth() - right_img.getLeft()) <= helper.getTouchSlop() && dx > 0) {
                    left_img.offsetLeftAndRight(-(left + left_img.getMeasuredWidth() - right_img.getLeft()));
                    hit_sec = true;
                } else {
                    hit_sec = false;
                }
            } else if (changedView == right_img) {
                //right_img左滑撞到左边img
                if (left - dx == left_img.getRight() && dx < 0) {
                    if (hit_first) {
                        left_img.offsetLeftAndRight(dx);
                        return;
                    }
                }
                if ((left - left_img.getRight()) <= helper.getTouchSlop() && dx < 0) {
                    right_img.offsetLeftAndRight(-(left - left_img.getRight()));
                    hit_first = true;
                } else {
                    hit_first = false;
                }
            }

        }

    };
    boolean lockDraw = false;

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

    private void init(Context context) {
        helper = ViewDragHelper.create(this, callback);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
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

    /**
     * 防止引起父scrollView垂直方向抖动
     *
     * @param view
     * @return
     */
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (lockDraw) return;

        paint.reset();
        paint.setColor(0xff486cdc);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(line_img.getMeasuredHeight());

        int left, right, top, bottom;
        left = left_img.getRight();
        top = line_img.getTop() + line_img.getMeasuredHeight() / 2;
        right = right_img.getLeft();
        bottom = line_img.getTop() + line_img.getMeasuredHeight() / 2;
        //画遮盖线
        canvas.drawLine(left, top, right, bottom, paint);

        DebugLog.e(left_img.getLeft() + "  left:" + left_img.getMeasuredWidth() / 2);
        left = left_img.getLeft() + left_img.getMeasuredWidth() / 2 - 50;
        top = left_img.getTop() - left_img.getMeasuredHeight();
        DebugLog.e("left:" + left + " top:" + top);


        paint.reset();
        paint.setColor(0xff486cdc);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, left + 100, top + 50, paint);

        paint.reset();
        paint.setColor(0xffffffff);
        paint.setTextSize(24);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawText(left_img.getLeft() % 100 + ":" + left_img.getLeft() % 100, left + 15, top + 30, paint);


        left = right_img.getLeft() + right_img.getMeasuredWidth() / 2 - 50;
        top = right_img.getTop() - right_img.getMeasuredHeight();

        paint.reset();
        paint.setColor(0xff486cdc);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, left + 100, top + 50, paint);

        paint.reset();
        paint.setColor(0xffffffff);
        paint.setTextSize(24);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawText(right_img.getLeft() % 100 + ":" + right_img.getLeft() % 100, left + 15, top + 30, paint);

    }


}
