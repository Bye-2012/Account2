package com.moon.Account2;

import android.app.Activity;
import android.os.Bundle;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.model.Expend;
import com.moon.model.Income;
import com.moon.myview.ChartView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    private DbUtils db;

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
        String[] types_in = getArray(R.array.types_in);
        String[] types_out = getArray(R.array.types_out);

        float[] money_in = new float[2];
        float[] money_out = new float[5];

        float all_in = 0;
        float all_out = 0;

        db = DbUtils.create(this, "account.db", 1, new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils dbUtils, int i, int i1) {
            }
        });

        WhereBuilder builder = WhereBuilder.b();
        List<Expend> es;
        List<Income> is;
        try {
            builder.expr("dates", "like", new SimpleDateFormat("yyyy-MM").format(new Date()) + "%");
            es = db.findAll(Selector.from(Expend.class).where(builder));
            for (Expend e : es) {
                float money = e.getMoney();
                all_out += money;
                switch (e.getType()) {
                    case 0:
                        money_out[0] += money;
                        break;
                    case 1:
                        money_out[1] += money;
                        break;
                    case 2:
                        money_out[2] += money;
                        break;
                    case 3:
                        money_out[3] += money;
                        break;
                    case 4:
                        money_out[4] += money;
                        break;
                }
            }

            is = db.findAll(Selector.from(Income.class).where(builder));
            for (Income income : is) {
                float incomeMoney = income.getMoney();
                all_in += incomeMoney;
                switch (income.getType()) {
                    case 0:
                        money_in[0] += incomeMoney;
                        break;
                    case 1:
                        money_in[1] += incomeMoney;
                        break;
                }
            }

            money_in = setFloat(money_in, all_in);
            money_out = setFloat(money_out, all_out);

            my_chart_out.setData(money_out, types_out);
            my_chart_in.setData(money_in,types_in);

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public String[] getArray(int id) {
        return getResources().getStringArray(id);
    }

    private float[] setFloat(float[] money, float all) {
        for (int i = 0; i < money.length; i++) {
            money[i] = money[i] / all;
        }
        return money;
    }
}