package com.example.sun.test2;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by sun on 2016/7/9.
 */
public class searchActivity extends AppCompatActivity {
     String cityName ;  //城市名

    private static Geocoder geocoder;   //此对象能通过经纬度来获取相应的城市等信息
    private Button shownearbutton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        shownearbutton=(Button)findViewById(R.id.show_near_button);
        LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT)
                    .show();
        }else {
            Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            startActivityForResult(intent, 0); //此为设置完成后返回到获取界面
        }
        geocoder = new Geocoder(this);
        //用于获取Location对象，以及其他
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        //实例化一个LocationManager对象
        locationManager = (LocationManager)this.getSystemService(serviceName);
        //provider的类型
        String provider = LocationManager.NETWORK_PROVIDER;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);   //高精度
        criteria.setAltitudeRequired(false);    //不要求海拔
        criteria.setBearingRequired(false); //不要求方位
        criteria.setCostAllowed(false); //不允许有话费
        criteria.setPowerRequirement(Criteria.POWER_LOW);   //低功耗
        Location location;
        String queryed_name=null;
        //通过最后一次的地理位置来获得Location对象
        try {
           location = locationManager.getLastKnownLocation(provider);
           queryed_name = updateWithNewLocation(location);
        }catch (SecurityException e)
        {
            e.printStackTrace();
        }
        if((queryed_name != null) && (0 != queryed_name.length())){

            cityName = queryed_name;
            TextView textView=(TextView)findViewById(R.id.address);
            textView.setText(cityName);
        }
        shownearbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getInstance().setProvince(User.getInstance().allprovinces.get( User.getInstance().allprovincesname.indexOf(cityName)));
                Intent intent=new Intent(searchActivity.this,ScenicListActivity.class);
                startActivity(intent);
            }
        });
    }
     static String updateWithNewLocation(Location location) {
        String mcityName = "";
        double lat = 0;
        double lng = 0;
        List<Address> addList = null;
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {

            System.out.println("无法获取地理信息");
        }
        try {

            addList = geocoder.getFromLocation(lat, lng, 1);    //解析经纬度

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (addList != null && addList.size() > 0) {
            for (int i = 0; i < addList.size(); i++) {
                Address add = addList.get(i);
                mcityName += add.getAdminArea();

            }
        }
        if(mcityName.length()!=0){

            return mcityName.substring(0, (mcityName.length()-1));
            //return mcityName;
        } else {
            return mcityName;
        }
    }
}

