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
    private float startAngle;
    private float sweepAngle;

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

        if (data[0] != -1) {
            startAngle = 0;
            sweepAngle = 0;
            //循环画出占有率
            for (int i = 0; i < data.length; i++) {
                sweepAngle = 360 * data[i];
                arcPaint.setColor(colors[i]);
                canvas.drawArc(arcRectF, startAngle, sweepAngle, true, arcPaint);
                startAngle += sweepAngle;
            }

            int height = 50;
            //循环画图
            for (int i = 0; i < data.length; i++) {
                arcPaint.setColor(colors[i]);
                canvas.drawRect(315, height, 350, height + 35, arcPaint);
                canvas.drawText(arrTitle[i], 365, height + 30, textPaint);
                height += 50;
            }
        }else if (data[0] == -1){
            arcPaint.setColor(Color.BLACK);
            switch(data.length){
                case 2:
                    canvas.drawText("暂无收入数据", 50, 80, textPaint);
                    break;
                case 5:
                    canvas.drawText("暂无支出数据", 50, 80, textPaint);
                    break;
            }
        }
    }

    /**
     * 设置数据
     */
    public void setData(float[] data, String[] arrTitle) {
        this.data = data;
        this.arrTitle = arrTitle;
        if (data != null && arrTitle != null) {

            Log.i("1111","date:"+data[0]+","+data[data.length-1]);

            invalidate();
        }
    }
}
