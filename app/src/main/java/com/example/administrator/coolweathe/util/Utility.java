package com.example.administrator.coolweathe.util;

import com.example.administrator.coolweathe.db.CoolWeatherDB;
import com.example.administrator.coolweathe.model.City;
import com.example.administrator.coolweathe.model.County;
import com.example.administrator.coolweathe.model.Province;

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
                    LogUtil.d(array[1]);
                    county.setCoutyname(array[1]);
                    county.setCitycode(proviceid);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;

    }
}
