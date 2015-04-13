package com.moon.dbhelper;

import android.content.Context;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.moon.Account2.R;
import com.moon.model.Expend;
import com.moon.model.Income;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: Moon
 * Date: 2015/4/13.
 */
public class GetDbData {
    private DbUtils db;
    private float[] counts;
    private String[] types_in;
    private String[] types_out;
    private float[][] money_count;
    private List<List<String>> details;

    public GetDbData(Context context, DbUtils db){
        this(context,db,null);
    }

    public GetDbData(Context context, DbUtils db, List<List<String>> details) {
        this.db = db;
        this.details = details;
        this.counts = new float[2];
        this.money_count = new float[2][];
        this.types_in = context.getResources().getStringArray(R.array.types_in);
        this.types_out = context.getResources().getStringArray(R.array.types_out);
    }

    public List<Map<String, Object>> getListData(WhereBuilder builder,List<Map<String,Object>> list) {
        list.clear();
        money_count[0] = new float[5];
        money_count[1] = new float[2];
        float all_in = 0;
        float all_out = 0;
        List<Expend> es;
        List<Income> is;
        try {
            es = db.findAll(Selector.from(Expend.class).where(builder).orderBy("dates", true));
            if (es != null && es.size() != 0) {
                for (Expend e : es) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    float money = e.getMoney();
                    all_out += money;
                    switch (e.getType()) {
                        case 0:
                            money_count[0][0] += money;
                            break;
                        case 1:
                            money_count[0][1] += money;
                            break;
                        case 2:
                            money_count[0][2] += money;
                            break;
                        case 3:
                            money_count[0][3] += money;
                            break;
                        case 4:
                            money_count[0][4] += money;
                            break;
                    }
                    map.put("money", money);
                    int type = e.getType();
                    map.put("type", types_out[type]);
                    if (details != null) {
                        map.put("detail", details.get(type).get(e.getDetail()));
                    }
                    map.put("date", e.getDate());
                    map.put("comment", e.getComment());
                    list.add(map);
                }
            }
            is = db.findAll(Selector.from(Income.class).where(builder).orderBy("dates", true));
            if (is != null && is.size() != 0) {
                for (Income income : is) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    float money = income.getMoney();
                    all_in += money;
                    map.put("money", money);
                    switch (income.getType()) {
                        case 0:
                            money_count[1][0] += money;
                            break;
                        case 1:
                            money_count[1][1] += money;
                            break;
                    }
                    map.put("type", "收入");
                    map.put("detail", types_in[income.getType()]);
                    map.put("date", income.getDate());
                    map.put("comment", income.getComment());
                    list.add(map);
                }
            }
            counts[0] = all_out;
            counts[1] = all_in;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public float[] getCounts() {
        return counts;
    }

    public float[][] getDetailCounts(){ return money_count;}
}
