package com.example.sun.test2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by LCM on 2016/7/10.
 */
public class indexActivity  extends AppCompatActivity {
    Toolbar toolbar =null;
    AlertDialog.Builder builder2 =null;
    private ImageButton to_personal_button=null;
    private ImageButton search_buton=null;
    private CircleButton go_tour_button=null;
    private List<Province>allprovinces=new ArrayList<Province>();
    private String []allprovincesname=new String[34];
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        setContentView(R.layout.index_layout);
        to_personal_button=(ImageButton)findViewById(R.id.to_personal_button);
        search_buton=(ImageButton)findViewById(R.id.search_buton);
        go_tour_button=(CircleButton)findViewById(R.id.go_tour_button);
        /*for (int i=0;i<10;i++)
        {
            Province province=new Province();
            province.setProvincename("省份"+i);
            User.getInstance().Hotprovinces.add(province);
        }
        for (int j=0;j<10;j++)
        {
            Place place=new Place();
            place.setPlacename("景区"+j);
            User.getInstance().Hotplaces.add(place);
        }*/
        useridgetRunnable runnable=new useridgetRunnable();
        Thread thread=new Thread(runnable);
        thread.start();
        try
        {
           thread.join();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        hotplacegetRunnable r=new hotplacegetRunnable();
        Thread t1=new Thread(r);
        t1.start();
        try
        {
            t1.join();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        hotprovincegetRunnable r1=new hotprovincegetRunnable();
        Thread t2=new Thread(r1);
        t2.start();
        try
        {
            t2.join();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        allprovincegetRunnable r4=new allprovincegetRunnable();
        Thread t4=new Thread(r4);
        t4.start();
        try
        {
            t4.join();
        }catch (Exception e){
            e.printStackTrace();
        }

        for(int k=0;k<User.getInstance().Hotprovinces.size();k++) {
            final int p=k;
            TextView textView=new TextView(this);
            textView.setClickable(true);
            textView.setText(User.getInstance().Hotprovinces.get(k).getProvincename());//動態設置名字
            picturegetRunnable r0=new picturegetRunnable();
            r0.setName(User.getInstance().Hotprovinces.get(k).getProvincename());
            r0.setPath("http://"+User.getInstance().getIpaddress()+"/Hawkeye/resource/images/province/"+User.getInstance().Hotprovinces.get(k).getPicturelink());
            //r0.setPath("http://"+User.getInstance().getIpaddress()+"/Hawkeye/resource/images/province/山西.jpg");
            Thread t=new Thread(r0);
            t.start();
            try{
                t.join();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            Bitmap b=r0.getPic();
            textView.setBackground(new BitmapDrawable(b));
            //textView.setBackgroundResource(R.mipmap.scenic_three);//動態設置背景圖片
            textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(35);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(indexActivity.this,
                            "选择了--->>" + User.getInstance().Hotprovinces.get(p).getProvincename(), Toast.LENGTH_SHORT)
                            .show();
                    User.getInstance().setProvince(User.getInstance().Hotprovinces.get(p));
                    Intent intent=new Intent(indexActivity.this,ScenicListActivity.class);
                    startActivity(intent);
                }
            });
            User.getInstance().ptextviews.add(textView);
        }
        for(int i=0;i<User.getInstance().Hotplaces.size();i++) {
            final int s=i;
            TextView textView=new TextView(this);
            textView.setClickable(true);
            textView.setText(User.getInstance().Hotplaces.get(i).getPlacename());//動態設置名字
            picturegetRunnable r0=new picturegetRunnable();
            r0.setName(User.getInstance().Hotplaces.get(i).getPlacename());
            r0.setPath("http://"+User.getInstance().getIpaddress()+"/Hawkeye/resource/images/place/"+User.getInstance().Hotplaces.get(i).getPicturelink());
            //r0.setPath("http://"+User.getInstance().getIpaddress()+"/Hawkeye/resource/images/province/山西.jpg");
            Thread t=new Thread(r0);
            t.start();
            try{
                t.join();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            Bitmap b=r0.getPic();
            textView.setBackground(new BitmapDrawable(b));
            //textView.setBackgroundResource(R.mipmap.scenic_three);//動態設置背景圖片
            textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(35);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(indexActivity.this,
                            "选择了--->>" + User.getInstance().Hotplaces.get(s).getPlacename(), Toast.LENGTH_SHORT)
                            .show();
                    User.getInstance().setPlace(User.getInstance().Hotplaces.get(s));
                    Intent intent=new Intent(indexActivity.this,ScenicActivity.class);
                    startActivity(intent);
                }
            });
            User.getInstance().stextviews.add(textView);
        }
        Fragment demoFragment = Fragment.instantiate(this, ScenicChangeFragment.class.getName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, demoFragment);
        fragmentTransaction.commit();
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        int count = getSupportFragmentManager().getBackStackEntryCount();
                        ActionBar actionbar = getSupportActionBar();
                        if (actionbar != null) {
                            actionbar.setDisplayHomeAsUpEnabled(count > 0);
                            actionbar.setDisplayShowHomeEnabled(count > 0);
                        }
                    }
                });

        Fragment provinceFragment = Fragment.instantiate(this, ProvinceChangeFragment.class.getName());
        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container2, provinceFragment);
        fragmentTransaction2.commit();
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        int count = getSupportFragmentManager().getBackStackEntryCount();
                        ActionBar actionbar = getSupportActionBar();
                        if (actionbar != null) {
                            actionbar.setDisplayHomeAsUpEnabled(count > 0);
                            actionbar.setDisplayShowHomeEnabled(count > 0);
                        }
                    }
                });
        to_personal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(indexActivity.this, PersonalActivity1.class);
                startActivity(intent);
            }
        });
        search_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(indexActivity.this, searchActivity.class);
                startActivity(intent);
            }
        });
        go_tour_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*for (int i=0;i<34;i++)
                {
                    allprovincesname[i]="省份"+i+1;
                    Province p=new Province();
                    p.setProvincename("省份" + i + 1);
                    p.setProvinceid("" + i + 1);
                    allprovinces.add(p);
                }*/

                builder2=new  AlertDialog.Builder(v.getContext());
                builder2.setTitle("省份选择");
                builder2.setSingleChoiceItems(allprovincesname, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(indexActivity.this,
                                "选择了--->>" + allprovincesname[which], Toast.LENGTH_SHORT)
                                .show();
                        User.getInstance().setProvince(allprovinces.get(which));
                        Intent intent=new Intent(indexActivity.this,ScenicListActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
               /*builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });*/
                builder2.show();
        }
        });

    }
     class hotplacegetRunnable implements Runnable{
     @Override
     public void run() {
         try {
             HttpClient client = new DefaultHttpClient();
             HttpPost request = new HttpPost("http://"+User.getInstance().getIpaddress()+"/Hawkeye/controller/hotplacequeryController.php");
             String strResult=new String("");
             JSONArray jarr=null;
             // replace with your url
             request.setHeader("Cookie",User.getInstance().getSession());
             HttpResponse response;
             try {
                 response = client.execute(request);
                 //Log.d("postresponse",response);
                 if (response.getStatusLine().getStatusCode() == 200) {
                     try {
                         /**读取服务器返回过来的json字符串数据**/
                         strResult = EntityUtils.toString(response.getEntity());
                         Log.d("strResult", strResult);
                         jarr = new JSONArray(strResult);
                         //jsonObject = getJSON(strResult);
                     } catch (IllegalStateException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     } catch (IOException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     } catch (JSONException e1) {
                         // TODO Auto-generated catch block
                         e1.printStackTrace();
                     }
                     try {
                         /**
                          * jsonObject.getString("code") 取出code
                          * 比如这里返回的json 字符串为 [code:0,msg:"ok",data:[list:{"name":1},{"name":2}]]
                          * **/
                         /**得到data这个key**/
                         //String groups=jsonObject.getString("groups");
                         //JSONObject jGroups=new JSONObject(groups);
                         if(!strResult.equals("")) {
                             for (int i = 0; i < jarr.length(); i++) {
                                 /** **/
                                 JSONObject jsono = (JSONObject) jarr.get(i);
                                 /**取出list下的name的值 **/
                                 Place s=new Place();
                                 s.setPlacename(jsono.getString("placename"));
                                 s.setPlaceid(jsono.getString("placeid"));
                                 s.setPicturelink(jsono.getString("picturelink"));
                                 User.getInstance().Hotplaces.add(s);
                             }
                         }
                     } catch (JSONException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     }
                 } else
                     Toast.makeText(indexActivity.this, "POST提交失败", Toast.LENGTH_SHORT).show();
             } catch (ClientProtocolException e) {
                 // TODO Auto-generated catch block
                 Log.d("wrong:", "wrong");
                 //e.printStackTrace();
             } catch (IOException e) {
                 // TODO Auto-generated catch block
                 Log.d("wrong2:", "wrong2");
                 e.printStackTrace();
             }

         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 }
    class hotprovincegetRunnable implements Runnable{
        @Override
        public void run() {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost("http://"+User.getInstance().getIpaddress()+"/Hawkeye/controller/hotprovincequeryController.php");
                String strResult=new String("");
                JSONArray jarr=null;
                // replace with your url
                request.setHeader("Cookie",User.getInstance().getSession());
                HttpResponse response;
                try {
                    response = client.execute(request);
                    //Log.d("postresponse",response);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        try {
                            /**读取服务器返回过来的json字符串数据**/
                            strResult = EntityUtils.toString(response.getEntity());
                            Log.d("strResult", strResult);
                            jarr = new JSONArray(strResult);
                            //jsonObject = getJSON(strResult);
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (JSONException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        try {
                            /**
                             * jsonObject.getString("code") 取出code
                             * 比如这里返回的json 字符串为 [code:0,msg:"ok",data:[list:{"name":1},{"name":2}]]
                             * **/
                            /**得到data这个key**/
                            //String groups=jsonObject.getString("groups");
                            //JSONObject jGroups=new JSONObject(groups);
                            if(!strResult.equals("")) {
                                for (int i = 0; i < jarr.length(); i++) {
                                    /** **/
                                    JSONObject jsono = (JSONObject) jarr.get(i);
                                    /**取出list下的name的值 **/
                                    Province s=new Province();
                                    s.setProvincename(jsono.getString("provincename"));
                                    s.setProvinceid(jsono.getString("provinceid"));
                                    s.setPicturelink(jsono.getString("picturelink"));
                                    User.getInstance().Hotprovinces.add(s);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else
                        Toast.makeText(indexActivity.this, "POST提交失败", Toast.LENGTH_SHORT).show();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    Log.d("wrong:", "wrong");
                    //e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.d("wrong2:", "wrong2");
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class allprovincegetRunnable implements Runnable{

        @Override
        public void run() {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost("http://"+User.getInstance().getIpaddress()+"/Hawkeye/controller/allprovincequeryController.php");
                String strResult=new String("");
                JSONArray jarr=null;
                // replace with your url
                request.setHeader("Cookie",User.getInstance().getSession());
                HttpResponse response;
                try {
                    response = client.execute(request);
                    //Log.d("postresponse",response);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        try {
                            /**读取服务器返回过来的json字符串数据**/
                            strResult = EntityUtils.toString(response.getEntity());
                            Log.d("strResult", strResult);
                            jarr = new JSONArray(strResult);
                            //jsonObject = getJSON(strResult);
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (JSONException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        try {
                            /**
                             * jsonObject.getString("code") 取出code
                             * 比如这里返回的json 字符串为 [code:0,msg:"ok",data:[list:{"name":1},{"name":2}]]
                             * **/

                            /**得到data这个key**/
                            //String groups=jsonObject.getString("groups");
                            //JSONObject jGroups=new JSONObject(groups);
                            if(!strResult.equals("")) {
                                for (int i = 0; i < jarr.length(); i++) {

                                    /** **/
                                    JSONObject jsono = (JSONObject) jarr.get(i);
                                    /**取出list下的name的值 **/
                                    Province p =new Province();
                                    p.setProvinceid(jsono.getString("provinceid"));
                                    p.setProvincename(jsono.getString("provincename"));
                                    allprovinces.add(p);
                                    User.getInstance().allprovinces.add(p);
                                    allprovincesname[i]=jsono.getString("provincename");
                                    User.getInstance().allprovincesname.add(jsono.getString("provincename"));
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else
                        Toast.makeText(indexActivity.this, "POST提交失败", Toast.LENGTH_SHORT).show();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    Log.d("wrong:", "wrong");
                    //e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.d("wrong2:", "wrong2");
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class picturegetRunnable implements Runnable{
        Bitmap pic;
        public Bitmap getPic() {
            return pic;
        }
        String pname;
        String path;
        public void setName(String name) {this.pname = name;}
        public void setPath(String path) {this.path = path;}
        @Override
        public void run(){
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(path);
                // replace with your url
                request.setHeader("Cookie",User.getInstance().getSession());
                HttpResponse response;
                try {
                    response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    pic = BitmapFactory.decodeStream(is);
                    Log.e("tag", "保存图片");
                    File f = new File(Environment.getExternalStorageDirectory()+"/test22/");
                    if(!f.exists())
                        f.mkdir();
                    File file=new File(f,pname+".jpg");
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    pic.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    try {
                        fOut.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.d("wrong:", "wrong");
                    //e.printStackTrace();
                    //Your code goes here
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    class useridgetRunnable implements Runnable{
        @Override
        public void run() {
            NameValuePair pair1 = new BasicNameValuePair("username", User.getInstance().getUsername());
            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            try {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost("http://"+User.getInstance().getIpaddress()+"/Hawkeye/controller/useridqueryController.php");
                request.setEntity(requestHttpEntity);
                String strResult;
                JSONObject jsonObject = null;
                // replace with your url

                HttpResponse response;
                try {
                    response = client.execute(request);
                    //Log.d("postresponse",response);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        try {
                            strResult = EntityUtils.toString(response.getEntity());
                            Log.d("strResult",strResult);
                            jsonObject = getJSON(strResult);
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }catch (JSONException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        String names = "";

                        try {
                            /**
                             * jsonObject.getString("code") 取出code
                             * 比如这里返回的json 字符串为 [code:0,msg:"ok",data:[list:{"name":1},{"name":2}]]
                             * **/

                            /**得到data这个key**/
                            User.getInstance().setUserid(jsonObject.getString("userid"));

                            //Toast.makeText(LoginActivity.this, "code:" + jsonObject.getString("code") + "name:" + names, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    } else
                        Toast.makeText(indexActivity.this, "POST提交失败", Toast.LENGTH_SHORT).show();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    Log.d("wrong:", "wrong");
                    //e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.d("wrong2:", "wrong2");
                    e.printStackTrace();
                }
                //Your code goes here
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public JSONObject getJSON(String sb) throws JSONException {
        return new JSONObject(sb);
    }
}