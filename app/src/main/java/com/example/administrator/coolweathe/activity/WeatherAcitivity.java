package com.example.administrator.coolweathe.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.coolweathe.R;
import com.example.administrator.coolweathe.service.AutoUpdateService;
import com.example.administrator.coolweathe.util.HttpUtil;
import com.example.administrator.coolweathe.util.LogUtil;
import com.example.administrator.coolweathe.util.TextUtil;
import com.example.administrator.coolweathe.util.Utility;

/**
 * Created by Administrator on 2016/1/12.
 */
public class WeatherAcitivity extends Activity implements View.OnClickListener{
    private TextView cityname;
    private LinearLayout weatherinfo;
    private TextView publish;
    private TextView temp1;
    private TextView temp2;
    private TextView weatherdesp;
    private TextView currtentdate;

    private Button swithcity;
    private Button reflush;
    private String flag;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
           if(msg.what==HttpUtil.OK_SUNCCESS)
           {
               String result = (String)msg.obj;
               if(flag.equals("countyCode"))
               {
                   String args[] =result.split("\\|");
                   String weathercode = args[1];
                   queryWeatherinfo(weathercode);
               }
               else
               {
                   Utility.handleWeatherResponse(WeatherAcitivity.this,result);
                   showWeather();
               }
           }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        cityname = (TextView)findViewById(R.id.city_name);
        weatherinfo = (LinearLayout)findViewById(R.id.weather_info_layout);
        temp1 = (TextView)findViewById(R.id.temp1);
        temp2 = (TextView)findViewById(R.id.temp2);
        publish = (TextView)findViewById(R.id.publlish_text);
        LogUtil.d("kankan");
        weatherdesp = (TextView)findViewById(R.id.weather_desp);
        currtentdate = (TextView)findViewById(R.id.current_date);
        String countycode = getIntent().getStringExtra("county_code");
        LogUtil.d("县区代码为="+countycode);
        swithcity = (Button)findViewById(R.id.switch_city);
        reflush = (Button)findViewById(R.id.refresh);
        swithcity.setOnClickListener(this);
        reflush.setOnClickListener(this);
        if(!TextUtil.isEmpty(countycode))
        {
            publish.setText("同步中");
            weatherinfo.setVisibility(View.INVISIBLE);
            cityname.setVisibility(View.INVISIBLE);
            quaryWeather(countycode);
        }
        else
            showWeather();



    }
    public void quaryWeather(String countycode)
    {
        String address = "http://www.weather.com.cn/data/list3/city" +countycode + ".xml";
        queryFromServer(address, "countyCode");
    }
    public void queryWeatherinfo(String weatherCode)
    {
        String address = "http://www.weather.com.cn/data/cityinfo/" +weatherCode + ".html";
        LogUtil.d("查询天气的地址："+address);
        queryFromServer(address, "weatherCode");

    }
    public void queryFromServer(final String url,final  String type)
    {
        flag=type;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.OKdoGet(url,handler);
            }
        }).start();
    }
    public void showWeather()
    {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(WeatherAcitivity.this);
        cityname.setText(pre.getString("cityname", ""));
        temp1.setText(pre.getString("temp1",""));
        temp2.setText(pre.getString("temp2",""));
        weatherdesp.setText(pre.getString("weatherDesp",""));
        currtentdate.setText(pre.getString("currentdate",""));
        publish.setText("今天"+pre.getString("publishtime","")+"发布");
        temp1.setText(pre.getString("temp1", ""));
        weatherinfo.setVisibility(View.VISIBLE);
        cityname.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.switch_city:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh:
                publish.setText(" 同步中...");
                SharedPreferences prefs = PreferenceManager.
                        getDefaultSharedPreferences(this);
                String weatherCode = prefs.getString("weathercode", "");
                if (!TextUtils.isEmpty(weatherCode)) {
                    queryWeatherinfo(weatherCode);
                }
                break;
            default:
                break;
        }

    }
}
