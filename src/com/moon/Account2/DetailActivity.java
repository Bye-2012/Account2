package com.moon.Account2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.*;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.adapter.MyListViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @ViewInject(R.id.spinner_chose_date)
    private Spinner spinner_detail_date;

    private MyListViewAdapter adapter_detail;
    private List<Map<String,Object>> list_detail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
        initDateAndListener();
    }

    private void initView(){
        ViewUtils.inject(this);
    }

    private void initDateAndListener(){
        list_detail = new ArrayList<Map<String, Object>>();
        adapter_detail = new MyListViewAdapter(this,list_detail);
        listView_detail.setAdapter(adapter_detail);

        ArrayAdapter<String> adapter_date = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.date_type));
        adapter_date.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_detail_date.setAdapter(adapter_date);

        spinner_detail_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}