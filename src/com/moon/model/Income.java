package com.moon.model;

import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * User: Moon
 * Date: 2015/4/12.
 */
@Table(name = "incomes")
public class Income {

    private long id;

    private String type;

    private float money;

    private Date date;

    private String comment;


}
