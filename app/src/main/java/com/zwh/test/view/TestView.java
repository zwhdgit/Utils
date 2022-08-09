package com.zwh.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.zwh.test.R;

public class TestView extends View {

    private Context context;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        context = getContext();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(context.getColor(R.color.white));



//        draCircle(canvas);
//        draText(canvas);
//        draRfc(canvas);
//        draPotions(canvas);
//        draArc(canvas);
        draPath(canvas);
    }

    private void draPath(Canvas canvas) {
        Path path = new Path();
        path.addCircle(300, 300, 200, Path.Direction.CCW);
        canvas.drawPath(path,paint);

    }

    private void draArc(Canvas canvas) {
//        paint.setStyle(Paint.Style.FILL); // 填充模式
//        canvas.drawArc(200, 100, 800, 500, -110, 100, true, paint); // 绘制扇形
//        canvas.drawArc(200, 100, 800, 500, 20, 140, false, paint); // 绘制弧形
        paint.setStyle(Paint.Style.STROKE); // 画线模式
//        canvas.drawArc(200, 100, 800, 500, 180, 60, false, paint); // 绘制不封口的弧形
        canvas.drawArc(100, 100, 200, 200, 300, 30, false, paint); // 绘制不封口的弧形
    }

    private void draPotions(Canvas canvas) {
        paint.setColor(Color.RED);
        // 画点必须设置大小
        paint.setStrokeWidth(20);
        float[] points = {0, 0, 50, 50, 50, 100, 100, 50, 100, 100, 150, 50, 150, 100};
// 绘制四个点：(50, 50) (50, 100) (100, 50) (100, 100)
        canvas.drawPoints(points, 2 /* 跳过两个数，即前两个 0 */,
                8 /* 一共绘制 8 个数（4 个点）*/, paint);
//        canvas.drawPoint(100, 100, paint);
    }

    private void draRfc(Canvas canvas) {
        canvas.drawRect(10, 100, 200, 110, paint);
    }

    private void draCircle(Canvas canvas) {
        // 画圆
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.FILL_AND_STROKE);
//        canvas.drawCircle(140, 140, 100, paint);
    }

    private void draText(Canvas canvas) {
        String s = "DRAW_TEXT";
        //测量文本高
        Rect textBounds = new Rect();
        paint.getTextBounds(s, 0, s.length(), textBounds);
        int textH = textBounds.height();

        paint.setTextSize(100);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawText(s, 0, 100 + textH * 0, paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(s, 0, 100 + textH * 1 + 50, paint);
//        paint.setStyle(Paint.Style.FILL_AND_STROKE);
//        canvas.drawText(s, 0, 100 + textH * 2 + 100, paint);
    }
}
