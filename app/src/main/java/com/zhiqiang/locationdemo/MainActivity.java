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
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mTxtShowLocation;
    private LocationManager mLocationManager;
    private String mProviderStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxtShowLocation = (TextView) findViewById(R.id.txt_show_location);

        init();

    }

    /**
     * 初始化操作
     */
    private void init() {
        //检测权限
        int location1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int location2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (location1 == PackageManager.PERMISSION_GRANTED && location2 == PackageManager.PERMISSION_GRANTED) {//有权限
            //获取地理位置管理器
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //String networkProvider = LocationManager.NETWORK_PROVIDER;
            //Location lastKnownLocation = locationManager.getLastKnownLocation(networkProvider);
            //获取所有可用的位置提供器
            List<String> providers = mLocationManager.getProviders(true);
            if (providers.contains(LocationManager.GPS_PROVIDER)) {
                //如果是GPS
                mProviderStr = LocationManager.GPS_PROVIDER;
            } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                //如果是Network
                mProviderStr = LocationManager.NETWORK_PROVIDER;
            } else {
                Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
                return;
            }
            //获取Location
            Location location = mLocationManager.getLastKnownLocation(mProviderStr);
            if (location != null) {
                //不为空,显示地理位置经纬度
                showLocation(location);
            }
            //监视地理位置变化
            //参数： 1.位置提供器的类型  2.监听位置变化的时间间隔 3.监听位置变化的距离间隔 4.监听器
            mLocationManager.requestLocationUpdates(mProviderStr, 5000, 100, mLocationListener);

        } else {//没有权限,去申请权限
            String[] locationPers = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, locationPers, 100);
        }


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

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */
    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            showLocation(location);

        }
    };


    /**
     * 获取地理编码,并展示数据
     * 注意：获取地理编码，由于Android系统Geocoder存在严重bug,在获取地理位置的信息时不一定能获取到反地理编码信息，
     * 一般采用谷歌提供的Geocoder api(支持json、xml格式)
     * http://maps.googleapis.com/maps/api/geocode/json?latlng=31.293767,121.42165&sensor=false
     * 或者百度地图服务端提供的Geocoder api(支持json、xml格式) ak需要自己申请服务端的ak
     * http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=31.293767,121.42165&output=json&pois=10&ak=
     *
     * @param location
     */
    private void showLocation(Location location) {

        StringBuffer sb = new StringBuffer();
        sb.append("维度：" + location.getLatitude())
                .append("\n经度：" + location.getLongitude())
                .append("\nprovider：" + location.getProvider())
                .append("\nSpeed：" + location.getSpeed())
                .append("\nTime：" + location.getTime());

        Geocoder gc = new Geocoder(MainActivity.this, Locale.getDefault());
        List<Address> locationList = null;
        try {
            locationList = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (locationList != null && locationList.size() > 0) {
            Address address = locationList.get(0);//得到Address实例
            String countryName = address.getCountryName();//得到国家名称，比如：中国
            sb.append("\ncountryName = " + countryName);
            String locality = address.getLocality();//得到城市名称，比如：北京市
            sb.append("\nlocality = " + locality);
            for (int i = 0; address.getAddressLine(i) != null; i++) {
                String addressLine = address.getAddressLine(i);//得到周边信息，包括街道等，i=0，得到街道名称
                sb.append("\naddressLine : " + addressLine);
            }
        }

        mTxtShowLocation.setText(sb.toString());
        Log.i(TAG, "locationStr = " + sb.toString());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            //移除监听器
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}
