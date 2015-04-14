package com.moon.Account2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.dbhelper.GetDbData;
import com.moon.myview.ChartView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: Moon
 * Date: 2015/4/12.
 */
public class ReportActivity extends Activity {
    @ViewInject(R.id.my_chart_in)
    private ChartView my_chart_in;

    @ViewInject(R.id.my_chart_out)
    private ChartView my_chart_out;

    @ViewInject(R.id.txt_rep_title)
    private TextView txt_rep_title;

    private GetDbData dbHelper;

    private WhereBuilder builder;

    private String date;
    private String[] types_in;
    private String[] types_out;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        initView();
        initData();
    }

    private void initView() {
        ViewUtils.inject(this);
    }

    private void initData() {
        DbUtils db = DbUtils.create(this, "account.db", 1, new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils dbUtils, int i, int i1) {
            }
        });

        dbHelper = new GetDbData(this, db);

        types_in = getArray(R.array.types_in);
        types_out = getArray(R.array.types_out);

    }

    /**
     * Button点击事件
     *
     * @param view 控件
     */
    public void clickButton(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dDialog;
        switch (view.getId()) {
            case R.id.btn_rep_day:
                dDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date = year + "-" + WriteActivity.getMonth(monthOfYear) + "-" + WriteActivity.getDay(dayOfMonth);
                                txt_rep_title.setText(date + " 数据");
                                builder = WhereBuilder.b();
                                builder.expr("dates", "=", date);
                                getData(builder);
                            }
                        }, year, monthOfYear, dayOfMonth);
                dDialog.show();
                break;
            case R.id.btn_rep_mon:
                dDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date = year + "-" + WriteActivity.getMonth(monthOfYear);
                                txt_rep_title.setText(date + " 数据");
                                builder = WhereBuilder.b();
                                builder.expr("dates", "like", date + "%");
                                getData(builder);
                            }
                        }, year, monthOfYear, dayOfMonth);
                dDialog.show();
                break;
            case R.id.btn_rep_year:
                dDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date = year + "";
                                txt_rep_title.setText(date + " 数据");
                                builder = WhereBuilder.b();
                                builder.expr("dates", "like", date + "%");
                                getData(builder);
                            }
                        }, year, monthOfYear, dayOfMonth);
                dDialog.show();
                break;
        }
    }

    /**
     * 获得要显示的数据
     *
     * @param b 查询条件
     */
    private void getData(WhereBuilder b) {
        dbHelper.getListData(b, new ArrayList<Map<String, Object>>());
        float[] counts = dbHelper.getCounts();
        float[][] detailCounts = dbHelper.getDetailCounts();
        my_chart_out.setData(setFloat(detailCounts[0], counts[0]), types_out);
        my_chart_in.setData(setFloat(detailCounts[1], counts[1]), types_in);
    }

    /**
     * 获得数组
     *
     * @param id 数组ID
     * @return String[]
     */
    public String[] getArray(int id) {
        return getResources().getStringArray(id);
    }

    /**
     * 组织百分比数据
     *
     * @param money 小类型总额
     * @param all   大类型总额
     * @return 百分比数据
     */
    private float[] setFloat(float[] money, float all) {
        for (int i = 0; i < money.length - 1; i++) {
            money[i] = money[i] / all;
        }
        return money;
    }
}