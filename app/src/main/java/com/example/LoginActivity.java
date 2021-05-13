package com.example;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import static java.net.Proxy.Type.HTTP;

public class LoginActivity extends AppCompatActivity {
    TextView welcome_text;
    TextView name_text;
    TextView account_text;
    TextView password_text;
    EditText account_edit;
    EditText password_edit;
    Button forget_btn;
    Button login_btn;
    Button sign_btn;

    private String responseMsg = "";
    private static final int REQUEST_TIMEOUT = 5*1000;//设置请求超时10秒钟
    private static final int SO_TIMEOUT = 10*1000;  //设置等待数据超时时间10秒钟
    private static final int LOGIN_OK = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        welcome_text=findViewById(R.id.welcome_text);
        name_text=findViewById(R.id.name_text);
        account_text=findViewById(R.id.account_text);
        password_text=findViewById(R.id.password_text);
        password_edit=findViewById(R.id.password_edit);
        account_edit=findViewById(R.id.account_edit);
        forget_btn=findViewById(R.id.forget_btn);
        login_btn=findViewById(R.id.login_btn);
        sign_btn=findViewById(R.id.sign_btn);
        //更换字体
        AssetManager mgr = getAssets();//获取AssetManager
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/MySong.ttf");
        welcome_text.setTypeface(tf);
        name_text.setTypeface(tf);
        password_text.setTypeface(tf);
        account_text.setTypeface(tf);
        //
        StrictMode.setThreadPolicy(new
                StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(
                new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        //设置登录按钮响应事件
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1.获取输入的账号密码
                // 2.与数据库对比
                // 3.判断登录状态
                Thread loginThread = new Thread(new LoginThread());
                loginThread.start();
            }
        });
        //设置注册按钮响应事件
        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1.获取输入账号密码
                // 2.与数据库对比
                // 3.判断是否有重复
                //     3.1 有重复（返回已注册）
                //     3.2 无重复（注册成功，请登录）
                Thread signThread = new Thread(new SignThread());
                signThread.start();
            }
        });
        //设置忘记密码响应
        forget_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private boolean loginServer(String username,String password){
        boolean loginValidata = false;
        //使用apache HTTP客户端实现
        String urlStr="http://192.168.2.105:8080/untitled_war_exploded/login";
        HttpPost request = new HttpPost(urlStr);

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("username",username));
        params.add(new BasicNameValuePair("password",password));
        try
        {
            //设置请求参数项
            request.setEntity(new UrlEncodedFormEntity(params));
            HttpClient client = getHttpClient();
            //执行请求返回相应
            HttpResponse response = client.execute(request);
            System.out.println(response.getStatusLine().getStatusCode());
            //判断是否请求成功
            if(response.getStatusLine().getStatusCode()==200)
            {
                loginValidata = true;
                //获得响应信息
                responseMsg = EntityUtils.toString(response.getEntity());
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return loginValidata;
    }

    //初始化HttpClient
    public HttpClient getHttpClient()
    {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
        HttpClient client = new DefaultHttpClient(httpParams);
        return client;
    }

    //Handler
    Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 0:
                    Toast.makeText(getApplicationContext(), "登录成功！即将进入日迹...", Toast.LENGTH_SHORT).show();
                    Thread myThread=new Thread(){//创建子线程
                        @Override
                        public void run() {
                            try{
                                sleep(1000);//使程序休眠1秒
                                Intent intent=new Intent(getApplicationContext(),start.class);//进入start界面
                                startActivity(intent);
                                finish();//关闭当前活动
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    };
                    myThread.start();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "登录失败，请重试", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "URL错误", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "注册成功！请登录", Toast.LENGTH_SHORT).show();
                    account_edit.setText("");
                    password_edit.setText("");
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "账号已存在,可尝试找回密码", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    //LoginThread线程类
    class LoginThread implements Runnable
    {

        @Override
        public void run() {
            String username = account_edit.getText().toString();
            String password = password_edit.getText().toString();
            System.out.println("username="+username+":password="+password);

            //URL合法，但是这一步并不验证密码是否正确
            boolean loginValidate = loginServer(username, password);
            System.out.println("-----------bool is :"+loginValidate+"----------response:"+responseMsg);
            Message msg = handler.obtainMessage();
            if(loginValidate)
            {
                if(responseMsg.equals("true"))
                {
                    msg.what = 0;
                    handler.sendMessage(msg);
                }else
                {
                    msg.what = 1;
                    handler.sendMessage(msg);
                }

            }else
            {
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }

    }

    private class SignThread implements Runnable {
        //注册
        @Override
        public void run() {
            String username = account_edit.getText().toString();
            String password = password_edit.getText().toString();
            System.out.println("注册：\n");
            System.out.println("username="+username+":password="+password);

            //URL合法，但是这一步并不验证密码是否正确
            boolean signValidate = signServer(username, password);
            System.out.println("-----------bool is :"+signValidate+"----------response:"+responseMsg);
            Message msg = handler.obtainMessage();
            if(signValidate)
            {
                if(responseMsg.equals("true"))
                {
                    msg.what = 4;
                    handler.sendMessage(msg);
                }else
                {
                    msg.what = 3;
                    handler.sendMessage(msg);
                }

            }else
            {
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }

    }
    private boolean signServer(String username,String password){
        boolean signValidata = false;
        //使用apache HTTP客户端实现
        String urlStr="http://192.168.2.105:8080/untitled_war_exploded/sign";
        HttpPost request = new HttpPost(urlStr);

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("username",username));
        params.add(new BasicNameValuePair("password",password));
        try
        {
            //设置请求参数项
            request.setEntity(new UrlEncodedFormEntity(params));
            HttpClient client = getHttpClient();
            //执行请求返回相应
            HttpResponse response = client.execute(request);
            System.out.println(response.getStatusLine().getStatusCode());
            //判断是否请求成功
            if(response.getStatusLine().getStatusCode()==200)
            {
                signValidata = true;
                //获得响应信息
                responseMsg = EntityUtils.toString(response.getEntity());
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return signValidata;
    }

}
