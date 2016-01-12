package com.example.administrator.coolweathe.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.administrator.coolweathe.receiver.AutoUpdateReceiver;
import com.example.administrator.coolweathe.util.Utility;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by Administrator on 2016/1/12.
 */
public class AutoUpdateService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000; // 这是8小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
    private  void updateWeather()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String weathercode = sp.getString("weathercode", "");
        String address = "http://www.weather.com.cn/data/cityinfo/" +weathercode + ".html";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        try{
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful())
            {
                Utility.handleWeatherResponse(this,response.body().string());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
