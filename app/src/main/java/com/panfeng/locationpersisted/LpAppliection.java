package com.panfeng.locationpersisted;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;

import java.io.File;

/**
 * Created by panfeng on 2017/11/29.
 */

public class LpAppliection extends Application {

    public SQLiteDatabase db;

    public Boolean is_init_db=false;

    private BaiduMap map;


    public BaiduMap getMap() {
        return map;
    }

    public void setMap(BaiduMap map) {
        this.map = map;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        init();
    }


    public void init(){


        try {
            String path=getSDPath()+"/locationpersisted";

            File file =new File(path);
            if(!file.exists()){
                file.mkdir();
            }
            db=SQLiteDatabase.openOrCreateDatabase(path+"/locationpersisted.db",null);
            //创建配置表
            db.execSQL("create table if not exists config(name text,value text)");
            //创建日志表
            db.execSQL("create table if not exists Location_persisted(update_time TIMESTAMP,Location_x text,Location_y text,address text)");

            //创建提醒表
            db.execSQL("create table if not exists call_list(Location_x text,Location_y text,call_type integer)");

            is_init_db=true;


        } catch (Exception e) {
            Log.d("出错！","初始化数据库出错！",e);
        }

    }


    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
