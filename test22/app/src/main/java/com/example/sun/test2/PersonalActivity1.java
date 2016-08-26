package com.example.sun.test2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sun on 2016/7/10.
 */
public class PersonalActivity1 extends AppCompatActivity {

    private ImageButton setButton=null;
    private TextView userNmae_personal=null;
    private ExpandableListView expandableListView=null;
    private List<String>grouplist=new ArrayList<String>();
    private  ClassifyExpandableListViewAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        setContentView(R.layout.personal_layout);
        grouplist.add("我的收藏");
        grouplist.add("我的游记");
        grouplist.add("游览记录");
        setButton=(ImageButton)findViewById(R.id.set_button);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersonalActivity1.this,settingActivity.class);
                startActivity(intent);
            }
        });
        nicknamegetRunnable r=new nicknamegetRunnable();
        Thread thread=new Thread(r);
        thread.start();
        try {
            thread.join();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        userNmae_personal=(TextView)findViewById(R.id.userNmae_personal);
        userNmae_personal.setText(User.getInstance().getNickname());
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setGroupIndicator(null);
        adapter=new ClassifyExpandableListViewAdapter(this);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
       expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
           @Override
           public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
               return false;
           }
       });

    }
    class ClassifyExpandableListViewAdapter extends BaseExpandableListAdapter {
        private Context context;

        public ClassifyExpandableListViewAdapter(Context context) {
            this.context = context;
        }

        public ClassifyExpandableListViewAdapter() {
            super();
        }


        @Override
        public int getGroupCount() {
            return 3;
        }


        @Override
        public int getChildrenCount(int groupPosition) {
            return 0;
        }


        @Override
        public Object getGroup(int groupPosition) {return null;
        }


        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }


        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }


        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }


        @Override
        public boolean hasStableIds() {
            return true;
        }


        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                GroupHolder groupHolder = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.expandlist_group, null);
                    groupHolder = new GroupHolder();
                    groupHolder.txt = (TextView) convertView.findViewById(R.id.txt);
                    groupHolder.img=(ImageView)convertView.findViewById(R.id.img);
                    convertView.setTag(groupHolder);
                } else {
                    groupHolder = (GroupHolder) convertView.getTag();
                }
                if (!isExpanded) {
                    groupHolder.img.setBackgroundResource(R.drawable.arrow_right);
                } else {
                    groupHolder.img.setBackgroundResource(R.drawable.arrow_down);
                }
            groupHolder.txt.setText(grouplist.get(groupPosition));
            return convertView;
        }


        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            /*ItemHolder itemHolder = null;
            if (convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.expendlist_item, null);
                itemHolder = new ItemHolder();
                itemHolder.txt = (TextView)convertView.findViewById(R.id.title);
                //itemHolder.img = (ImageView)convertView.findViewById(R.id.img);
                convertView.setTag(itemHolder);
            }
            else
            {
                itemHolder = (ItemHolder)convertView.getTag();
            }
            itemHolder.txt.setText(item_list.get(groupPosition).get(childPosition));
            //itemHolder.img.setBackgroundResource(item_list2.get(groupPosition).get(childPosition));*/
            //convertView = LayoutInflater.from(context).inflate(R.layout.expendlist_item, null);

            return convertView;
        }
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
        class GroupHolder
        {
            public TextView txt;
            public ImageView img;
        }
    }
    class collectedplaceget implements Runnable{
        @Override
        public void run() {

        }
    }
    class tourloggetRunnable implements Runnable{
        @Override
        public void run() {

        }
    }
    class nicknamegetRunnable implements Runnable{
        @Override
        public void run() {
            NameValuePair pair1 = new BasicNameValuePair("username", User.getInstance().getUsername());
            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            try {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost("http://"+User.getInstance().getIpaddress()+"/Hawkeye/controller/nicknamequeryController.php");
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
                           User.getInstance().setNickname(jsonObject.getString("nickname"));

                            //Toast.makeText(LoginActivity.this, "code:" + jsonObject.getString("code") + "name:" + names, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    } else
                        Toast.makeText(PersonalActivity1.this, "POST提交失败", Toast.LENGTH_SHORT).show();
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
