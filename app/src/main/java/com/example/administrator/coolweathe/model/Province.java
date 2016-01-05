package com.example.administrator.coolweathe.model;

/**
 * Created by Administrator on 2016/1/5.
 */
public class Province {
    private int id ;
    private String provincename;
    private String provincecode;

    public String getProvincecode() {
        return provincecode;
    }

    public void setProvincecode(String provincecode) {
        this.provincecode = provincecode;
    }

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
