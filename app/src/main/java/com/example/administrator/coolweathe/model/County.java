package com.example.administrator.coolweathe.model;

/**
 * Created by Administrator on 2016/1/5.
 */
public class County {
    private int id;
    private String coutyname;
    private String coutycode;
    private int citycode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoutyname() {
        return coutyname;
    }

    public void setCoutyname(String coutyname) {
        this.coutyname = coutyname;
    }

    public String getCoutycode() {
        return coutycode;
    }

    public void setCoutycode(String coutycode) {
        this.coutycode = coutycode;
    }

    public int getCitycode() {
        return citycode;
    }

    public void setCitycode(int citycode) {
        this.citycode = citycode;
    }
}
