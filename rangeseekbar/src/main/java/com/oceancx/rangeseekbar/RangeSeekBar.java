package com.oceancx.rangeseekbar;

import android.content.Context;
import android.content.res.TypedArray;
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


/**
 * 上面的消息框的spec
 * 带改进成支持多指触控的.
 * Created by oceancx on 16/1/8.
 */
public class RangeSeekBar extends FrameLayout {

    public static final int MODE_TIME = 0;
    public static final int MODE_MAX_MIN_VALUE = 1;
    View left_img;
    View right_img;
    ImageView line_img;
    Paint paint;
    int maxValue, minValue;
    int initMinValue, initMaxValue;
    ViewDragHelper helper;
    boolean firstLayout = true;
    int color_red = 0xffFF4081;
    double field_min, field_max, field_len;
    int mode = MODE_TIME;


    int lastDx = 0;

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        boolean hit_sec = true;
        boolean hit_first = true;


        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (mode == MODE_TIME) {
                /**
                 * 将minValue和maxValue规定到半小时的刻度
                 */
                int half_h = 60 * 30;
                initMinValue = initMinValue / half_h * half_h;
                initMaxValue = initMaxValue / half_h * half_h;
                firstLayout = true;

                requestLayout();
                invalidate();
            }
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

                initMinValue = (int) ((left_img.getRight() - field_min) * (maxValue - minValue) / field_len) + minValue;
                if (hit_sec && dx > 0)
                    initMaxValue = (int) ((right_img.getLeft() - field_min) * (maxValue - minValue) / field_len) + minValue;

            } else if (child == right_img) {
                if (left < left_img.getMeasuredWidth() + getPaddingLeft()) {
                    left = left_img.getMeasuredWidth() + getPaddingLeft();
                }
                if (left > getMeasuredWidth() - right_img.getMeasuredWidth() - getPaddingRight()) {
                    left = getMeasuredWidth() - right_img.getMeasuredWidth() - getPaddingRight();
                }
                if (hit_first && dx < 0)
                    initMinValue = (int) ((left_img.getRight() - field_min) * (maxValue - minValue) / field_len) + minValue;
                initMaxValue = (int) ((right_img.getLeft() - field_min) * (maxValue - minValue) / field_len) + minValue;

            }


            invalidate();
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
//            DebugLog.e("changedView :" + changedView.getLeft() + " left:" + left + " top:" + top + " dx:" + dx + " dy:" + dy);
            if (changedView == left_img) {
                //left_img右滑撞到right_img
                if (left + left_img.getMeasuredWidth() - dx == right_img.getLeft() && dx > 0) {
                    if (hit_sec) {
                        right_img.offsetLeftAndRight(dx);
                        return;
                    }
                }
                if (-(left + left_img.getMeasuredWidth() - right_img.getLeft()) <= helper.getTouchSlop() / 3 && dx > 0) {
                    left_img.offsetLeftAndRight(-(left + left_img.getMeasuredWidth() - right_img.getLeft()));
                    initMinValue = (int) ((left_img.getRight() - field_min) * (maxValue - minValue) / field_len) + minValue;
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
                if ((left - left_img.getRight()) <= helper.getTouchSlop() / 3 && dx < 0) {
                    right_img.offsetLeftAndRight(-(left - left_img.getRight()));
                    initMaxValue = (int) ((right_img.getLeft() - field_min) * (maxValue - minValue) / field_len) + minValue;
                    hit_first = true;
                } else {
                    hit_first = false;
                }

            }

        }

    };

    int rect_w = 42, rect_h = 20, rect_radius = 8;
    int triangle_b = 8, triangle_h = 4;
    int font_size = 14;


    public RangeSeekBar(Context context) {
        this(context, null);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public RangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.RangeSeekBar, defStyleAttr, defStyleAttr);
        mode = a.getInteger(R.styleable.RangeSeekBar_range_mode, -1);
        if (mode == -1) mode = MODE_TIME;
        if (mode == MODE_TIME) {
            minValue = 0;
            maxValue = 60 * 60 * 24;
        } else {
            minValue = a.getInteger(R.styleable.RangeSeekBar_min_value, -1);
            if (minValue == -1) minValue = 0;

            maxValue = a.getInteger(R.styleable.RangeSeekBar_max_value, -1);
            if (maxValue == -1) maxValue = Integer.MAX_VALUE;
        }
        initMinValue = a.getInteger(R.styleable.RangeSeekBar_init_min_value, minValue);
        initMaxValue = a.getInteger(R.styleable.RangeSeekBar_init_max_value, maxValue);

        a.recycle();


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

    int last_dx = 0;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (firstLayout) {
            firstLayout = false;
            field_min = getPaddingLeft() + left_img.getMeasuredWidth();
            field_max = getMeasuredWidth() - getPaddingRight() - right_img.getMeasuredWidth();
            field_len = field_max - field_min;
        }
        last_dx = (int) ((initMinValue - minValue) * field_len * 1.0f / (maxValue - minValue));
        left_img.offsetLeftAndRight(last_dx);

        last_dx = (int) ((initMaxValue - minValue) * field_len * 1.0f / (maxValue - minValue));
        right_img.offsetLeftAndRight(last_dx + left_img.getMeasuredWidth());


    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        helper = null;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        paint.reset();
        //红色
        paint.setColor(0xffFF4081);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(line_img.getMeasuredHeight());

        int left, right, top, bottom;
        int half_line_img_h = line_img.getMeasuredHeight() / 2;

        left = left_img.getRight();
        top = line_img.getTop() + half_line_img_h;
        right = right_img.getLeft();
        bottom = line_img.getTop() + half_line_img_h;
        //画遮盖线
        canvas.drawLine(left, top, right, bottom, paint);

        int half_left_img_w = left_img.getMeasuredWidth() / 2;
        left = (int) (left_img.getLeft() + half_left_img_w - pxToDp(rect_w) / 2);
        top = (int) (left_img.getTop() - pxToDp(rect_h + triangle_h));
        drawBlueRoundRectWithDownTriangleWithText(canvas, left, top, initMinValue);


        int half_right_img_w = right_img.getMeasuredWidth() / 2;
        left = (int) (right_img.getLeft() + half_right_img_w - pxToDp(rect_w) / 2);
        top = (int) (right_img.getBottom() + pxToDp(triangle_h));
        drawBlueRoundRectWithUpTriangleWithText(canvas, left, top, initMaxValue);
    }


    private String secToHHMM(int sec) {

        int hour, min;
        hour = sec / (60 * 60);
        min = (sec - hour * 60 * 60) / 60;

        return (hour < 10 ? "0" + hour : "" + hour) + ":" +
                (min < 10 ? "0" + min : "" + min);
    }

    private void drawBlueRoundRectWithDownTriangleWithText(Canvas canvas, float x, float y, int value) {
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
        paint.setColor(color_red);
//        canvas.clipRect(x, y, x + pxToDp(rect_w), y + pxToDp(rect_h + triangle_h));
        RectF roundRect = new RectF(x, y, x + pxToDp(rect_w), y + pxToDp(rect_h));// 设置个新的长方形
        canvas.drawRoundRect(roundRect, rect_radius, rect_radius, paint);
        Path path = new Path();
        path.moveTo(x + pxToDp(rect_w / 2 - triangle_b / 2), y + pxToDp(rect_h));
        path.lineTo(x + pxToDp(rect_w / 2), y + pxToDp(rect_h + triangle_h));
        path.lineTo(x + pxToDp(rect_w / 2 + triangle_b / 2), y + pxToDp(rect_h));
        path.close();
        canvas.drawPath(path, paint);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setTextSize(pxToDp(font_size));
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (int) ((roundRect.bottom + roundRect.top - fontMetrics.bottom - fontMetrics.top) / 2);
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        if (mode == MODE_TIME)
            canvas.drawText(secToHHMM(value), roundRect.centerX(), baseline, paint);
        else if (MODE_MAX_MIN_VALUE == mode)
            canvas.drawText(String.valueOf(value), roundRect.centerX(), baseline, paint);

    }

    private void drawBlueRoundRectWithUpTriangleWithText(Canvas canvas, int x, int y, int value) {
        if (paint == null) paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color_red);

//        canvas.clipRect(x, y - triangle_h, x + pxToDp(rect_w), y + pxToDp(rect_h));
        RectF roundRect = new RectF(x, y, x + pxToDp(rect_w), y + pxToDp(rect_h));// 设置个新的长方形
        canvas.drawRoundRect(roundRect, rect_radius, rect_radius, paint);

        Path path = new Path();
        path.moveTo(x + pxToDp(rect_w / 2 - triangle_b / 2), y);
        path.lineTo(x + pxToDp(rect_w / 2), y - pxToDp(triangle_h));
        path.lineTo(x + pxToDp(rect_w / 2 + triangle_b / 2), y);
        path.close();
        canvas.drawPath(path, paint);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setTextSize(pxToDp(font_size));
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (int) ((roundRect.bottom + roundRect.top - fontMetrics.bottom - fontMetrics.top) / 2);
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        if (mode == MODE_TIME)
            canvas.drawText(secToHHMM(value), roundRect.centerX(), baseline, paint);
        else if (MODE_MAX_MIN_VALUE == mode)
            canvas.drawText(String.valueOf(value), roundRect.centerX(), baseline, paint);
    }


    private float pxToDp(int pix) {
        final float scale = getResources().getDisplayMetrics().density;
        return pix * scale;
    }


    public void setMode(int mode) {
        if (mode > 1) return;
        this.mode = mode;
        firstLayout = true;
        requestLayout();
    }

    public void setSeekRange(int mode, int min, int max) {
        setSeekRange(mode, min, max, min, max);
    }

    public void setSeekRange(int mode, int min, int max, int initMin, int initMax) {
        if (mode > 1) return;
        this.mode = mode;
        this.minValue = min;
        this.maxValue = max;
        this.initMinValue = initMin;
        this.initMaxValue = initMax;
        firstLayout = true;
        //这句话 位置比较关键
        invalidate();
        requestLayout();
    }

    public int getInitMinValue() {
        return initMinValue;
    }

    public int getInitMaxValue() {
        return initMaxValue;
    }
}