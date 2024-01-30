package com.example.myapplication.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressBar extends View {

    private Paint paint;
    private int progress;

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        paint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 10;

        paint.setColor(Color.GRAY);
        canvas.drawCircle(centerX, centerY, radius, paint);

        paint.setColor(Color.CYAN);
        float sweepAngle = 360 * progress / 100f;
        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius,
                -90, sweepAngle, false, paint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
