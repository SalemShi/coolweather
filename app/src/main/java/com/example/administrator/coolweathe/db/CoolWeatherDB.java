package com.example.administrator.coolweathe.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.coolweathe.model.City;
import com.example.administrator.coolweathe.model.County;
import com.example.administrator.coolweathe.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/5.
 */
public class CoolWeatherDB {
    /*
    *Êý¾Ý¿âÃû³Æ
    */
    public static final String DB_NAME = "";
    public static final int  VERSON =1;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;
    private  CoolWeatherDB(Context context)
    {
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null, VERSON);
        db = dbHelper.getWritableDatabase();
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
            values.put("id",province.getId());
            values.put("province_name",province.getProvincename());
            values.put("province_code",province.getProvincecode());
            db.insert("Province",null,values);
        }
    }
    public void saveCity(City city)
    {
        if(city !=null)
        {
            ContentValues values = new ContentValues();
            values.put("id",city.getId());
            values.put("city_name",city.getCityname());
            values.put("city_code",city.getCitycode());
            values.put("province_code",city.getProvincecode());
            db.insert("City",null,values);
        }
    }
    public void saveCounty(County county)
    {
        if(county !=null)
        {
            ContentValues values = new ContentValues();
            values.put("id", county.getId());
            values.put("county_name", county.getCoutyname());
            values.put("county_code",county.getCoutycode());
            values.put("city_code",county.getCitycode());
            db.insert("County",null,values);
        }
    }
    public List<Province> loadProvince()
    {
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        do
        {
            Province province = new Province();
            province.setId(cursor.getInt(cursor.getColumnIndex("id")));
            province.setProvincecode(cursor.getString(cursor.getColumnIndex("province_code")));
            province.setProvincename(cursor.getString(cursor.getColumnIndex("province_name")));
            list.add(province);
        }
        while (cursor.moveToNext());
        return list;
    }
    public List<City> loadCity(int privice_code)
    {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City",null,"province_code=?",new String[]{""+privice_code},null,null,null);
        do {
            City city = new City();
            city.setId(cursor.getInt(cursor.getColumnIndex("id")));
            city.setCityname(cursor.getString(cursor.getColumnIndex("city_name")));
            city.setCitycode(cursor.getString(cursor.getColumnIndex("city_code")));
            city.setProvincecode(cursor.getInt(cursor.getColumnIndex("province_code")));
            list.add(city);
        }
        while (cursor.moveToNext());
        return list;
    }
    public List<County> loadCounty(int city_code)
    {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("=County",null,"city_code=?",new String[]{""+city_code},null,null,null);
        do {
            County county = new County();
            county.setId(cursor.getInt(cursor.getColumnIndex("id")));
            county.setCoutycode(cursor.getString(cursor.getColumnIndex("county_name")));
            county.setCoutyname(cursor.getString(cursor.getColumnIndex("county_code")));
            county.setCitycode(cursor.getInt(cursor.getColumnIndex("city_code")));
            list.add(county);
        }
        while (cursor.moveToNext());
        return list;
    }


}
