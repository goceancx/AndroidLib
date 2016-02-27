package com.oceancx.androidlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.oceancx.androidlib.R;


/**
 * 虚线自定义视图
 * Created by oceancx on 15/12/25.
 */
public class DashedLine extends View {
    Paint paint;
    Path path;
    DashPathEffect pathEffect;

    public DashedLine(Context context) {
        this(context, null);
    }

    public DashedLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);


    }

    public DashedLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        pathEffect = new DashPathEffect(new float[]{context.getResources().getDimensionPixelSize(R.dimen._2dp),
                context.getResources().getDimensionPixelSize(R.dimen._5dp)}, 0);
        paint = new Paint();
        path = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getMeasuredHeight());
        paint.setColor(Color.rgb(0xbf, 0xc4, 0xd3));
        paint.setAntiAlias(true);
        paint.setPathEffect(pathEffect);
        path.moveTo(0, 0);
        path.lineTo(getMeasuredWidth(), 0);
        canvas.drawPath(path, paint);
    }
}
