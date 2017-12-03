package com.panfeng.locationpersisted;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.panfeng.locationpersisted.common.entity.CallEntity;
import com.panfeng.locationpersisted.common.entity.LocationPersistedEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by panfeng on 2017/11/29.
 */

public class LpAppliection extends Application {

    public SQLiteDatabase db;

    public Boolean is_init_db=false;


    /**
     * 百度当前位置
     */
    private BDLocation bdLocation;

    private List<CallEntity> callIntoEntityList;
    private List<CallEntity> callAwayEntityList;

    public BDLocation getBdLocation() {
        return bdLocation;
    }

    /**
     * 当前配置
     */
    private Map<String,String> config;

    /**
     * 最后一次定位信息
     */
    private LocationPersistedEntity last_persisted;

    private List<UpdateConfigCallBack> configCallBackList;



    public void setBdLocation(BDLocation bdLocation) {
        this.bdLocation = bdLocation;
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
            db.execSQL("create table if not exists Location_persisted(id text,address text,latitude text,longgitude text,altitude integer,city text,country text,district text,province text,street text,streetNumber text,operators text,speed text,time text )");

            //创建进入提醒表
            db.execSQL("create table if not exists call_into_list(latitude text,longgitude text,call_range integer,title text,content text,icon text)");
            //创建离开提醒表
            db.execSQL("create table if not exists call_away_list(latitude text,longgitude text,call_range integer,title text,content text,icon text)");
            is_init_db=true;
            updateCallEntityList();
            updateConfig();


        } catch (Exception e) {
            Log.d("出错！","初始化数据库出错！",e);
        }

    }

    private void updateCallEntityList(){
        if (callIntoEntityList==null)
            callIntoEntityList=new ArrayList<>();
        if (callAwayEntityList==null)
            callAwayEntityList=new ArrayList<>();


        if(db!=null&&db.isOpen()){
            Cursor cursor= db.query("call_away_list",new String[]{"latitude","longgitude","call_range","title","content","icon"},null,null,null,null,null);
            while (cursor.moveToNext()){
                CallEntity entity=new CallEntity();
                entity.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex("latitude"))));
                entity.setLonggitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex("longgitude"))));
                entity.setCall_range(cursor.getInt(cursor.getColumnIndex("call_range")));
                entity.setCall_type(0);
                entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                entity.setContent(cursor.getString(cursor.getColumnIndex("content")));
                entity.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
                callIntoEntityList.add(entity);
            }

            Cursor awayCursor= db.query("call_away_list",new String[]{"latitude","longgitude","call_range","title","content","icon"},null,null,null,null,null);
            while (cursor.moveToNext()){
                CallEntity entity=new CallEntity();
                entity.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex("latitude"))));
                entity.setLonggitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex("longgitude"))));
                entity.setCall_range(cursor.getInt(cursor.getColumnIndex("call_range")));
                entity.setCall_type(1);
                entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                entity.setContent(cursor.getString(cursor.getColumnIndex("content")));
                entity.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
                callAwayEntityList.add(entity);
            }

        }
    }





    private void updateConfig(){
        try {
            if(config==null){
                config=new HashMap<>();
            }
            if (configCallBackList==null){
                configCallBackList=new ArrayList<>();
            }


            if(db!=null&&db.isOpen()){
                Cursor cursor= db.query("config",new String[]{"name","value"},null,null,null,null,null);
                while (cursor.moveToNext()){
                        config.put(cursor.getString(cursor.getColumnIndex("name")),cursor.getString(cursor.getColumnIndex("value")));
                }

                for (UpdateConfigCallBack callBack:configCallBackList){
                    callBack.RunTask(config);
                }
            }
        } catch (Exception e) {
            Log.d("","更新配置出错");
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

    public List<CallEntity> getCallIntoEntityList() {
        return callIntoEntityList;
    }


    public List<CallEntity> getCallAwayEntityList() {
        return callAwayEntityList;
    }


    public LocationPersistedEntity getLast_persisted() {
        return last_persisted;
    }

    public void setLast_persisted(LocationPersistedEntity last_persisted) {
        this.last_persisted = last_persisted;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    /**
     * 更新配置回调
     */
    public static interface UpdateConfigCallBack {

        /**
         * 更新配置回调方法
         * @param config
         */
        public void RunTask(Map<String,String> config);
    }
}
