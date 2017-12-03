package com.panfeng.locationpersisted.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.panfeng.locationpersisted.LpAppliection;
import com.panfeng.locationpersisted.R;
import com.panfeng.locationpersisted.common.entity.LocationPersistedEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by panfeng on 2017/11/29.
 * 位置记录查看
 */

public class LocationWatchActivity extends Activity {

    private ListView listView;

    private List<LocationPersistedEntity> persistedEntityList;

    private BaseAdapter adapter = new BaseAdapter() {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        {
            mInflater = LayoutInflater.from(LocationWatchActivity.this);
        }

        @Override
        public int getCount() {
            return persistedEntityList.size();
        }

        @Override
        public Object getItem(int i) {
            return persistedEntityList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            RecordViewHolder holder = null;

            if (view == null) {
                view = mInflater.inflate(R.layout.recode_item, null);
                holder = new RecordViewHolder();
                holder.address = view.findViewById(R.id.record_address);
                holder.location_x = view.findViewById(R.id.recode_location_x);
                holder.location_y = view.findViewById(R.id.recode_location_y);
                holder.time = view.findViewById(R.id.location_time);
                view.setTag(holder);
            } else {
                holder = (RecordViewHolder) view.getTag();
            }
            LocationPersistedEntity entity = persistedEntityList.get(i);
            holder.location_x.setText(String.valueOf(entity.getLonggitude()));
            holder.location_y.setText(String.valueOf(entity.getLatitude()));
            holder.address.setText(entity.getTime() == null ? "未知" : entity.getTime().toString());
            holder.address.setText(new StringBuffer().
                    append(entity.getProvince()).
                    append(entity.getCity()).
                    append(entity.getDistrict()).
                    append(entity.getStreet()).
                    append(entity.getStreetNumber()).
                    toString());
            return view;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_watch_activity);
        initData();
        listView = findViewById(R.id.location_watch_list);
        listView.setAdapter(adapter);

    }


    private void initData() {
        if (persistedEntityList == null) {
            persistedEntityList = new ArrayList<>();
        }
        LpAppliection appliection = ((LpAppliection) getApplication());
        SQLiteDatabase db = appliection.getDb();
        if (db.isOpen()) {
            // id,address ,latitude ,longgitude ,altitude ,city ,country ,district ,province ,street ,streetNumber ,operators ,speed ,time )");
            Cursor cursor = db.query("Location_persisted", new String[]{"id", "address", "latitude", "longgitude", "altitude", "city", "country", "district", "province", "street", "streetNumber", "operators", "speed", "time"}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                LocationPersistedEntity entity = new LocationPersistedEntity();
                persistedEntityList.add(entity);
                entity.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                entity.setAltitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex("altitude"))));
                entity.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex("latitude"))));
                entity.setLonggitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex("longgitude"))));
                entity.setCity(cursor.getString(cursor.getColumnIndex("city")));
                entity.setCountry(cursor.getString(cursor.getColumnIndex("country")));
                entity.setDistrict(cursor.getString(cursor.getColumnIndex("district")));
                entity.setProvince(cursor.getString(cursor.getColumnIndex("province")));
                entity.setStreet(cursor.getString(cursor.getColumnIndex("street")));
                entity.setStreetNumber(cursor.getString(cursor.getColumnIndex("streetNumber")));
                entity.setOperators(cursor.getString(cursor.getColumnIndex("operators")));
                entity.setSpeed(Float.parseFloat(cursor.getString(cursor.getColumnIndex("speed"))));
                entity.setTime(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex("time")))));
            }

        }

    }


    public static class RecordViewHolder {
        public TextView location_x;
        public TextView location_y;
        public TextView time;
        public TextView address;
    }

}
