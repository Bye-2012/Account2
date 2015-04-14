package com.moon.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created with IntelliJ IDEA
 * User: Moon
 * Date: 2015/4/12.
 */

public class ChartView extends View {

    private Paint arcPaint;
    private Paint textPaint;
    private RectF arcRectF;

    private float[] data;
    private String[] arrTitle;
    private int[] colors;

    private int flag = 0;

    private int width;
    private int height;

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
        /**
         * 初始化图形画笔
         */
        arcPaint = new Paint();
        arcPaint.setStyle(Paint.Style.FILL);
        arcPaint.setAntiAlias(true);
        arcRectF = new RectF(30, 50, 360, 380);

        /**
         * 初始化文字画笔
         */
        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(25);

        /**
         * 初始化文字数据和颜色
         */
        data = new float[]{-1};
        arrTitle = new String[]{"1"};
        colors = new int[]{Color.GREEN, Color.RED, Color.YELLOW, Color.BLACK, Color.BLUE};

        /**
         * 计算显示位置
         */
        width = getResources().getDisplayMetrics().widthPixels / 2 - 150;
        height = getResources().getDisplayMetrics().heightPixels / 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //TODO 绘制饼状图
        //清空内容
        canvas.drawColor(Color.WHITE);

        if (flag == 1) {
            float startAngle = 0;
            float sweepAngle;
            //循环画出占有率
            for (int i = 0; i < data.length; i++) {
                sweepAngle = 360 * data[i];
                arcPaint.setColor(colors[i]);
                canvas.drawArc(arcRectF, startAngle, sweepAngle, true, arcPaint);
                startAngle += sweepAngle;
            }

            textPaint.setTextSize(25);

            int height = 0;
            if (data.length == 5) {
                height = 110;
            } else if (data.length == 2) {
                height = 185;
            }
            //循环画图
            for (int i = 0; i < data.length; i++) {
                arcPaint.setColor(colors[i]);
                canvas.drawRect(405, height, 425, height + 20, arcPaint);
                canvas.drawText(arrTitle[i] + " (" + (data[i] * 100) + "%)", 445, height + 20, textPaint);
                height += 40;
            }
            flag = 0;
        } else if (flag == 0) {
            textPaint.setTextSize(50);
            switch (data.length) {
                case 2:
                    canvas.drawText("暂无收入数据", width, height, textPaint);
                    break;
                case 5:
                    canvas.drawText("暂无支出数据", width, height, textPaint);
                    break;
            }
            flag = 0;
        }
    }

    /**
     * 设置数据
     */
    public void setData(float[] data, String[] arrTitle) {
        this.data = data;
        this.arrTitle = arrTitle;
        if (data != null && arrTitle != null) {
            float f = 0f;
            for (int i = 0; i < data.length; i++) {
                f += data[i];
            }
            if (f == 1f) {
                flag = 1;
            }
            invalidate();
        }
    }
}