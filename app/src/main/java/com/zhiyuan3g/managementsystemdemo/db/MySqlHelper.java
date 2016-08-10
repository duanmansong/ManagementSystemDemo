package com.zhiyuan3g.managementsystemdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/1/4.
 */
public class MySqlHelper extends SQLiteOpenHelper {
    public MySqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table user(_id integer primary key autoincrement,"
                + "username varchar(20)," + "password varchar(20),"
                + "flag integer default 0)";
        String sql1 = "create table employee(_id integer primary key autoincrement,"
                + "name varchar(20),"
                + "sex varchar(20),"
                + "mingZu varchar(20),"
                + "id varchar(20),"
                + "department varchar(20),"
                + "birthday varchar(20),"
                + "phone varchar(20),"
                + "user varchar(20),"
                + "more varchar(20),"
                + "image blob)";
        String sql2 = "create table loginHistory(_id integer primary key autoincrement,"
                + "name varchar(20))";
        db.execSQL(sql);
        db.execSQL(sql1);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
