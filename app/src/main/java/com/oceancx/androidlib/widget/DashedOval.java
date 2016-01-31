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
 * Created by oceancx on 15/12/25.
 */
public class DashedOval extends View {
    Paint paint;
    Path path;
    DashPathEffect pathEffect;

    public DashedOval(Context context) {
        super(context);
    }

    public DashedOval(Context context, AttributeSet attrs) {
        super(context, attrs);

        pathEffect = new DashPathEffect(new float[]{context.getResources().getDimensionPixelSize(R.dimen._2dp),
                context.getResources().getDimensionPixelSize(R.dimen._5dp)}, 0);
        paint = new Paint();
        path = new Path();

    }

    public DashedOval(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        canvas.drawPath(path, paint);
    }
}
