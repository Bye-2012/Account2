package com.moon.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by IDEA
 * user:witt
 * date:15-4-9
 */

public class ChartView extends View {

    private Paint arcPaint;
    private Paint textPaint;
    private RectF arcRectF;

    private float[] data;
    private String[] arrTitle;
    private int[] colors;

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * 初始化方法
     */
    private void init(Context context, AttributeSet attrs) {

        arcPaint = new Paint();
        arcPaint.setStyle(Paint.Style.FILL);
        arcPaint.setAntiAlias(true);
        arcRectF = new RectF(10, 30, 250, 270);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);

        data = new float[]{50, 40, 8, 1, 1};
        arrTitle = new String[]{"Android", "IOS", "windows phone", "blackberry", "other"};
        colors = new int[]{Color.GREEN, Color.RED, Color.YELLOW, Color.BLACK, Color.BLUE};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //TODO 绘制饼状图
        //清空内容

        canvas.drawColor(Color.WHITE);

        float startAngle = 0;
        float sweepAngle = 0;
        //循环画出占有率
        for (int i = 0; i < data.length; i++) {
            sweepAngle = 360 * data[i];
            arcPaint.setColor(colors[i]);
            canvas.drawArc(arcRectF, startAngle, sweepAngle, true, arcPaint);
            startAngle += sweepAngle;
        }

        int height = 10;
        //循环画图
        for (int i = 0; i < data.length; i++) {
            height += 50;
            arcPaint.setColor(colors[i]);
            canvas.drawRect(300, height, 350, height + 40, arcPaint);
            canvas.drawText(arrTitle[i], 360, height + 35, textPaint);
        }
    }

    /**
     * 设置数据
     */
    public void setData(float[] data, String[] arrTitle) {
        this.data = data;
        this.arrTitle = arrTitle;

        for (int i = 0; i < data.length; i++) {
            Log.i("---", data[i] + "");
            Log.i("---", arrTitle[i]);
        }

        invalidate();
    }
}
