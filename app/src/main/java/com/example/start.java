package com.example;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class start extends AppCompatActivity {
    TextView welcome_text;
    Button ready_btn;
    Button person_btn;
    Button setting_btn;
    public static int status=0;/* status=0  初始状态
                                    status=1 路线生成中
                                    status=2 等待编辑
                                    status=3 路线日记生成成功
                                    **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(status==0){
            //路线初始界面
            setContentView(R.layout.activity_start);
            //获取组件
            welcome_text=findViewById(R.id.welcome_text);
            ready_btn=findViewById(R.id.ready_btn);
            person_btn=findViewById(R.id.person_btn);
            setting_btn=findViewById(R.id.setting_btn);
            //设置字体
            AssetManager assetManager=getAssets();
            Typeface typeface = Typeface.createFromAsset(assetManager,"fonts/MySong.ttf");
            welcome_text.setTypeface(typeface);
            //设置显示文字
            String s;
            s="Hello~"+"\n\n现在是"+ Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    +":"+Calendar.getInstance().get(Calendar.MINUTE)+
                    "\n\n准备好了吗\n\n开始新的一天吧！";
            welcome_text.setText(s);
            ready_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    status=1;
                    Intent intent = new Intent(start.this,start.class);
                    startActivity(intent);
                }
            });
            person_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(start.this,user.class);
                    startActivity(intent);
                }
            });
            setting_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打开菜单setting
                }
            });
        }else if(status==1){
            //路线生成中界面
            setContentView(R.layout.activity_build);
            Button finish_btn=findViewById(R.id.finishTail_btn);
            Button person_btn=findViewById(R.id.person_btn);
            Button setting_btn=findViewById(R.id.setting_btn);
            finish_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    status=2;
                    Intent intent = new Intent(start.this,start.class);
                    startActivity(intent);
                }
            });
            person_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(start.this,user.class);
                    startActivity(intent);
                }
            });
            setting_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打开菜单setting
                }
            });

        }else if(status==2){
            //路线生成完成，等待编辑节点
            setContentView(R.layout.activity_record);
            Button editTail_btn=findViewById(R.id.editTail_btn);
            Button person_btn=findViewById(R.id.person_btn);
            Button setting_btn=findViewById(R.id.setting_btn);
            editTail_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    status=3;
                    Intent intent = new Intent(start.this,start.class);
                    startActivity(intent);
                }
            });
            person_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(start.this,user.class);
                    startActivity(intent);
                }
            });
            setting_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打开菜单setting
                }
            });

        }else if(status==3){
            //完成路线日记
            setContentView(R.layout.activity_share);
            //获取组件
            TextView text=findViewById(R.id.text);
            Button share_btn=findViewById(R.id.share_btn);
            Button person_btn=findViewById(R.id.person_btn);
            setting_btn=findViewById(R.id.setting_btn);
            //设置字体
            AssetManager assetManager=getAssets();
            Typeface typeface = Typeface.createFromAsset(assetManager,"fonts/MySong.ttf");
            text.setTypeface(typeface);
            //设置显示文字
            String s;
            s="恭喜你"+"\n\n记录了今天的生活！";
            welcome_text.setText(s);
            share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //截图分享
                }
            });
            person_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(start.this,user.class);
                    startActivity(intent);
                }
            });
            setting_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打开菜单setting
                }
            });

        }


    }
}