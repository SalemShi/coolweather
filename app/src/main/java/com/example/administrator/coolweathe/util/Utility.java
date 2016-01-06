package com.example.administrator.coolweathe.util;

import com.example.administrator.coolweathe.db.CoolWeatherDB;
import com.example.administrator.coolweathe.model.Province;

/**
 * Created by Administrator on 2016/1/6.
 */
public class Utility {
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {
        if (!response.equals(null)) {
            LogUtil.d("response="+response);
            if (!response.equals(null)) {
                String[] allProvinces = response.split(",");
                if (allProvinces != null && allProvinces.length > 0) {
                    for (String p : allProvinces) {
                        String[] array = p.split("\\|");
                        Province province = new Province();
                        province.setProvincecode(array[0]);
                        province.setProvincename(array[1]);
                        // 将解析出来的数据存储到Province表
                        coolWeatherDB.saveProvince(province);
                    }
                    return true;
                }
            }
        }
        return false;

    }
}
