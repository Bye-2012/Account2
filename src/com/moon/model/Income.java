package com.moon.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created with IntelliJ IDEA
 * User: Moon
 * Date: 2015/4/12.
 */

/**
 * 收入表:id,type,money,date,comment
 */

@Table(name = "incomes")
public class Income {
    @Id(column = "_id")
    private long id;

    @Column(column = "type")
    private int type;

    @Column(column = "money", defaultValue = "0")
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
