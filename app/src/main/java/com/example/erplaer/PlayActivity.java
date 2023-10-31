package com.example.erplaer;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pili.pldroid.player.widget.PLVideoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.JZVideoPlayerStandard;


public class PlayActivity extends AppCompatActivity {

//20211002

    private TabLayout tabLayout;
    private ViewPager viewPager;


    private String playpath;
    private String intro;
    private String picpath;
    private String name;
    private String playfrom;
    Map palymap=new HashMap();

    private TextView mtext_play_intro;
    private TextView texttip2;
    private TextView textplay;
    private ImageButton imb01;


    public String forurl;
    public String forespode;
    public PLVideoView mPlVideoView;
    List listi= new ArrayList<>();
    List episode= new ArrayList<>();
    List playurls= new ArrayList<>();
    List playurlAll= new ArrayList<>();
   List playAllfrom=new ArrayList<>();
    private String m3u8url;
    public JZVideoPlayerStandard myJzvdStd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Bundle bundle = getIntent().getExtras();

        viewPager=findViewById(R.id.vp);
        tabLayout=findViewById(R.id.tablayout);
        //initViewpage();
        myJzvdStd =  findViewById(R.id.videoplayer);

        //获取影片各数值
        listi = (List) bundle.get("dbs");
        playpath = (String) listi.get(3);
        intro = (String) listi.get(2);
        picpath = (String) listi.get(1);
        name = (String) listi.get(0);
        playfrom=(String) listi.get(4);
// 防止没有播放源的问题 拆分出问题
        if(playpath.length()!=0){
            playurlAll = split_from.split(playpath);
          //  System.out.println("测试输出拆开数据0000000000" + playpath);
            //播放路径分源拆开得到list
           // System.out.println("测试输出拆开数据" + playurlAll.get(0));
            //播放源拆分
            playAllfrom = split_from.split(playfrom);
            palymap = splitpath.split((String) playurlAll.get(0));
            //播放源选项卡初始化
            initViewpage();
            episode = (List) palymap.get("list1");
            playurls = (List) palymap.get("list2");
            m3u8url = (String) playurls.get(0);
            myJzvdStd.setUp( m3u8url,myJzvdStd.SCREEN_WINDOW_NORMAL,episode.get(0));

        }else{
            //System.out.println("测试输出拆开数据22222" + playpath);


         //   myJzvdStd.setUp( m3u8url,myJzvdStd.SCREEN_WINDOW_NORMAL,episode.get(0));
        }



        //设置选集上方的片名
        textplay=findViewById(R.id.player);
        textplay.setText(name);


        //设置播放简介
        texttip2=findViewById(R.id.play_tip2);
        texttip2.setText("内容简介");
        mtext_play_intro=findViewById(R.id.text_play_intro);
        mtext_play_intro.setText(intro);

        //监听点击下载按钮
        imb01=findViewById(R.id.download);
        imb01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentp;
                intentp=new Intent(view.getContext(), DownActivity.class);
                intentp.putExtra("urls",forurl);
                intentp.putExtra("names",name);
                intentp.putExtra("espode",forespode);


                if(forurl == null){

                    Toast.makeText(getBaseContext(),R.string.nochoice,Toast.LENGTH_LONG).show();


                }else {
                    startActivity(intentp);
                }
            }
        });


    }

    //初始化选项卡
    private void initViewpage(){
        //设置选项表头显示
        for(int i=0;i<playAllfrom.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText((String) playAllfrom.get(i)));
        }
        //拆分数据来源设置string进行传递给各选项卡playAllfrom.size()
        List<Fragment> fragments=new ArrayList<>();
        for (int i=0;i<playAllfrom.size();i++){
            ListFragment lf=new ListFragment();
            fragments.add(lf);
            lf.setPalymap((String)playurlAll.get(i));
           // System.out.println("输出看下值"+(String)playurlAll.get(i));

        }



        FragmentAdapter fragmentAdapter;
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),fragments,playAllfrom);



        //System.out.println("：：：：============移除前调用=========：：：");
        viewPager.setAdapter(fragmentAdapter);
        //System.out.println("选择卡测试222："+"===========");
        tabLayout.setupWithViewPager(viewPager);
       // System.out.println("选择卡测试3333："+"===========");
        //tabLayout.setTabsFromPagerAdapter(fragmentAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        myJzvdStd.release();
    }
    @Override
    public void onBackPressed() {
        if (myJzvdStd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

//提供给Frament更新值
    public void refreshvideo(String inurl,String inname) {
        myJzvdStd.release();
        myJzvdStd.setUp( inurl,myJzvdStd.SCREEN_WINDOW_NORMAL,inname);
        this.forurl=inurl;
        this.forespode=inname;

    }

}