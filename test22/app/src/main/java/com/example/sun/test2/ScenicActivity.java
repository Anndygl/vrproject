package com.example.sun.test2;
//逻辑：动态加载景区图片、相关介绍等内容
import android.content.Context;
        import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
        import android.support.v4.view.ViewCompat;
        import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ScenicActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {
    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private View mFab;           //浮动button
    private int mMaxScrollSize;
    private boolean mIsImageHidden;//动画
    private ImageView placeimage=null;
    private TextView  placename=null;
    private TextView  placedesc=null;
    private Button collectButton=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scenic_detail2);
        placeimage=(ImageView)findViewById(R.id.scenic_bcg);
        placename=(TextView)findViewById(R.id.scenic_name);
        placedesc=(TextView)findViewById(R.id.scenic_desc);
        collectButton=(Button)findViewById(R.id.button_collect);

        placename.setText(User.getInstance().getPlace().getPlacename());
        placeinfogetRunnable r=new placeinfogetRunnable();
        Thread t=new Thread(r);
        t.start();
        try {
            t.join();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        placedesc.setText(User.getInstance().getPlace().getIntroduce());
        String path= Environment.getExternalStorageDirectory()+"/test22/"+User.getInstance().getPlace().getPlacename();
        File mFile=new File(path);
        //若该文件存在
        if (mFile.exists()) {
            long   time=mFile.lastModified();
            Log.d("filetime",""+time);
            Log.d("filetime",""+System.currentTimeMillis());
            if(System.currentTimeMillis()-time<604800000) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                placeimage.setImageBitmap(bitmap);
            }
            else
            {
                picturegetRunnable r1 = new picturegetRunnable();
                r1.setPath("http://"+User.getInstance().getIpaddress()+"/Hawkeye/resource/images/place/"+User.getInstance().getPlace().getPicturelink());
                r1.setName(User.getInstance().getPlace().getPlacename());
                Thread t1 = new Thread(r1);
                t1.start();
                try {
                    t1.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                placeimage.setImageBitmap(r1.getPic());
            }
        }
        else {
            picturegetRunnable r1 = new picturegetRunnable();
            r1.setPath("http://"+User.getInstance().getIpaddress()+"/Hawkeye/resource/images/place/"+User.getInstance().getPlace().getPicturelink());
            r1.setName(User.getInstance().getPlace().getPlacename());
            Thread t1 = new Thread(r1);
            t1.start();
            try {
                t1.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            placeimage.setImageBitmap(r1.getPic());
        }
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placecollectRunnable r=new placecollectRunnable();
                Thread t=new Thread(r);
                t.start();
                try{
                    t.join();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                if(r.result.equals("true"))
                {
                    Toast.makeText(ScenicActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(ScenicActivity.this, "收藏失败:"+r.feedback, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //设置底部buttons栏的背景透明度
        View v = findViewById(R.id.alterchoice);
        v.getBackground().setAlpha(200);
        //动画效果
        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.flexible_example_appbar);
        appbar.addOnOffsetChangedListener(this);
        //实例化悬浮button
        mFab = findViewById(R.id.flexible_example_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ScenicActivity.this,RouteConceptActivity.class);
                startActivity(intent);
            }
        });
    }
    //button route
    public void routeClick(View view){
        Intent intent=new Intent(this,RouteConceptActivity.class);
        this.startActivity(intent);
    }
    //button help
    public void helpClick(View view){
        Intent intent=new Intent(this,UserGuide.class);
        this.startActivity(intent);
    }
    //实现动画效果
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(i)) * 100
                / mMaxScrollSize;
        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true;
                ViewCompat.animate(mFab).scaleY(0).scaleX(0).start();
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;
                ViewCompat.animate(mFab).scaleY(1).scaleX(1).start();
            }
        }
    }
    public static void start(Context c)
    {
        c.startActivity(new Intent(c, ScenicActivity.class));
    }
    class placecollectRunnable implements Runnable{
        public String result;
        public String feedback;

        public String getResult() {
            return result;
        }
        public String getFeedback() {
            return feedback;
        }
        @Override
        public void run() {
            NameValuePair pair1 = new BasicNameValuePair("placeid", User.getInstance().getPlace().getPlaceid());
            NameValuePair pair2 = new BasicNameValuePair("userid", User.getInstance().getUserid());
            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            pairList.add(pair2);
            try {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost("http://"+User.getInstance().getIpaddress()+"/Hawkeye/controller/placecollectController.php");
                request.setEntity(requestHttpEntity);
                String strResult;
                JSONObject jsonObject = null;
                // replace with your url
                request.setHeader("Cookie", "language0=0; "+User.getInstance().getSession());
                HttpResponse response;
                try {
                    response = client.execute(request);
                    //Log.d("postresponse",response);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        try {
                            /**读取服务器返回过来的json字符串数据**/
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
                            result = jsonObject.getString("result");
                            if(result.equals("false"))
                            {
                                feedback=jsonObject.getString("feedback");
                                //Looper.prepare();
                                Log.d("loginfeedback", feedback);
                                //Toast.makeText(LoginActivity.this, feedback, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {

                            }
                            //User.getInstance().getPlace().setIntroduce(jsonObject.getString("introduce"));
                            //Toast.makeText(LoginActivity.this, "code:" + jsonObject.getString("code") + "name:" + names, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    } else
                        Toast.makeText(ScenicActivity.this, "POST提交失败", Toast.LENGTH_SHORT).show();
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
    class placeinfogetRunnable implements Runnable{
        @Override
        public void run() {
            NameValuePair pair1 = new BasicNameValuePair("placeid", User.getInstance().getPlace().getPlaceid());
            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            try {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost("http://"+User.getInstance().getIpaddress()+"/Hawkeye/controller/placequeryController.php");
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
                            Header it = response.getFirstHeader("Set-Cookie");
                            String session = it.toString();
                            String[] heads = session.split(";");
                            String[] sessions = heads[0].split(":");
                            session = sessions[1];
                            User.getInstance().setSession(session);
                            /**读取服务器返回过来的json字符串数据**/
                            strResult = EntityUtils.toString(response.getEntity());
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
                            User.getInstance().getPlace().setIntroduce(jsonObject.getString("introduce"));
                            //Toast.makeText(LoginActivity.this, "code:" + jsonObject.getString("code") + "name:" + names, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    } else
                        Toast.makeText(ScenicActivity.this, "POST提交失败", Toast.LENGTH_SHORT).show();
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
                    File file=new File(f,pname);
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
