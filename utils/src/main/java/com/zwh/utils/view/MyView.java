package com.zwh.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;

import java.util.Random;

public class MyView extends View implements View.OnClickListener {
    private String mStr = "开始";
    private String[] contents = new String[]{"0", "1", "2", "3", "4", "5"};
    //    public int[] colors = new int[]{Color.parseColor("#8EE5EE"), Color.parseColor("#FFD700"), Color.parseColor("#FFD39B"), Color.parseColor("#FF8247"), Color.parseColor("#FF34B3"), Color.parseColor("#F0E68C")};
    public String[] colors = new String[]{"#8EE5EE", "#FFD700", "#FFD39B", "#FF8247", "#FF34B3", "#F0E68C"};
    private int mWidth;
    private Paint mPaint;
    private Context mContext;
    private float startjs = 0f;
    private String TAG = "sss";

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mContext = context;
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        mPaint.setStyle(Paint.Style.STROKE);
        //设置边缘锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(3);
//        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2, mPaint);
        RectF rectF = new RectF(0, 0, mWidth, mWidth);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < colors.length; i++) {
            //给扇形填充颜色
//            mPaint.setColor(colors[i]);
            mPaint.setColor(Color.parseColor(colors[i]));
            int startjd = i * 60;
            //参数的意思  画扇形    开始的角度    结束的角度     是否有中心    画笔
            canvas.drawArc(rectF, startjd, 60, true, mPaint);
//            canvas.drawCircle();
        }
        for (int i = 0; i < colors.length; i++) {
            //给扇形填充颜色
//            mPaint.setColor(colors[i]);
            mPaint.setColor(Color.parseColor(colors[i]));
            int startjd = i * 60;
            //参数的意思  画扇形    开始的角度    结束的角度     是否有中心    画笔
            canvas.drawArc(rectF, startjd, 60, true, mPaint);
//            canvas.drawCircle();
        }
        for (int i = 0; i < colors.length; i++) {
            //给扇形填充颜色
//            mPaint.setColor(colors[i]);
            mPaint.setColor(Color.parseColor(colors[i]));
            int startjd = i * 60;
            //参数的意思  画扇形    开始的角度    结束的角度     是否有中心    画笔
            canvas.drawArc(rectF, startjd, 60, true, mPaint);
//            canvas.drawCircle();
        }
        //字体颜色
        mPaint.setColor(Color.BLACK);
        //字体大小
        mPaint.setTextSize(24);
        //进行循环
        for (int i = 0; i < contents.length; i++) {
            int startjd = i * 60;
            //设定文字的路径
            Path path = new Path();
            path.addArc(rectF, startjd, 60);
            canvas.drawTextOnPath(contents[i], path, 50, 50, mPaint);
        }

        // 缺角
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(0, mWidth / 2, 20, mPaint);
//        canvas.drawArc(0.0,mWidth/2,mWidth,mWidth,60);
        canvas.drawCircle(mWidth, mWidth / 2, 20, mPaint);

        mPaint.setColor(Color.RED);
        canvas.drawCircle(mWidth / 2, mWidth / 2, 50, mPaint);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(24);
        //要得到我们写的字的高和宽
        Rect rect = new Rect();
        mPaint.getTextBounds(mStr, 0, mStr.length(), rect);
        int width = rect.width();
        int height = rect.height();
        canvas.drawText(mStr, mWidth / 2 - width / 2, mWidth / 2 + height / 2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(300, 300);
        //得到测量过后的高和宽
        mWidth = getMeasuredWidth();
    }

    @Override
    public void onClick(View v) {
        //随机数
        Random random = new Random();
//        int js = random.nextInt(3240);
//        int last = random.nextInt(360) + 360;
        // 3-12
        int radio = random.nextInt(3240) + 1440;
//        int radio = 30;
//        float radio = startjs + 40;
        //旋转动画
        Log.e("TAG", "start: " + startjs);
        RotateAnimation rotateAnimation = new RotateAnimation(startjs, radio, mWidth / 2, mWidth / 2);
        //旋转时间
        rotateAnimation.setDuration(3000);
        //保留最后执行完的位置
        rotateAnimation.setFillAfter(true);
        //启动动画
        startAnimation(rotateAnimation);
//        startjs = (js + last) % 360;
        startjs = radio % 360;
        getResult();
        Log.e("TAG", "end: " + startjs);
    }

    private void getResult() {
        if (startjs <= 30) {
            Log.e(TAG, "getResult: 4");
        } else if (startjs > 30 && startjs <= 90) {
            Log.e(TAG, "getResult: 3");
        } else if (startjs > 90 && startjs <= 150) {
            Log.e(TAG, "getResult: 2");
        } else if (startjs > 150 && startjs <= 210) {
            Log.e(TAG, "getResult: 1");
        } else if (startjs > 210 && startjs <= 270) {
            Log.e(TAG, "getResult: 0");
        } else if (startjs > 270 && startjs <= 330) {
            Log.e(TAG, "getResult: 5");
        } else {
            Log.e(TAG, "getResult: 4");
        }
    }
}
