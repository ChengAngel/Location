package com.zhiqiang.locationdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * ================================================
 * 作    者：程志强
 * 邮    箱：18394188838@163.com
 * 版    本：1.0.0
 * 创建日期：2017/8/5 12:06
 * 描    述：使用Android系统提供的定位服务获取当前位置经纬度和城市编码
 * 修订历史：
 * ================================================
 */
public class MainActivity extends AppCompatActivity {
    private TextView mTxtShowLocation;
    private LocationUtils mLocationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxtShowLocation = (TextView) findViewById(R.id.txt_show_location);
        mLocationUtils = new LocationUtils(this);
        mLocationUtils.initLoaction(new OnLoctionResultListener() {
            @Override
            public void onLoctionResultListener(String locationInfo) {
                mTxtShowLocation.setText(locationInfo);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (100 == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "权限已拒绝", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationUtils.removeLocationListener();
    }
}
