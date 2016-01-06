package com.example.administrator.coolweathe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.coolweathe.util.LogUtil;

/**
 * Created by Administrator on 2016/1/5.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    private static  final  String CREATE_PROVINCE ="create table Province("
            + "id integer primary key AUTOINCREMENT,"
            + "province_name text,"
            + "province_code text)";

    private static  final  String CREATE_CITY ="create table City(" +
            "id integer primary key AUTOINCREMENT,"
            +"city_name text,"
            +"city_code text,"
            +"province_code,integer)";
    private static  final  String CREATE_COUNTY ="create table County(" +
            "id integer primary key AUTOINCREMENT," +
            "county_name text," +
            "county_code text," +
            "city_code integer)";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
        LogUtil.d("数据库创建成功");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
