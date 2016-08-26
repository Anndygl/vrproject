package com.example.sun.test2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Properties;

import me.drakeet.materialdialog.MaterialDialog;

public class settingActivity extends AppCompatActivity {
    private Button logoutButton=null;
    MaterialDialog builder2 =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        logoutButton=(Button)findViewById(R.id.log_out_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder2 = new MaterialDialog(v.getContext());
                //builder2.setTitle("增加分类");
                builder2.setMessage("真的要退出登录吗?*_*");
                builder2.setPositiveButton("确定", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        logoutRunnable r = new logoutRunnable();
                        Thread t = new Thread(r);
                        t.start();
                        try {
                            t.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (r.result.equals("true")) {
                           User.getInstance().setPassword("");
                            User.getInstance().setSession("");
                            User.getInstance().setUsername("");
                            User.getInstance().Hotplaces.clear();
                            User.getInstance().Hotprovinces.clear();
                            Intent intent=new Intent(settingActivity.this,loginActivity.class);
                            startActivity(intent);
                            SysApplication.getInstance().exit();
                            settingActivity.this.finish();
                        } else if (r.result.equals("false")) {
                            new AlertDialog.Builder(settingActivity.this)
                                    .setTitle("退出登录失败")
                                    .setMessage(r.feedback)
                                    .setPositiveButton("确定", null)
                                    .show();
                        } else {
                            new AlertDialog.Builder(settingActivity.this)
                                    .setTitle("退出登录失败")
                                    .setMessage("未知原因，请检查网络或联系制作者")
                                    .setPositiveButton("确定", null)
                                    .show();
                        }
                    }});
                builder2.setNegativeButton("取消",new View.OnClickListener() {

                    public void onClick(View V) {
                        // TODO 自动生成的方法存根
                        builder2.dismiss();
                    }
                });
                builder2.show();
            }
        });
    }
    class logoutRunnable implements Runnable {
        public String result = new String("");
        public String feedback = new String("");

        @Override
        public void run() {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost("");
                String strResult;
                JSONObject jsonObject = null;
                // replace with your url

                request.setHeader("Cookie",User.getInstance().getSession());
                result ="true";
                HttpResponse response;
                try {
                    response = client.execute(request);
                    //Log.d("postresponse",response);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        try {
                            /**读取服务器返回过来的json字符串数据**/
                            strResult = EntityUtils.toString(response.getEntity());
                            jsonObject = getJSON(strResult);
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
                        String names = "";
                        try {
                            /**
                             * jsonObject.getString("code") 取出code
                             * 比如这里返回的json 字符串为 [code:0,msg:"ok",data:[list:{"name":1},{"name":2}]]
                             * **/

                            /**得到data这个key**/
                            result = jsonObject.getString("result");
                            result ="true";
                            if (result.equals("false")) {
                                feedback = jsonObject.getString("feedback");
                                //Looper.prepare();
                                Log.d("loginfeedback", feedback);
                                //Toast.makeText(LoginActivity.this, feedback, Toast.LENGTH_SHORT).show();
                            } else {

                            }
                            //Toast.makeText(LoginActivity.this, "code:" + jsonObject.getString("code") + "name:" + names, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else
                        Toast.makeText(settingActivity.this, "POST提交失败", Toast.LENGTH_SHORT).show();
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

}
