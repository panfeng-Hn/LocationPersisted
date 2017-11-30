package com.panfeng.locationpersisted.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.panfeng.locationpersisted.R;

/**
 * Created by panfeng on 2017/11/29.
 */

public class LocationWatchActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_watch_activity);
    }

}
