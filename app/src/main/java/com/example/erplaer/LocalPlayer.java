package com.example.erplaer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

public class LocalPlayer extends AppCompatActivity  {
    private GSYVideoPlayer myGSY;
    private String m3u8url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_local_player);
        Bundle bundle = getIntent().getExtras();
        m3u8url=(String) bundle.get("dbs");

       // System.out.println("测试播放1---==="+m3u8url);
        myGSY=findViewById(R.id.MyGSY);

        myGSY.setUp(m3u8url, true, "本地播放");
        myGSY.getStartButton().performClick();

    }




}