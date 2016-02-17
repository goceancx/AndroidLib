package com.oceancx.androidlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
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
 * 上面的消息框的spec
 * Created by oceancx on 16/1/8.
 */
public class RangeSeekBar extends FrameLayout {

    View left_img;
    View right_img;
    ImageView line_img;
    Paint paint;
    int maxValue, minValue;
    final int _24hourSeconds = 24 * 60 * 60;
    ViewDragHelper helper;
    boolean firstLayout = true;
    int color_blue = 0xff486cdc;
    double field_min, field_max, field_len;


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
        minValue = 8 * 60 * 60;
        maxValue = 20 * 60 * 60;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() < 2) throw new IllegalArgumentException("孩子节点要超过2个");
        left_img = getChildAt(1);
        right_img = getChildAt(2);
        line_img = (ImageView) getChildAt(0);

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

        if (firstLayout) {
            firstLayout = false;
            field_min = getPaddingLeft() + left_img.getMeasuredWidth();
            field_max = getMeasuredWidth() - getPaddingRight() - right_img.getMeasuredWidth();
            field_len = field_max - field_min;
        }

        int img_left, img_right;
        img_left = (int) (minValue * 1.0f * field_len / _24hourSeconds) + getPaddingLeft();
        img_right = img_left + left_img.getMeasuredWidth();
        DebugLog.e("l:" + img_left + "r:" + img_right);
//            left_img.layout(img_left, left_img.getTop(), img_right, left_img.getBottom());
        left_img.offsetLeftAndRight(img_left - getPaddingLeft());

        img_left = (int) (maxValue * 1.0f * field_len / _24hourSeconds) + getPaddingLeft() + left_img.getMeasuredWidth();
        img_right = img_left + right_img.getMeasuredWidth();
        DebugLog.e("2 ---  l:" + img_left + "r:" + img_right);
//            right_img.layout(img_left, right_img.getTop(), img_right, right_img.getBottom());
        right_img.offsetLeftAndRight(img_left - getPaddingLeft());


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
        paint.setColor(color_blue);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(line_img.getMeasuredHeight());

        int left, right, top, bottom;
        int half_line_img_h = line_img.getMeasuredHeight() / 2;
        int half_line_img_w = line_img.getMeasuredHeight() / 2;

        left = left_img.getRight();
        top = line_img.getTop() + half_line_img_h;
        right = right_img.getLeft();
        bottom = line_img.getTop() + half_line_img_w;
        //画遮盖线
        canvas.drawLine(left, top, right, bottom, paint);

        int half_left_img_w = left_img.getMeasuredWidth() / 2;

        left = (int) (left_img.getLeft() + half_left_img_w - pxToDp(100) / 2);
        top = left_img.getTop() - left_img.getMeasuredHeight();
        minValue = (int) ((left_img.getRight() - left_img.getMeasuredWidth() - getPaddingLeft()) / field_len * _24hourSeconds);

        drawBlueRoundRectWithDownTriangleWithText(canvas, left, top, secToHHMM(minValue));


        int half_right_img_w = right_img.getMeasuredWidth() / 2;

        left = (int) (right_img.getLeft() + half_right_img_w - pxToDp(100) / 2);
        top = right_img.getTop() - right_img.getMeasuredHeight();

        maxValue = (int) ((right_img.getLeft() - left_img.getMeasuredWidth() - getPaddingLeft()) / field_len * _24hourSeconds);
        drawBlueRoundRectWithDownTriangleWithText(canvas, left, top, secToHHMM(maxValue));
    }

    private String secToHHMM(int sec) {

        int hour, min;
        hour = sec / 60 / 60;
        min = (sec - hour * 60 * 60) / 60;


        return (hour < 10 ? "0" + hour : "" + hour) + ":" +
                (min < 10 ? "0" + min : "" + min);
    }

    private void drawBlueRoundRectWithDownTriangleWithText(Canvas canvas, float x, float y, String text) {
        /**
         * 画圆角矩形 还有 下面的三角形
         * spec rect : w : 100  h : 40 radius : 8
         * spec triangle : b:10 h:5 5√2 5√2
         * number: font-size : 28px line-height : 40px
         * Canvas画图形参考: http://blog.csdn.net/rhljiayou/article/details/7212620
         */
        if (paint == null) paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color_blue);

        RectF roundRect = new RectF(x, y, x + pxToDp(100), y + pxToDp(40));// 设置个新的长方形
        canvas.drawRoundRect(roundRect, 8, 8, paint);
        Path path = new Path();
        path.moveTo(x + pxToDp(45), y + pxToDp(40));
        path.lineTo(x + pxToDp(50), y + pxToDp(45));
        path.lineTo(x + pxToDp(55), y + pxToDp(40));
        path.close();
        canvas.drawPath(path, paint);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setTextSize(pxToDp(24));
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (int) ((roundRect.bottom + roundRect.top - fontMetrics.bottom - fontMetrics.top) / 2);
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, roundRect.centerX(), baseline, paint);

    }


    private float pxToDp(int pix) {
        final float scale = getResources().getDisplayMetrics().density;
        return pix / 2f * scale;
    }


    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setRangeValue(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        firstLayout = true;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}