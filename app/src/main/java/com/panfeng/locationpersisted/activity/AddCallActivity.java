package com.panfeng.locationpersisted.activity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.panfeng.locationpersisted.LpAppliection;
import com.panfeng.locationpersisted.R;

/**
 * Created by panfeng on 2017/12/3.
 * //添加提醒的详细页面，
 */

public class AddCallActivity extends Activity {

    private Button add;
    private EditText title;
    private EditText content;


    private EditText add_call_x;
    private EditText add_call_y;
    private EditText range;
    private int editType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_call_activity);
        add = findViewById(R.id.add_notify);
        title = findViewById(R.id.notify_title);
        content = findViewById(R.id.notify_content);
        add_call_x.setText(String.valueOf(savedInstanceState.getDouble("x")));
        add_call_y.setText(String.valueOf(savedInstanceState.getDouble("y")));
        range.setText(String.valueOf(savedInstanceState.getDouble("range")));
        editType = savedInstanceState.getInt("editType");
        //设置添加按钮的事件
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LpAppliection appliection = ((LpAppliection) getApplication());
                SQLiteDatabase db = appliection.getDb();
                if (db.isOpen()) {
                    if (editType == 0) {
                        db.execSQL("insert into call_into_list values(latitude,longgitude,call_range,title,content,icon) ", new Object[]{
                                add_call_x.getText().toString(),
                                add_call_y.getText().toString(),
                                Integer.parseInt(range.getText().toString()),
                                title.getText().toString(),
                                content.getText().toString(),
                                null});
                    }else{
                        db.execSQL("insert into call_away_list values(latitude,longgitude,call_range,title,content,icon) ", new Object[]{
                                add_call_x.getText().toString(),
                                add_call_y.getText().toString(),
                                Integer.parseInt(range.getText().toString()),
                                title.getText().toString(),
                                content.getText().toString(),
                                null});
                    }
                    //添加完成后调用返回键返回到上一个页面
                    AddCallActivity.this.onBackPressed();

                }
            }
        });


    }
}
