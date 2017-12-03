package com.panfeng.locationpersisted.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.panfeng.locationpersisted.LpAppliection;
import com.panfeng.locationpersisted.R;

/**
 * Created by panfeng on 2017/11/29.
 */

public class MapActivity extends Activity {
    /**
     * 百度地图控制类
     */
    private BaiduMap baiduMap;
    private LinearLayout top_layout;
    private LinearLayout botton_layout;
    /**
     * 百度地图的view
     */
    private TextureMapView mapView;
    private EditText location_x;
    private EditText location_y;
    private EditText range;

    private TextView lable_location_x;
    private TextView labble_location_y;

    private TextView addr_near;
    private TextView addr_detali;

    private Button add_addr;
    private Button close_panel;
    private GeoCoder mSearch;


    private Boolean isEdit;
    private int editType;
    private LatLng newlatLng;


    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {

        public void onGetGeoCodeResult(GeoCodeResult result) {

            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                return;
            }
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                return;
            }
            String address = result.getSematicDescription();
            addr_near.setText(address);

            String addrDetaliText = new StringBuffer().
                    append(result.getAddressDetail().province).
                    append(result.getAddressDetail().city).
                    append(result.getAddressDetail().district).
                    append(result.getAddressDetail().street).
                    append(result.getAddressDetail().streetNumber).
                    toString();
            addr_detali.setText(addrDetaliText);


        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(this, savedInstanceState);
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(listener);
        setContentView(R.layout.map_activity);
        mapView = findViewById(R.id.baidu_map_view);
        baiduMap = mapView.getMap();
        top_layout = findViewById(R.id.edit_liner_layout);
        botton_layout = findViewById(R.id.detali_layout);
        location_x = findViewById(R.id.edit_x);
        location_x = findViewById(R.id.edit_y);

        lable_location_x = findViewById(R.id.text_x);
        labble_location_y = findViewById(R.id.text_y);
        addr_near = findViewById(R.id.addr_near);
        addr_detali = findViewById(R.id.address);
        range = findViewById(R.id.edit_rang);

        isEdit=savedInstanceState.getInt("isEdit")==0?false:true;
        editType=savedInstanceState.getInt("editType");
        if(isEdit){
            botton_layout.setVisibility(View.GONE);
            top_layout.setVisibility(View.VISIBLE);
        }else{
            botton_layout.setVisibility(View.VISIBLE);
            top_layout.setVisibility(View.GONE);

        }

        LpAppliection appliection = (LpAppliection) getApplication();
        //进入地图后先将地图状态更新到当前位置，如果不更新，默认在位置在北京天安门
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng(appliection.getBdLocation().getLatitude(), appliection.getBdLocation().getLongitude()));
        baiduMap.animateMapStatus(mapStatusUpdate);
        setListener();
        setMapListener();

    }


    private void setListener() {
        add_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MapActivity.this,AddCallActivity.class);
                intent.putExtra("x",newlatLng.longitude);
                intent.putExtra("y",newlatLng.latitude);
                intent.putExtra("range",range.getText().toString());
                intent.putExtra("editType",editType);


            }
        });

        close_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botton_layout.setVisibility(View.GONE);
            }
        });


    }

    private void setMapListener() {
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                newlatLng=latLng;
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
                location_x.setText(String.valueOf(latLng.longitude));
                location_y.setText(String.valueOf(latLng.latitude));
                lable_location_x.setText(String.valueOf(latLng.longitude));
                labble_location_y.setText(String.valueOf(latLng.latitude));
                add_addr.setText("获取中");
                addr_detali.setText("获取中");
                mSearch.destroy();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                newlatLng=mapPoi.getPosition();
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mapPoi.getPosition()));
                location_x.setText(String.valueOf(mapPoi.getPosition().longitude));
                location_y.setText(String.valueOf(mapPoi.getPosition().latitude));

                lable_location_x.setText(String.valueOf(mapPoi.getPosition().longitude));
                labble_location_y.setText(String.valueOf(mapPoi.getPosition().latitude));
                add_addr.setText("获取中");
                addr_detali.setText("获取中");
                mSearch.destroy();

                return false;
            }
        });
        if(isEdit){
            baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    newlatLng=marker.getPosition();
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(marker.getPosition()));
                    location_x.setText(String.valueOf(marker.getPosition().longitude));
                    location_y.setText(String.valueOf(marker.getPosition().latitude));
                    lable_location_x.setText(String.valueOf(marker.getPosition().longitude));
                    labble_location_y.setText(String.valueOf(marker.getPosition().latitude));
                    add_addr.setText("获取中");
                    addr_detali.setText("获取中");
                    mSearch.destroy();
                    return false;
                }
            });

        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


}
