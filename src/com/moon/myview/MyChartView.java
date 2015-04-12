package com.moon.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: Moon
 * Date: 2015/4/10.
 */
public class MyChartView extends View {

    public MyChartView(Context context) {
        this(context, null);
    }

    public MyChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private Paint arcPaint;
    private RectF arcRect;

    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private float[] fs = new float[]{-1};

    private int widthPixels;
    private int heightPixels;

    private void init(Context context, AttributeSet attrs) {
        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);

        arcRect = new RectF(150, 100, 350, 300);

        widthPixels = getResources().getDisplayMetrics().widthPixels;
        heightPixels = getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);

        float f = useDate();
        if (f != -1) {
            arcPaint.setColor(Color.GREEN);
            canvas.drawArc(arcRect, 0, 360 * f, true, arcPaint);
            canvas.drawRect(200, 380, 220, 400, arcPaint);
            canvas.drawText("支出", 230, 390, arcPaint);

            arcPaint.setColor(Color.RED);
            canvas.drawArc(arcRect, 360 * f, 360 * (1 - f), true, arcPaint);
            canvas.drawRect(200, 420, 220, 440, arcPaint);
            canvas.drawText("收入", 230, 430, arcPaint);
        }

        if (fs[0] != -1) {

        }
    }

    private float useDate() {
        if (list != null && list.size() != 0) {
            return Float.parseFloat(list.get(0).get("out").toString()) / (Float.parseFloat(list.get(0).get("out").toString()) + Float.parseFloat(list.get(1).get("inn").toString()));
        }
        return -1;
    }

    public void setData(List<Map<String, Object>> list) {
        this.list = list;
        if (list != null && list.size() != 0) {
            postInvalidate();//刷新 -- 所有线程
        }
    }
}