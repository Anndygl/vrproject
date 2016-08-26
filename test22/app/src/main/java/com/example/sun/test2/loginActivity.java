package com.example.sun.test2;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;
import me.drakeet.materialdialog.MaterialDialog;


public class loginActivity  extends AppCompatActivity {

    private EditText userName =null;
    private EditText passWord =null;
    private CircleButton loginButton=null;
    private Button regButton=null;
    public static String result=new String("");
    public static String feedback=new String("");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        //User.getInstance().setIpaddress("005e7ee.nat123.net");
        User.getInstance().setIpaddress("192.168.0.107");
        userName=(EditText)findViewById(R.id.username);
        passWord=(EditText)findViewById(R.id.password);
        loginButton=(CircleButton)findViewById(R.id.log_in_button);
        regButton=(Button)findViewById(R.id.To_Sign_button);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(loginActivity.this,signupActivity.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePostRequest();
                if(result.equals("true"))
                {
                    Snackbar.make(v, "login success", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent intent = new Intent(loginActivity.this, indexActivity.class);
                    startActivity(intent);
                    loginActivity.this.finish();
                }
                else if(result.equals("false"))
                {
                    Snackbar.make(getCurrentFocus(), "loginfailed:"+feedback, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
    private void makePostRequest() {
        User.getInstance().setUsername(userName.getText().toString());
        User.getInstance().setPassword(passWord.getText().toString());
        //verifycode = lvfcdEdit.getText().toString();
        if(User.getInstance().getUsername().equals(""))
        {
            Snackbar.make(getCurrentFocus(), "用户名不得为空", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            result="false";
        }
        else
        {
            if(User.getInstance().getUsername().length()>16||User.getInstance().getUsername().length()<5)
            {
                Snackbar.make(getCurrentFocus(), "用户名必须介于5-16位之间", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else
            {
                if (User.getInstance().getPassword().equals("")) {
                    Snackbar.make(getCurrentFocus(), "密码不得为空", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    if(User.getInstance().getPassword().length()>16||User.getInstance().getPassword().length()<6)
                    {
                        Snackbar.make(getCurrentFocus(), "密码必须介于6-16位之间", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else
                    {
                        User.getInstance().setPassword(getMD5Str(passWord.getText().toString()));
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                NameValuePair pair1 = new BasicNameValuePair("username", User.getInstance().getUsername());
                                NameValuePair pair2 = new BasicNameValuePair("password", User.getInstance().getPassword());
                                List<NameValuePair> pairList = new ArrayList<NameValuePair>();
                                pairList.add(pair1);
                                pairList.add(pair2);
                                try {
                                    HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
                                    HttpClient client = new DefaultHttpClient();
                                    HttpPost request = new HttpPost("http://"+User.getInstance().getIpaddress()+"/Hawkeye/controller/loginController.php");
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
                                                Log.d("session",User.getInstance().getSession());
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
                                                //Toast.makeText(LoginActivity.this, "code:" + jsonObject.getString("code") + "name:" + names, Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }


                                        } else
                                            Toast.makeText(loginActivity.this, "POST提交失败", Toast.LENGTH_SHORT).show();
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
                        });
                        thread.start();
                        try {
                            thread.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    public JSONObject getJSON(String sb) throws JSONException {
        return new JSONObject(sb);
    }
    private static String getMD5Str(String str)
    {
        MessageDigest messageDigest = null;
        try
        {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e)
        {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++)
        {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }
}

