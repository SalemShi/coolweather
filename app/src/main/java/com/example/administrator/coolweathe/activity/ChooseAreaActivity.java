package com.example.administrator.coolweathe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.administrator.coolweathe.util.Utility;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
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

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private CoolWeatherDB coolWeatherDB;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==HttpUtil.OK_SUNCCESS)
            {
                LogUtil.d("Message sunccess");
                if (Utility.handleProvincesResponse(coolWeatherDB,(String)msg.obj))
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                         queryProvinces();
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
        queryProvinces();


    }


    private  void queryProvinces()
    {
        provinceList = coolWeatherDB.loadProvince();
        LogUtil.d("provincelist.size="+provinceList.size());
        if(provinceList.size()>0)
        {
            datalist.clear();
            for(Province p :provinceList)
            {
                datalist.add(p.getProvincename());
            }
            arrayAdapter.notifyDataSetChanged();
            title.setText("china");
        }
        else
        {
            queryFromService(null,"province");
        }
    }
    private void queryFromService(final String code,final String type)
    {
        if (code==null)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.d("TO SEE SERVICE");
                   HttpUtil.OKdoGet("http://www.weather.com.cn/data/list3/city.xml?level=1",handler);
                }
            }).start();

        }
    }
}
