package com.moon.Account2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.model.Expend;
import com.moon.model.Income;
import com.moon.zxing.activity.CaptureActivity;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA
 * User: Moon
 * Date: 2015/4/12.
 */
public class WriteActivity extends Activity {
    @ViewInject(R.id.radioGroup)
    private RadioGroup rg;
    @ViewInject(R.id.spinner_type)
    private Spinner spinner_type;
    @ViewInject(R.id.spinner_detail)
    private Spinner spinner_detail;
    @ViewInject(R.id.textView_date)
    private TextView textView_date;
    @ViewInject(R.id.editText_money)
    private EditText editText_money;
    @ViewInject(R.id.editText_comment)
    private EditText editText_comment;
    @ViewInject(R.id.layout_details)
    private LinearLayout layout_details;

    private String[] types_in;
    private String[] types_out;
    private List<List<String>> details;
    private ArrayAdapter<String> adapter_type;
    private ArrayAdapter<String> adapter_details;

    private int flag = 1;
    private int type;
    private int detail;
    private String date;

    private Calendar calendar = Calendar.getInstance();

    private DbUtils db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        initView();
        initDate();
        initListeners();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        ViewUtils.inject(this);
    }

    /**
     * 初始化显示数据
     */
    private void initDate() {
        db = DbUtils.create(this, "account.db", 1, new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils dbUtils, int i, int i1) {
            }
        });
        details = new ArrayList<List<String>>();
        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        textView_date.setText(date);
        getData();
        setType();
        setDetail(0);
    }

    /**
     * 设置监听
     */
    private void initListeners() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioBtn_out:
                        flag = 1;
                        setType();
                        setDetail(0);
                        emptyText();
                        layout_details.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioBtn_in:
                        flag = 2;
                        setType();
                        emptyText();
                        layout_details.setVisibility(View.GONE);
                        break;
                }
            }
        });

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setDetail(i);
                type = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                detail = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * 设置Spinner_type
     */
    private void setType() {
        if (flag == 1) {
            setTypeAdapter(types_out);
        } else if (flag == 2) {
            setTypeAdapter(types_in);
        }
    }

    /**
     * 设置Spinner_detail
     *
     * @param i
     */
    private void setDetail(int i) {
        setDetailAdapter(i);
    }

    /**
     * 获得xml文件数据
     */
    public void getData() {
        types_in = getArray(R.array.types_in);
        types_out = getArray(R.array.types_out);
        details.add(getList(R.array.types_eat));
        details.add(getList(R.array.types_cloth));
        details.add(getList(R.array.types_home));
        details.add(getList(R.array.types_go));
        details.add(getList(R.array.types_use));
    }

    /**
     * 获得Array数据
     * @param id
     * @return
     */
    public String[] getArray(int id) {
        return getResources().getStringArray(id);
    }

    /**
     * 获得List数据
     * @param id
     * @return
     */
    public List<String> getList(int id) {
        return Arrays.asList(getArray(id));
    }

    /**
     * 设置type适配器
     *
     * @param type
     */
    private void setTypeAdapter(String[] type) {
        adapter_type = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(adapter_type);
    }

    /**
     * 设置detail适配器
     *
     * @param i
     */
    private void setDetailAdapter(int i) {
        adapter_details = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, details.get(i));
        adapter_details.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_detail.setAdapter(adapter_details);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    public void clickButton(View v) {
        switch (v.getId()) {
            case R.id.btn_chose_date:
                int year = calendar.get(Calendar.YEAR);
                int monthOfYear = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date = year + "-" + getMonth(monthOfYear) + "-" + getDay(dayOfMonth);
                                textView_date.setText(date);
                            }
                        }, year, monthOfYear, dayOfMonth);
                dDialog.show();
                break;
            case R.id.btn_save:
                switch(flag){
                    case 1:
                        Expend expend = new Expend();
                        expend.setType(type);
                        expend.setDetail(detail);
                        expend.setMoney(Integer.parseInt(editText_money.getText().toString()));
                        expend.setComment(editText_comment.getText().toString());
                        expend.setDate(date);
                        save(expend);
                        break;
                    case 2:
                        Income income = new Income();
                        income.setType(type);
                        income.setMoney(Integer.parseInt(editText_money.getText().toString()));
                        income.setComment(editText_comment.getText().toString());
                        income.setDate(date);
                        save(income);
                        break;
                }
                break;
            case R.id.btn_scan:
                Intent openCameraIntent = new Intent(this,CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            editText_comment.setText(scanResult);
        }
    }

    /**
     * 获得月份字符串
     *
     * @param monthOfYear
     * @return
     */
    private String getMonth(int monthOfYear) {
        String m;
        if ((monthOfYear + 1) / 10 == 0) {
            m = "0" + (monthOfYear + 1);
        } else {
            m = "" + (monthOfYear + 1);
        }
        return m;
    }

    /**
     * 获得"天"的字符串
     *
     * @param dayOfMonth
     * @return
     */
    private String getDay(int dayOfMonth) {
        String d;
        if (dayOfMonth / 10 == 0) {
            d = "0" + dayOfMonth;
        } else {
            d = "" + dayOfMonth;
        }
        return d;
    }

    /**
     * 清空输入框
     */
    private void emptyText() {
        editText_money.setText("");
        editText_comment.setText("");
    }

    /**
     * 保存到数据库
     * @param obj
     */
    private void save(Object obj){
        try {
            db.save(obj);
            Toast.makeText(this,"ok",Toast.LENGTH_SHORT).show();
        } catch (DbException e) {
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        }
    }
}