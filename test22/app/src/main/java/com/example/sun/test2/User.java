package com.example.sun.test2;

import android.graphics.Bitmap;
import android.net.IpPrefix;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
//user单例
public class User {
    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static User userinstance;
    //构造方法私有化
    private User(){}
    //实例化一次
    private String password=new String("");
    private String nickname=new String("");
    private String username=new String("");
    private String session=new  String("");
    private Province province=null;
    private Place place=null;
    private Route route=null;
    private Integer routepointer=0;
    private Integer commentitemnumber=0;
    private String  ipaddress;
    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public Integer getCommentitemnumber() {
        return commentitemnumber;
    }

    public void setCommentitemnumber(Integer commentitemnumber) {
        this.commentitemnumber = commentitemnumber;
    }

    public List<Province> Hotprovinces= new ArrayList<Province>();
    public List<Place> Hotplaces=new ArrayList<Place>();
    public List<TextView>ptextviews=new ArrayList<TextView>();
    public List<TextView>stextviews=new ArrayList<TextView>();
    public List<Place>currentplaces=new ArrayList<Place>();
    public List<Bitmap>bitmaps=new ArrayList<Bitmap>();
    public List<Route>currentroutes=new ArrayList<Route>();
    public List<Province>allprovinces=new ArrayList<Province>();
    public List<String>allprovincesname=new ArrayList<String>();
    public Integer getRoutepointer() {
        return routepointer;
    }

    public void setRoutepointer(Integer routepointer) {
        this.routepointer = routepointer;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public synchronized static User getInstance()
    {
        if (null == userinstance) {
            userinstance = new User();
        }
        return userinstance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
