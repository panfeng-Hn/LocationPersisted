package com.panfeng.locationpersisted.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.panfeng.locationpersisted.LpAppliection;
import com.panfeng.locationpersisted.R;
import com.panfeng.locationpersisted.common.entity.CallEntity;
import com.panfeng.locationpersisted.common.entity.LocationPersistedEntity;

import java.io.FileDescriptor;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

import static com.baidu.location.LocationClientOption.LOC_SENSITIVITY_HIGHT;
import static com.baidu.mapapi.utils.DistanceUtil.getDistance;

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

                LpAppliection appliection = (LpAppliection) getApplication();

//                BaiduMap map= appliection.getMap();
//
//
//                map.setMyLocationConfiguration(new MyLocationConfiguration(Locat));
            }
        });

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(10000);
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
            LpAppliection appliection = ((LpAppliection) getApplication());
            appliection.setBdLocation(bdLocation);
            Log.d("a", "获取到位置信息:" + bdLocation.toString());
            //构建entity
            LocationPersistedEntity entity = new LocationPersistedEntity();
            entity.setTime(new Date());
            entity.setSpeed(bdLocation.getSpeed());

            entity.setStreet(bdLocation.getStreet());
            entity.setStreetNumber(bdLocation.getStreetNumber());
            entity.setProvince(bdLocation.getProvince());
            entity.setDistrict(bdLocation.getDistrict());
            entity.setCountry(bdLocation.getCountry());
            entity.setCity(bdLocation.getCity());

            entity.setLonggitude(bdLocation.getLongitude());
            entity.setLatitude(bdLocation.getLatitude());
            entity.setAltitude(bdLocation.getAltitude());
            entity.setAddress(bdLocation.getAddrStr());
            String operators = "无";
            switch (bdLocation.getOperators()) {
                case BDLocation.OPERATORS_TYPE_MOBILE:
                    operators = "中国移动";
                    break;
                case BDLocation.OPERATORS_TYPE_UNICOM:
                    operators = "中国联通";
                    break;
                case BDLocation.OPERATORS_TYPE_TELECOMU:
                    operators = "中国电信";
                    break;
            }
            entity.setOperators(operators);
            //将获取到的信息存到db
            SQLiteDatabase db = appliection.getDb();
            if (db.isOpen()) {
                String sql = new StringBuffer().
                        append("insert into Location_persisted values('").
                        append(UUID.randomUUID().toString()).
                        append("','").
                        append(bdLocation.getAddrStr()).
                        append("','").
                        append(bdLocation.getLatitude()).
                        append("','").
                        append(bdLocation.getLongitude()).
                        append("',").
                        append(bdLocation.getAltitude()).
                        append(",'").
                        append(bdLocation.getCity()).
                        append("','").
                        append(bdLocation.getCountry()).
                        append("','").
                        append(bdLocation.getDistrict()).
                        append("','").
                        append(bdLocation.getProvince()).
                        append("','").
                        append(bdLocation.getStreet()).
                        append("','").
                        append(bdLocation.getStreetNumber()).
                        append("','").
                        append(entity.getOperators()).
                        append("','").
                        append(bdLocation.getSpeed()).
                        append("','").
                        append(new Date().getTime()).
                        append("'").
                        toString();
                db.execSQL(sql);
            }
            //判断上一次是否有结果
            if (appliection.getLast_persisted() == null) {
                return;
            }

            boolean isNotify=false;

            List<CallEntity> awatlist = appliection.getCallAwayEntityList();
            for (CallEntity call : awatlist) {
                //上一次结果
                double last_result = DistanceUtil.getDistance(new LatLng(call.getLatitude(), call.getLonggitude()), new LatLng(appliection.getLast_persisted().getLatitude(), appliection.getLast_persisted().getLonggitude()));
                double nowResult = DistanceUtil.getDistance(new LatLng(call.getLatitude(), call.getLonggitude()), new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));

                int range= call.getCall_range();

                if(last_result<range&&nowResult>range){
                        //执行通知

                    if(!isNotify){
                        doNotify(call);
                        isNotify=true;
                    }
                }
            }


            List<CallEntity> intoList = appliection.getCallIntoEntityList();
            for (CallEntity call : intoList) {
                //上一次结果
                double last_result = DistanceUtil.getDistance(new LatLng(call.getLatitude(), call.getLonggitude()), new LatLng(appliection.getLast_persisted().getLatitude(), appliection.getLast_persisted().getLonggitude()));
                double nowResult = DistanceUtil.getDistance(new LatLng(call.getLatitude(), call.getLonggitude()), new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));
                int range= call.getCall_range();
                if(last_result>range&&nowResult<range){
                    if(!isNotify){
                        doNotify(call);
                        isNotify=true;
                    }
                }
            }

        }

        public  void doNotify(CallEntity entity){
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(GetLocationService.this,"");
            mBuilder.setContentTitle(entity.getTitle());//设置通知标题
            mBuilder.setContentText(entity.getContent());//设置通知内容
            mBuilder.setWhen(System.currentTimeMillis());//设置通知事件
            mBuilder.setAutoCancel(false);//设置是否自动退出
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
            mBuilder.setOngoing(false);
            mBuilder.setDefaults(Notification.DEFAULT_ALL);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);//设置通知显示的图标
            //发送通知
            mNotificationManager.notify((int)Math.random(),mBuilder.build());
            //播放震动，播放次序为：播放0.5秒，停0.4秒，再播放0.6秒
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{0,4000,400,3000},-1);
        }
    }
}
