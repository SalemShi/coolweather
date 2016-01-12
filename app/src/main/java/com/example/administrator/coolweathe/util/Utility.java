package com.example.administrator.coolweathe.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.administrator.coolweathe.db.CoolWeatherDB;
import com.example.administrator.coolweathe.model.City;
import com.example.administrator.coolweathe.model.County;
import com.example.administrator.coolweathe.model.Province;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/1/6.
 */
public class Utility {
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {
        if (!TextUtil.isEmpty(response)) {
                String[] allProvinces = response.split(",");
                if (allProvinces != null && allProvinces.length > 0) {
                    for (String p : allProvinces) {
                        String[] array = p.split("\\|");
                        Province province = new Province();
                        province.setProvincecode(array[0]);
                        province.setProvincename(array[1]);
                        coolWeatherDB.saveProvince(province);
                    }
                    return true;
                }
            }
        return false;

    }
    public  static boolean handleCityResponse(CoolWeatherDB coolWeatherDB, String response,int proviceid) {
            if (!TextUtil.isEmpty(response)) {
                String[] allCity = response.split(",");
                if (allCity != null && allCity.length > 0) {
                    for (String p : allCity) {
                        String[] array = p.split("\\|");
                       // LogUtil.d("array长度="+array.length+",array[0]="+array[0]+",array[1]="+array[1]);
                        City city = new City();
                        city.setCitycode(array[0]);
                        city.setCityname(array[1]);
                        city.setProvincecode(proviceid);
                        coolWeatherDB.saveCity(city);
                    }
                    return true;
                }
             }
        return false;

    }
    public  static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB, String response,int proviceid) {
        if (!TextUtil.isEmpty(response)) {
            String[] allCounty = response.split(",");
            if (allCounty != null && allCounty.length > 0) {
                for (String p : allCounty) {
                    String[] array = p.split("\\|");
                    // LogUtil.d("array长度="+array.length+",array[0]="+array[0]+",array[1]="+array[1]);
                    County county = new County();
                    county.setCoutycode(array[0]);
                    county.setCoutyname(array[1]);
                    county.setCitycode(proviceid);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;

    }
    public static void handleWeatherResponse(Context context,String response)
    {
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherjson = jsonObject.getJSONObject("weatherinfo");
            String cityname = weatherjson.getString("city");
            String weathercode = weatherjson.getString("cityid");
            String temp1 = weatherjson.getString("temp1");
            String temp2 = weatherjson.getString("temp2");
            String weather = weatherjson.getString("weather");
            String publishtime =weatherjson.getString("ptime");
            saveWeatherInfo(context,cityname,weathercode,temp1,temp2,weather,publishtime);
        }
       catch (Exception e)
       {
           e.printStackTrace();
       }
    }
    public static void saveWeatherInfo(Context context, String cityName,String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("cityname",cityName);
        editor.putString("weathercode",weatherCode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weatherDesp",weatherDesp);
        editor.putString("publishtime",publishTime);
        editor.putString("currentdate",simpleDateFormat.format(new Date()));
        editor.commit();

    }
}
