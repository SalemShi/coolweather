package com.example.administrator.coolweathe.activity;
///data/data/com.example.administrator.coolweathe/databases/

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.coolweathe.R;
import com.example.administrator.coolweathe.db.CoolWeatherDB;
import com.example.administrator.coolweathe.model.City;
import com.example.administrator.coolweathe.model.County;
import com.example.administrator.coolweathe.model.Province;
import com.example.administrator.coolweathe.util.HttpUtil;
import com.example.administrator.coolweathe.util.LogUtil;
import com.example.administrator.coolweathe.util.TextUtil;
import com.example.administrator.coolweathe.util.Utility;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;

/**
 * Created by Administrator on 2016/1/6.
 */
public class ChooseAreaActivity extends Activity {
    private TextView title;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> datalist = new ArrayList<String>();
    private String flag;
    private int CurrentLevel;
    private int ProvinceLevel = 1;
    private int CityLevel = 2;
    private int CountyLevel = 3;
    private Province selectedProvice;
    private City selectedCity;


    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private CoolWeatherDB coolWeatherDB;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HttpUtil.OK_SUNCCESS) {
                boolean result;
                if (flag.equals("province"))
                    result = Utility.handleProvincesResponse(coolWeatherDB, (String) msg.obj);
                else if (flag.equals("city"))
                    result = Utility.handleCityResponse(coolWeatherDB, (String) msg.obj, selectedProvice.getId());
                else
                    result = Utility.handleCountyResponse(coolWeatherDB, (String) msg.obj, selectedCity.getId());
                if(result)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(flag.equals("province"))
                                queryProvinces();
                            else if(flag.equals("city"))
                                queryCity();
                            else
                                queryCountry();
                                //LogUtil.d("STOP");
                        }
                    });
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        title = (TextView)findViewById(R.id.text_title);
        listView = (ListView)findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(arrayAdapter);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
       // coolWeatherDB.db.execSQL("delete from province");
        queryProvinces();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (CurrentLevel == ProvinceLevel) {
                    selectedProvice = provinceList.get(position);
                    queryCity();
                } else if (CurrentLevel == CityLevel) {
                    selectedCity = cityList.get(position);
                    queryCountry();
                }

            }
        });

    }


    private  void queryProvinces()
    {
        //查询省份数据
        provinceList = coolWeatherDB.loadProvince();
        LogUtil.d("provincelist.size=" + provinceList.size());
        if(provinceList.size()>0)
        {
            datalist.clear();
            for(Province p :provinceList)
            {
                datalist.add(p.getProvincename());
            }
            arrayAdapter.notifyDataSetChanged();
            CurrentLevel = ProvinceLevel;
            title.setText("中国");
        }
        else
        {
            queryFromService(null,"province");
        }
    }
    private  void queryCity()
    {
        //查询城市
        LogUtil.d("所选省市=" + selectedProvice.getProvincename());
        cityList = coolWeatherDB.loadCity(selectedProvice.getId());
        LogUtil.d("city.size=" + cityList.size());
        if(cityList.size()>0)
        {
            datalist.clear();
            for(City c :cityList)
            {
                datalist.add(c.getCityname());
            }
            arrayAdapter.notifyDataSetChanged();
            CurrentLevel = CityLevel;
            title.setText(selectedProvice.getProvincename());
        }
        else
        {
            queryFromService(selectedProvice.getProvincecode(),"city");
        }
    }
    private  void queryCountry()
    {
        //查询县
        LogUtil.d("所选市县=" + selectedCity.getCityname());
        countyList = coolWeatherDB.loadCounty(selectedCity.getId());
        LogUtil.d("county.size=" + countyList.size());
        if(countyList.size()>0)
        {
            datalist.clear();
            for(County c :countyList)
            {
                datalist.add(c.getCoutyname());
                LogUtil.d(c.getCoutycode()+c.getCoutyname()+c.getCitycode());
            }
            arrayAdapter.notifyDataSetChanged();
            CurrentLevel =CountyLevel ;
            title.setText(selectedCity.getCityname());
        }
        else
        {
            queryFromService(selectedCity.getCitycode(),"county");
        }
    }
    private void queryFromService(final String code,final String type)
    {
        //从服务器上查询
       flag = type;
       final String url ;
        if (!TextUtil.isEmpty(code))
                url="http://www.weather.com.cn/data/list3/city"+code+".xml";
        else
            url = "http://www.weather.com.cn/data/list3/city.xml?level=1";
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("URL="+url);
                HttpUtil.OKdoGet(url,handler);
            }
        }).start();
    }
}
