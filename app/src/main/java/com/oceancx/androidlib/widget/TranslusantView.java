package com.oceancx.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * 用来做遮罩,看似简单,却很实用
 * Created by oceancx on 15/12/11.
 */
public class TranslusantView extends FrameLayout {

    public TranslusantView(Context context) {
        super(context);
    }

    public TranslusantView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TranslusantView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
