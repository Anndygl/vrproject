package com.example.sun.test2;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/*
*景区简介列表
 */
public class ScenicListActivity extends AppCompatActivity {
        private RecyclerView recyclerView;
        private List<Scenic> scenicsList;
        private ScenicListRecyViewAdapter adapter;
        private Button sortButton=null;
        private Button searchButton=null;
        AlertDialog.Builder builder2 =null;
        //private MaterialDialog materialDialog;


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenic_list);
        sortButton=(Button)findViewById(R.id.button_sort);
        searchButton=(Button)findViewById(R.id.button_search);
        //设置底部buttons栏的背景透明度
        View v = findViewById(R.id.altersort);
        v.getBackground().setAlpha(200);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        setRecyclerView();
        //initPersonData();
        adapter=new ScenicListRecyViewAdapter(scenicsList, ScenicListActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        sortButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                builder2=new  AlertDialog.Builder(v.getContext());
                builder2.setTitle("排序方式");
                final String [] d=new String[2];
                d[0]="按热度排序";
                d[1]="按评分排序";
                builder2.setSingleChoiceItems(d, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ScenicListActivity.this,
                                "选择了--->>" + d[which], Toast.LENGTH_SHORT)
                                .show();
                        dialog.dismiss();
                    }
                });
                builder2.show();
            }
        });
       searchButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent =new Intent(ScenicListActivity.this,searchActivity.class);
               startActivity(intent);
           }
       });
}
    void setRecyclerView()
    {
        placelistgetRunnable runnable=new placelistgetRunnable();
        Thread t=new Thread(runnable);
         t.start();
        try{
            t.join();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        for(int i=0;i<User.getInstance().currentplaces.size();i++)
        {
            String path= Environment.getExternalStorageDirectory()+"/test22/"+User.getInstance().currentplaces.get(i).getPlacename()+".jpg";
            File mFile=new File(path);
            //若该文件存在
            if (mFile.exists()) {
                long   time=mFile.lastModified();
                Log.d("filetime",""+time);
                Log.d("filetime",""+System.currentTimeMillis());
                if(System.currentTimeMillis()-time<604800000) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    User.getInstance().bitmaps.add(bitmap);
                }
                else
                {
                    picturegetRunnable r1 = new picturegetRunnable();
                    r1.setPath("http://"+User.getInstance().getIpaddress()+"/Hawkeye/resource/images/place/"+User.getInstance().currentplaces.get(i).getPicturelink());
                    r1.setName(User.getInstance().currentplaces.get(i).getPlacename());
                    Thread t1 = new Thread(r1);
                    t1.start();
                    try {
                        t1.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    User.getInstance().bitmaps.add(r1.getPic());
                }
            }
            else {
                picturegetRunnable r1 = new picturegetRunnable();
                r1.setPath("http://"+User.getInstance().getIpaddress()+"/Hawkeye/resource/images/place/"+User.getInstance().currentplaces.get(i).getPicturelink());
                r1.setName(User.getInstance().currentplaces.get(i).getPlacename());
                Thread t1 = new Thread(r1);
                t1.start();
                try {
                    t1.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                User.getInstance().bitmaps.add(r1.getPic());

            }
        }
        adapter=new ScenicListRecyViewAdapter(scenicsList, ScenicListActivity.this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

        private void initPersonData() {
        scenicsList =new ArrayList<>();
        //動態添加Scenic
        scenicsList.add(new Scenic(getString(R.string.scenic_one_title),getString(R.string.scenic_one_desc),R.mipmap.scenic_one));
        scenicsList.add(new Scenic(getString(R.string.scenic_two_title), getString(R.string.scenic_two_desc), R.mipmap.scenic_two));
        scenicsList.add(new Scenic(getString(R.string.scenic_three_title), getString(R.string.scenic_three_desc), R.mipmap.scenic_three));
        scenicsList.add(new Scenic(getString(R.string.scenic_four_title), getString(R.string.scenic_four_desc), R.mipmap.scenic_four));
        }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            User.getInstance().currentplaces.clear();
            //scenicsList.clear();
            User.getInstance().bitmaps.clear();
            ScenicListActivity.this.finish();
            return true;
        }else{
            return super.onKeyDown(keyCode, event);}
    }
        //void sortClick(View v){
                //materialDialog =new MaterialDialog(this).setContentView(R.layout.sort_layout);
                //materialDialog.show();

        //}
        //void searchClick(View v){

                //Intent intent=new Intent(this,searchActivity.class);
                //this.startActivity(intent);

        //}
        //void cancleOnclick(View v){
                //materialDialog.dismiss();
        //}
        //void confirmOnclick(View v){
                //materialDialog.dismiss();
        //}

        class placelistgetRunnable implements Runnable{
                @Override
                public void run() {
                        NameValuePair pair1 = new BasicNameValuePair("provinceid", User.getInstance().getProvince().getProvinceid());
                        Log.d("provinceid",User.getInstance().getProvince().getProvinceid());
                        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
                        pairList.add(pair1);
                        try {
                                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
                                HttpClient client = new DefaultHttpClient();
                                HttpPost request = new HttpPost("http://"+User.getInstance().getIpaddress()+"/Hawkeye/controller/placelistqueryController.php");
                                request.setEntity(requestHttpEntity);
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
                                                                       Place p=new Place();
                                                                    p.setPlaceid(jsono.getString("placeid"));
                                                                    p.setPlacename(jsono.getString("placename"));
                                                                    p.setPicturelink(jsono.getString("picturelink"));
                                                                    p.setClicknumber(jsono.getString("clicknumber"));
                                                                    p.setIntroduce(jsono.getString("introduce"));
                                                                    p.setScore(jsono.getString("score"));
                                                                    User.getInstance().currentplaces.add(p);
                                                                    Scenic s=new Scenic();
                                                                    s.setTitle(jsono.getString("placename"));
                                                                    s.setDesc(jsono.getString("introduce"));
                                                                }
                                                        }
                                                } catch (JSONException e) {
                                                        // TODO Auto-generated catch block
                                                        e.printStackTrace();
                                                }
                                        } else
                                                Toast.makeText(ScenicListActivity.this, "POST提交失败", Toast.LENGTH_SHORT).show();
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
    public JSONObject getJSON(String sb) throws JSONException {
        return new JSONObject(sb);
    }
}
