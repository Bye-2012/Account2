package com.moon.model;

/**
 * Created with IntelliJ IDEA
 * User: Moon
 * Date: 2015/4/12.
 */

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 支出表：id,type,detail,money,date,comment
 */
@Table(name = "expends")
public class Expend {

    @Id(column = "_id")
    private long id;

    @Column(column = "type")
    private int type;

    @Column(column = "detail")
    private int detail;

    @Column(column = "money")
    private float money;

    @Column(column = "dates")
    private String dates;

    @Column(column = "comment")
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return dates;
    }

    public void setDate(String dates) {
        this.dates = dates;
    }

    public int getDetail() {
        return detail;
    }

    public void setDetail(int detail) {
        this.detail = detail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
