package com.panfeng.locationpersisted.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.panfeng.locationpersisted.LpAppliection;

import java.io.FileDescriptor;
import java.util.List;
import java.util.Timer;

import static com.baidu.location.LocationClientOption.LOC_SENSITIVITY_HIGHT;

/**
 * Created by panfeng on 2017/11/29.
 */

public class GetLocationService extends Service {

    MyBinder binder = new MyBinder();
    public LocationClient mLocationClient = null;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        return binder;

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                binder.onReceiveLocation(bdLocation);

                LpAppliection appliection= (LpAppliection) getApplication();

//                BaiduMap map= appliection.getMap();
//
//
//                map.setMyLocationConfiguration(new MyLocationConfiguration(Locat));
            }
        });

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
//        option.setOpenAutoNotifyMode(1000 * 60, 10, LOC_SENSITIVITY_HIGHT);
        mLocationClient.setLocOption(option);
        mLocationClient.start();


    }

    public class MyBinder extends Binder {

        private Timer timer;

        public Timer getTimer() {
            return timer;
        }

        public void setTimer(Timer timer) {
            this.timer = timer;
        }


        public void onReceiveLocation(BDLocation bdLocation) {

            Log.d("a","获取到位置信息:"+bdLocation.toString());

        }

    }
}
