package com.example.administrator.coolweathe.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.coolweathe.model.City;
import com.example.administrator.coolweathe.model.County;
import com.example.administrator.coolweathe.model.Province;
import com.example.administrator.coolweathe.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/5.
 */
public class CoolWeatherDB {
    /*
    *���ݿ�����
    */
    public static final String DB_NAME = "CoolWeather";
    public static final int  VERSON =1;
    private static CoolWeatherDB coolWeatherDB;
    public SQLiteDatabase db;
    private  CoolWeatherDB(Context context)
    {
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null, VERSON);
            db =dbHelper.getWritableDatabase();
    }
    public synchronized  static  CoolWeatherDB getInstance(Context context)
    {
        if(coolWeatherDB==null)
        {
            coolWeatherDB = new CoolWeatherDB(context);
            return coolWeatherDB;
        }
        return coolWeatherDB;
    }
    public void saveProvince(Province province)
    {
        if(province!=null)
        {
            ContentValues values = new ContentValues();
            values.put("province_name",province.getProvincename());
            values.put("province_code",province.getProvincecode());
            db.insert("Province", null, values);
            LogUtil.d("插入省成功");
        }
        else
            LogUtil.d("插入省失败");

    }
    public void saveCity(City city)
    {
        if(city !=null)
        {
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityname());
            values.put("city_code",city.getCitycode());
            values.put("province_code", city.getProvincecode());
            db.insert("City", null, values);
        }

    }
    public void saveCounty(County county)
    {
        if(county !=null)
        {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCoutyname());
            values.put("county_code",county.getCoutycode());
            values.put("city_code",county.getCitycode());
            db.insert("County", null, values);
            LogUtil.d("插入县城成功");
        }
        else
             LogUtil.d("插入县城失败");
    }
    public List<Province> loadProvince()
    {
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvincecode(cursor.getString(cursor.getColumnIndex("province_code")));
                province.setProvincename(cursor.getString(cursor.getColumnIndex("province_name")));
                list.add(province);
            }
            while (cursor.moveToNext());
        }
        return list;
    }
    public List<City> loadCity(int privice_code)
    {
        List<City> list = new ArrayList<City>();
       //Cursor cursor = db.query("city",null,"province_code = ?",new String[]{String.valueOf(privice_code)},null,null,null);
       // String s ="select * from city where province_code="+String.valueOf(privice_code);
        String s=String.valueOf(privice_code);
        Cursor cursor = db.rawQuery("select * from city where province_code="+s,null);
        //Cursor cursor = db.rawQuery("select * from city where province_code= ?", new String[]{String.valueOf(privice_code)});
     //  Cursor cursor = db.query("City",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityname(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCitycode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvincecode(cursor.getInt(cursor.getColumnIndex("province_code")));
                list.add(city);
            }
            while (cursor.moveToNext());
        }
        return list;
    }
    public List<County> loadCounty(int city_code)
    {
        List<County> list = new ArrayList<County>();
        String s =String.valueOf(city_code);
        LogUtil.d(s);
        Cursor cursor = db.rawQuery("select * from county where city_code=" + s, null);
       // Cursor cursor = db.query("=County",null,"city_code=?",new String[]{String.valueOf(city_code)},null,null,null);
        if(cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCoutycode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCoutyname(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCitycode(cursor.getInt(cursor.getColumnIndex("city_code")));
                list.add(county);
            }
            while (cursor.moveToNext());
        }
        return list;
    }


}
