package com.example.sun.test2;
/**
 * Created by sun on 2016/7/3.
 */

import android.content.Context;
        import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
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
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;
import me.drakeet.materialdialog.MaterialDialog;

public class RouteConceptActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {
    MaterialDialog mMaterialDialog =null;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 10;
    //the same as the numItems of  class FakePageAdapter and  itemNumber of  class MaterialUpConceptFakePage,that is the number of carditems in each framepager,which should be load danymicly
    private boolean mIsAvatarShown = true;
    private ImageView mProfileImage;
    private int mMaxScrollSize;
    private FloatingActionButton lastroute=null;
    private FloatingActionButton nextroute=null;
    ImageView routeimage=null;
    TextView routename=null;
    CircleButton playButton=null;
    public   static Integer pointer=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
         /*for (int i=0;i<10;i++)
         {
             Route r=new Route();
             r.setIntroduce("introduce" + i);
             r.setRoutename("route" + i);
             List<Comment>comments=new ArrayList<Comment>();
             for (int j=0;j<3;j++)
             {
                 Comment comment=new Comment();
                 comment.setCommentor(("comment or"+i+j));
                 comment.setContent("content" + i + j);
                 comment.setTime("212341561");
                 comments.add(comment);
             }
             r.setCommentnubmer(3);
             User.getInstance().setCommentitemnumber(3);
             r.setComments(comments);
             User.getInstance().currentroutes.add(r);
         }*/
        routelistgetRunnable r=new routelistgetRunnable();
        Thread t=new Thread(r);
        t.start();
        try{
            t.join();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        if(User.getInstance().currentroutes.size()!=0) {
            final TabLayout tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);
            final ViewPager viewPager = (ViewPager) findViewById(R.id.materialup_viewpager);
            AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.materialup_appbar);
            lastroute = (FloatingActionButton) findViewById(R.id.last_route_button);
            nextroute = (FloatingActionButton) findViewById(R.id.next_route_button);
            routeimage = (ImageView) findViewById(R.id.route_background);
            routename = (TextView) findViewById(R.id.route_name);
            playButton = (CircleButton) findViewById(R.id.route_setoff_button);
            appbarLayout.addOnOffsetChangedListener(this);
            mMaxScrollSize = appbarLayout.getTotalScrollRange();
            User.getInstance().setRoutepointer(pointer);
            routename.setText(User.getInstance().currentroutes.get(pointer).getRoutename());
            picturegetRunnable r3 = new picturegetRunnable();
            r3.setPath("http://" + User.getInstance().getIpaddress() + "/Hawkeye/resource/images/route/" + User.getInstance().currentroutes.get(pointer).getPicturelink());
            r3.setName(User.getInstance().currentroutes.get(pointer).getRoutename());
            Thread t3 = new Thread(r3);
            t3.start();
            try {
                t3.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            routeimage.setImageBitmap(r3.getPic());
            routegetRunnable r1 = new routegetRunnable();
            r1.setRouteid(User.getInstance().currentroutes.get(pointer).getRouteid());
            r1.setWhich(pointer);
            Thread t1 = new Thread(r1);
            t1.start();
            try {
                t1.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
            tabLayout.setupWithViewPager(viewPager);
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RouteConceptActivity.this, cardBoardVideoActivity.class);
                    startActivity(intent);
                }
            });
            lastroute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pointer <= 0) {
                        Snackbar.make(v, "已经没有上一条了哦", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        pointer = 0;
                    } else {
                        pointer = pointer - 1;
                        User.getInstance().setRoutepointer(pointer);
                        routename.setText(User.getInstance().currentroutes.get(pointer).getRoutename());
                        picturegetRunnable r = new picturegetRunnable();
                        r.setPath("http://" + User.getInstance().getIpaddress() + "/Hawkeye/resource/images/route/" + User.getInstance().currentroutes.get(pointer).getPicturelink());
                        r.setName(User.getInstance().currentroutes.get(pointer).getRoutename());
                        Thread t = new Thread(r);
                        t.start();
                        try {
                            t.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        routeimage.setImageBitmap(r.getPic());
                        routegetRunnable r1 = new routegetRunnable();
                        r1.setRouteid(User.getInstance().currentroutes.get(pointer).getRouteid());
                        r1.setWhich(pointer);
                        Thread t1 = new Thread(r1);
                        t1.start();
                        try {
                            t1.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
                        tabLayout.setupWithViewPager(viewPager);

                    }
                }
            });
            nextroute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pointer >= (User.getInstance().currentroutes.size() - 1)) {
                        Snackbar.make(v, "已经没有下一条了哦", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        pointer = User.getInstance().currentroutes.size() - 1;
                    } else {
                        pointer = pointer + 1;
                        User.getInstance().setRoutepointer(pointer);
                        routename.setText(User.getInstance().currentroutes.get(pointer).getRoutename());
                        picturegetRunnable r = new picturegetRunnable();
                        r.setPath("http://" + User.getInstance().getIpaddress() + "/Hawkeye/resource/images/route/" + User.getInstance().currentroutes.get(pointer).getPicturelink());
                        r.setName(User.getInstance().currentroutes.get(pointer).getRoutename());
                        Thread t = new Thread(r);
                        t.start();
                        try {
                            t.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        routeimage.setImageBitmap(r.getPic());
                        routegetRunnable r1 = new routegetRunnable();
                        r1.setRouteid(User.getInstance().currentroutes.get(pointer).getRouteid());
                        r1.setWhich(pointer);
                        Thread t1 = new Thread(r1);
                        t1.start();
                        try {
                            t1.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
                        tabLayout.setupWithViewPager(viewPager);
                    }
                }
            });
        }else {
            mMaterialDialog = new MaterialDialog(this)
                    .setTitle("提示")
                    .setMessage("本景点暂无路线")
                    .setPositiveButton("好的", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                            //Intent intent=new Intent(signupActivity.this,loginActivity.class);
                            //signupActivity.this.startActivity(intent);
                            RouteConceptActivity.this.finish();
                        }
                    });
            mMaterialDialog.show();
        }
    }

    public static void start(Context c) {
        c.startActivity(new Intent(c, RouteConceptActivity .class));
    }

    //动画效果，可不管
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();
        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;
        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
        }
        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;
        }
    }

    class TabsAdapter extends FragmentPagerAdapter {
        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;//tab 的数量,可不改
        }
        //获取每个tabPage
        @Override
        public Fragment getItem(int i) {
            switch (i) {

                case 0:
                  return RouteConceptFakePage.newInstance();
                case 1:
                   return ReviewConceptFakePage.newInstance();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return "介绍";
                case 1: return "评论";
            }
            return "";
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            User.getInstance().currentroutes.clear();
            //scenicsList.clear();
            RouteConceptActivity.this.finish();
            return true;
        }else{
            return super.onKeyDown(keyCode, event);}
    }
    class routelistgetRunnable implements Runnable
    {
        @Override
        public void run() {
            NameValuePair pair1 = new BasicNameValuePair("placeid", User.getInstance().getPlace().getPlaceid());
            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            try {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost("http://"+User.getInstance().getIpaddress()+"/Hawkeye/controller/routelistqueryController.php");
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
                                    Route route=new Route();
                                    route.setRouteid(jsono.getString("routeid"));
                                    route.setRoutename(jsono.getString("routename"));
                                    route.setPicturelink(jsono.getString("picturelink"));
                                    route.setIntroduce(jsono.getString("introduce"));
                                    User.getInstance().currentroutes.add(route);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else
                        Toast.makeText(RouteConceptActivity.this, "POST提交失败", Toast.LENGTH_SHORT).show();
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
    class routegetRunnable implements Runnable
    {
        String Routeid;
        Integer which;

        public String getRouteid() {
            return Routeid;
        }

        public void setRouteid(String routeid) {
            Routeid = routeid;
        }

        public Integer getWhich() {
            return which;
        }

        public void setWhich(Integer which) {
            this.which = which;
        }

        @Override
        public void run() {
            NameValuePair pair1 = new BasicNameValuePair("routeid",Routeid);
            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            try {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost("http://"+User.getInstance().getIpaddress()+"/Hawkeye/controller/routequeryController.php");
                request.setEntity(requestHttpEntity);
                String strResult=new String("");
                JSONObject jsonObject = null;
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
                            jsonObject=getJSON(strResult);
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
                            User.getInstance().currentroutes.get(which).setVideolink(jsonObject.getString("videolink"));
                            User.getInstance().currentroutes.get(which).setIntroduce(jsonObject.getString("introduce"));
                            String comments=jsonObject.getString("comments");
                            Log.d("comment", comments);

                            JSONArray commentsa=new JSONArray(comments);
                            List<Comment> comment=new ArrayList<Comment>();
                            int i;
                            for(i=0;i<commentsa.length();i++)
                            {
                                JSONObject ja=(JSONObject)commentsa.get(i);
                                Comment c=new Comment();
                                c.setCommentor(ja.getString("commentor"));
                                c.setTime(ja.getString("time"));
                                c.setContent(ja.getString("content"));
                                comment.add(c);
                            }
                            User.getInstance().currentroutes.get(which).setCommentnubmer(i);
                            User.getInstance().setCommentitemnumber(i);
                            User.getInstance().currentroutes.get(which).setComments(comment);

                            /**得到data这个key**/
                            if(!strResult.equals("")) {
                                //jsonObject.getString("videolink");
                                //jsonObject.getString("");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else
                        Toast.makeText(RouteConceptActivity.this, "POST提交失败", Toast.LENGTH_SHORT).show();
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
    public JSONObject getJSON(String sb) throws JSONException {
        return new JSONObject(sb);
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
}
