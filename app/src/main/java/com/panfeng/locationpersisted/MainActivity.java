package com.panfeng.locationpersisted;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    //权限获取
    private static final int BAIDU_READ_PHONE_STATE =100;


    //离开提醒列表
    private Button awaw_call_list;
    //进入提醒列表
    private Button into_call_list;
    //设置位置刷新频率
    private Button set_location_button;
    //位置记录查看
    private Button location_cord_watch;
    //位置刷新频率edit
    private EditText location_refush_rate;
    private Button watch_map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        awaw_call_list=findViewById(R.id.awaw_call_list);
        into_call_list=findViewById(R.id.into_call_list);
        set_location_button=findViewById(R.id.set_location_button);
        location_cord_watch=findViewById(R.id.location_cord_watch);
        location_refush_rate=findViewById(R.id.location_refush_rate);
        //检查权限
        checkPermission();
        setListener();

    }

    private  void checkPermission(){
        if(this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            requestPermissions( new String[]{ Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },BAIDU_READ_PHONE_STATE );
        }


    }

    @SuppressLint("WrongConstant")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Boolean is_permission=true;

       if(requestCode== BAIDU_READ_PHONE_STATE){
            for (int i=0;i<grantResults.length;i++){
                if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                    is_permission=false;
                    break;
                }
            }
            if(is_permission){
                LpAppliection appliection= (LpAppliection) getApplication();
                appliection.init();
            }else{

                Toast.makeText(this,"权限获取失败，程序退出！",0);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
       }
    }

    private void setListener(){
        awaw_call_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        into_call_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        set_location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rate= set_location_button.getText().toString();
                double d= Double.parseDouble(rate);
                int rate_int=(int)d;

                LpAppliection appliection= (LpAppliection) getApplication();

                SQLiteDatabase db= appliection.getDb();
                db.execSQL("delete from config where name='rate'");
                db.execSQL("insert into config (name,value) values('rate',"+rate+")");



                //TODO 更新service中的内容

            }
        });

        location_cord_watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        watch_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }

}
