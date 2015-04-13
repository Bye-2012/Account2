package com.moon.Account2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.*;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.adapter.MyListViewAdapter;
import com.moon.model.Expend;
import com.moon.model.Income;

import java.text.SimpleDateFormat;
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
    @ViewInject(R.id.spinner_chose_date)
    private Spinner spinner_detail_date;

    private MyListViewAdapter adapter_detail;
    private List<Map<String, Object>> list_detail;

    private DbUtils db;

    private String[] types_in;
    private String[] types_out;
    private List<List<String>> details = new ArrayList<List<String>>();

    private float all_in;
    private float all_out;

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
        db = DbUtils.create(this, "account.db", 1, new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils dbUtils, int i, int i1) {
            }
        });
        list_detail = new ArrayList<Map<String, Object>>();
        adapter_detail = new MyListViewAdapter(this, list_detail);
        listView_detail.setAdapter(adapter_detail);

        ArrayAdapter<String> adapter_date = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.date_type));
        adapter_date.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_detail_date.setAdapter(adapter_date);

        spinner_detail_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WhereBuilder builder = WhereBuilder.b();
                list_detail.clear();
                all_in = 0;
                all_out = 0;
                List<Expend> es;
                List<Income> is;
                try {
                    switch (i) {
                        case 0:
                            builder.expr("dates = date('now')");
                            break;
                        case 1:
                            builder.expr("dates = date('now')");
                            break;
                        case 2:
                            builder.expr("dates", "like", new SimpleDateFormat("yyyy-MM").format(new Date()) + "%");
                            break;
                        case 3:
                            builder.expr("dates", "like", new SimpleDateFormat("yyyy").format(new Date()) + "%");
                            break;
                    }
                    es = db.findAll(Selector.from(Expend.class).where(builder));
                    if (es != null && es.size()!= 0) {
                        for (Expend e : es) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            all_out += e.getMoney();
                            map.put("money", e.getMoney());
                            int type = e.getType();
                            map.put("type", types_out[type]);
                            map.put("detail", details.get(type).get(e.getDetail()));
                            map.put("date", e.getDate());
                            map.put("comment", e.getComment());
                            list_detail.add(map);
                            map = null;
                        }
                    }
                    is = db.findAll(Selector.from(Income.class).where(builder));
                    if (is != null && is.size()!= 0) {
                        for (Income income : is) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            all_in += income.getMoney();
                            map.put("money", income.getMoney());
                            map.put("type", "收入");
                            map.put("detail", types_in[income.getType()]);
                            map.put("date", income.getDate());
                            map.put("comment", income.getComment());
                            list_detail.add(map);
                            map = null;
                        }
                    }
                    adapter_detail.notifyDataSetChanged();
                    txt_detail_in.setText(all_in+"");
                    txt_detail_out.setText(all_out+"");
                    txt_detail_balance.setText((all_in-all_out)+"");
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void getData() {
        types_in = getArray(R.array.types_in);
        types_out = getArray(R.array.types_out);
        details.add(getList(R.array.types_eat));
        details.add(getList(R.array.types_cloth));
        details.add(getList(R.array.types_home));
        details.add(getList(R.array.types_go));
        details.add(getList(R.array.types_use));
    }

    public String[] getArray(int id) {
        return getResources().getStringArray(id);
    }

    public List<String> getList(int id) {
        return Arrays.asList(getArray(id));
    }
}