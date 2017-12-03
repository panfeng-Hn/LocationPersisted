package com.panfeng.locationpersisted.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.panfeng.locationpersisted.LpAppliection;
import com.panfeng.locationpersisted.R;
import com.panfeng.locationpersisted.common.entity.CallEntity;

import java.util.List;

/**
 * Created by panfeng on 2017/11/29.
 * 进入提醒
 */

public class IntoCallListActivity extends Activity {

    private List<CallEntity> callEntityList;
    private ListView listView;


    private Button add_button;
    private BaseAdapter adapter = new BaseAdapter() {
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        {

            mInflater=LayoutInflater.from(IntoCallListActivity.this);
        }

        @Override
        public int getCount() {
            return callEntityList.size();
        }

        @Override
        public Object getItem(int i) {
            return callEntityList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View converView, ViewGroup viewGroup) {
            AwayCallListActivity.ViewHolder holder;
            //观察convertView随ListView滚动情况

            if (converView == null) {
                converView=mInflater.inflate(R.layout.call_item,null);
                holder=new AwayCallListActivity.ViewHolder();
                holder.address=converView.findViewById(R.id.item_address);
                holder.location_x=converView.findViewById(R.id.item_location_x);
                holder.location_y=converView.findViewById(R.id.item_location_y);
                holder.range=converView.findViewById(R.id.item_range);
                converView.setTag(holder);
            }
            else{
                holder = (AwayCallListActivity.ViewHolder)converView.getTag();//取出ViewHolder对象
            }
            CallEntity entity=callEntityList.get(i);
            holder.range.setText(entity.getCall_range());
            holder.location_x.setText(String.valueOf(entity.getLonggitude()));
            holder.location_y.setText(String.valueOf(entity.getLatitude()));
            return converView;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.into_call_activity);


        callEntityList=((LpAppliection)getApplication()).getCallAwayEntityList();
        add_button=findViewById(R.id.add_away_item);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(IntoCallListActivity.this,MapActivity.class);
                intent.putExtra("isEdit",1);
                intent.putExtra("editType",0);

                startActivity(intent);
            }
        });
        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                return false;
            }
        });
    }

}
