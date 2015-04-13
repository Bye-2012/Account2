package com.moon.Account2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.adapter.MyListViewAdapter;
import com.moon.dbhelper.GetDbData;

import java.util.*;

/**
 * Created with IntelliJ IDEA
 * User: Moon
 * Date: 2015/4/12.
 */
public class DetailActivity extends FragmentActivity {
    @ViewInject(R.id.listView_detail)
    private ListView listView_detail;
    @ViewInject(R.id.textView_detail_in)
    private TextView txt_detail_in;
    @ViewInject(R.id.textView_detail_out)
    private TextView txt_detail_out;
    @ViewInject(R.id.textView_detail_balance)
    private TextView txt_detail_balance;

    private MyListViewAdapter adapter_detail;
    private List<Map<String, Object>> list_detail;
    private List<List<String>> details = new ArrayList<List<String>>();

    private WhereBuilder builder;

    private Calendar calendar = Calendar.getInstance();

    private GetDbData dbHelper;

    private String date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();

        getData();

        initDateAndListener();
    }

    private void initView() {
        ViewUtils.inject(this);
    }

    private void initDateAndListener() {
        DbUtils db = DbUtils.create(this, "account.db", 1, new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils dbUtils, int i, int i1) {
            }
        });
        list_detail = new ArrayList<Map<String, Object>>();
        adapter_detail = new MyListViewAdapter(this, list_detail);
        listView_detail.setAdapter(adapter_detail);

        dbHelper = new GetDbData(this,db,details);
    }

    public void clickButton(View view) {
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dDialog;
        switch (view.getId()) {
            /**
             * 按日查找
             */
            case R.id.btn_dtl_day:
                dDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date = year + "-" + WriteActivity.getMonth(monthOfYear) + "-" + WriteActivity.getDay(dayOfMonth);
                                builder = WhereBuilder.b();
                                builder.expr("dates", "=", date);
                                setListData(builder);
                            }
                        }, year, monthOfYear, dayOfMonth);
                dDialog.show();
                break;
            /**
             * 按月查找
             */
            case R.id.btn_dtl_mon:
                dDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date = year + "-" + WriteActivity.getMonth(monthOfYear);
                                builder = WhereBuilder.b();
                                builder.expr("dates", "like", date + "%");
                                setListData(builder);
                            }
                        }, year, monthOfYear, dayOfMonth);
                dDialog.show();
                break;
            /**
             * 按年查找
             */
            case R.id.btn_dtl_year:
                dDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date = year+"";
                                builder = WhereBuilder.b();
                                builder.expr("dates", "like", date + "%");
                                setListData(builder);
                            }
                        }, year, monthOfYear, dayOfMonth);
                dDialog.show();
                break;
            /**
             * 按类型查找
             */
            case R.id.btn_dtl_type:
                break;
        }
    }

    /**
     * 设置ListView数据
     * @param b 查询条件
     */
    private void setListData(WhereBuilder b){
        list_detail = dbHelper.getListData(b,list_detail);
        Log.i("1111--3",date+"..."+list_detail.size()+"");
        adapter_detail.notifyDataSetChanged();
        setCounts();
    }

    /**
     * 设置统计金额
     */
    private void setCounts(){
        float[] counts = dbHelper.getCounts();
        txt_detail_out.setText(counts[0]+"");
        txt_detail_in.setText(counts[1]+"");
        txt_detail_balance.setText((counts[0]-counts[1])+"");
    }

    /**
     * 获得类型
     */
    private void getData() {
        details.add(getList(R.array.types_eat));
        details.add(getList(R.array.types_cloth));
        details.add(getList(R.array.types_home));
        details.add(getList(R.array.types_go));
        details.add(getList(R.array.types_use));
    }

    private String[] getArray(int id) {
        return getResources().getStringArray(id);
    }

    private List<String> getList(int id) {
        return Arrays.asList(getArray(id));
    }
}
