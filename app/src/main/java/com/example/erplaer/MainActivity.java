package com.example.erplaer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.king.app.dialog.AppDialog;
import com.king.app.dialog.AppDialogConfig;
import com.king.app.updater.AppUpdater;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Map map = new HashMap();
    Map map01 = new HashMap();
    Map map02 = new HashMap();
    Map updatemap=new HashMap();
    Map updateinfomap=new HashMap();
   Map  getmap =  new HashMap();

    SearchView msearchview;
    public String utoken;
    private int nowpage;


    public String ustatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout line001 = (LinearLayout) findViewById(R.id.ll_01);
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        utoken = sharedPreferences.getString("token", "");
        ustatus = sharedPreferences.getString("status", "");
        Button button1, button2, button3, button4, budown, butlogin, butvip;
        button1 = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        butvip = findViewById(R.id.buttonvip);

        if (ustatus.equals("1")) {
            butvip.setVisibility(View.VISIBLE);

        } else {

            butvip.setVisibility(View.GONE);
        }
        forceUpdate();
        msearchview = findViewById(R.id.search01);
        msearchview.setQueryHint("输入影片名");

        budown = findViewById(R.id.downpage);
        butlogin = findViewById(R.id.login);

        //设置监听器
        button1.setOnClickListener(bulisten);
        button2.setOnClickListener(bulisten);
        button3.setOnClickListener(bulisten);
        button4.setOnClickListener(bulisten);
        butvip.setOnClickListener(bulisten);





      //  SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swres01);


//下载按钮
        budown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent001;
                intent001 = new Intent(MainActivity.this, DownActivity.class);
                startActivity(intent001);

            }
        });
//登录按钮
        butlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent001;
                intent001 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent001);
            }
        });

//查询
        msearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent3;

                intent3 = new Intent(MainActivity.this, RestoutActivity.class);
                intent3.putExtra("dbs", s);
                startActivity(intent3);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
//默认首页
        GETS(4);
    }

    //点击各个类型
    private View.OnClickListener bulisten = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button:

                    map02.clear();
                    GETS(4);
                    break;   //首页
                case R.id.button2:

                    map02.clear();
                    GETS(1);
                    break;  //电影
                case R.id.button3:
                    map02.clear();

                    GETS(2);
                    break; //电视剧
                case R.id.button4:
                    map02.clear();

                    GETS(0);
                    break; //综合
                case R.id.buttonvip:

                    map02.clear();
                    GETS(99);
                    break;  //vip
            }
        }
    };

    //如果token过期或者失败 t=99返回的数据和t=4返回数据一样
    private void GETS(int t) {
        HttpUtil.sendRequestWithOkhttp("http://180.169.217.164:48081/getset/getsdb?type=" + t + "&token=" + utoken+"&cpage=1", new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Anjson anjson = new Anjson();
                map = anjson.parseJsonWithJsonObject(response);
                runOnUiThread(new Runnable() {
                                  @RequiresApi(api = Build.VERSION_CODES.M)
                                  @Override
                                  public void run() {
                                      RecyclerView mrecyview2 = (RecyclerView) findViewById(R.id.main_recyls);
                                      //spancount修改为3 每一行从2修改为3
                                      GridLayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this, 3);
                                      mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                          @Override
                                          public int getSpanSize(int position) {
                                              List<String> listi = new ArrayList<>();

                                              listi = (List<String>) map.get("picList");

                                              if (position < 1 | position == listi.size()) {

                                                  return 3;
                                                  //显示数=sapncount/ return数

                                              } else {
                                                  return 1;
                                              }
                                          }
                                      });


                                      mrecyview2.setLayoutManager(mLayoutManager);

                                      mainRecycleAdapter adapter = new mainRecycleAdapter(MainActivity.this, map);

                                      mrecyview2.setAdapter(adapter);
                                      nowpage=2;
                                      mrecyview2.addOnScrollListener(new EndlessRecyclerOnScrollListener()
                                      {

                                          @Override
                                          //加载更多数据的方法
                                          public void onLoadMore()
                                          {



                                              System.out.println("下拉到最后了");
                                              map02=GetMore(nowpage,t);
                                              nowpage++;
                                              List listt=(List<String>) map02.get("nameList");

                                              if (map02.size() != 0) {
                                                //  System.out.println("========提交数据给updateData======" + map02.get("nameList"));
                                                  adapter.updateData(map02);
                                                  //System.out.println("=========更新更多的数据结果 。。。。。。。。。======" + map02);
                                              }

                                          }


                                      });

                                  }
                              }
                );


            }
        });
    }

//获取更多数据的方法但是注意因为是异步的方式 所以其实第一次获取到不能刷出展现 第二次请求才会把第一次请求的结果给展现
    private Map GetMore(int pageti, int type01)
    {

         HttpUtil.sendRequestWithOkhttp("http://180.169.217.164:48081/getset/getsdb?type="+type01+"&token=" + utoken+"&cpage="+pageti, new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                System.out.println("=========系统出错了===============");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Anjson anjson1 = new Anjson();
                getmap = anjson1.parseJsonWithJsonObject(response);

            }
        } );

        List list004=(List<String>) getmap.get("nameList");
     //  System.out.println("=========返回的数据结果 return ====="+list004);
    return getmap;

    }

 //点击更新按钮进行更新
    public void checkUpdate(View view) {
        String s1 = "http://180.169.217.164:48081/getset/update";

        HttpUtil.sendRequestWithOkhttp(s1, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("=========系统出错了===============");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                try {
                    updateinfomap=parseJsonWithJsonObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            //获取各个数据
                 int sversioncode= Integer.parseInt((String) updateinfomap.get("kVersionCode"));
                String mdifyContent= (String) updateinfomap.get("kModifyContent");
                String versionName= (String) updateinfomap.get("kVersionName");
                int apkSize= Integer.parseInt((String) updateinfomap.get("kApkSize"));
                String msg= (String) updateinfomap.get("kMsg");
                String downloadUrl= (String) updateinfomap.get("kDownloadUrl");
                int updateStatus= Integer.parseInt((String) updateinfomap.get("kUpdateStatus"));

             //   updatemap = (JSONObject) anjson1.parseJsonWithJsonObject(response);
                //获取AP当前版本 build.gradle   versionCode 193
                int currentVersion = getVersionCode(MainActivity.this);

                if(sversioncode>currentVersion) {
                    //简单弹框升级
                    AppDialogConfig config = new AppDialogConfig(MainActivity.this);
                    config.setTitle("三秒影院"+versionName+"版本")
                            .setConfirm("升级") //旧版本使用setOk
                            .setContent(mdifyContent+"\r\n版本：" + versionName + " 更新包大小:" + apkSize/1024 + "MB")
                            .setOnClickConfirm(new View.OnClickListener() { //旧版本使用setOnClickOk
                                @Override
                                public void onClick(View v) {
                                    new AppUpdater.Builder()
                                            .setUrl(downloadUrl)
                                            .build(getBaseContext())
                                            .start();
                                    AppDialog.INSTANCE.dismissDialog();
                                }
                            });
                    Looper.prepare();
                    AppDialog.INSTANCE.showDialog(MainActivity.this, config);
                    Looper.loop();
                }else
                    {

                        AppDialogConfig config = new AppDialogConfig(MainActivity.this);
                        config.setTitle("三秒影院"+versionName+"版本")
                                 //旧版本使用setOk
                                .setContent("您的版本目前已经是最新版本" );

                        Looper.prepare();
                        AppDialog.INSTANCE.showDialog(MainActivity.this, config);
                        Looper.loop();
                }

            }
        });




    }
   //启动自查如果需要强制更新则强制更新

    public void forceUpdate() {
        String s2 = "http://180.169.217.164:48081/getset/update";

        HttpUtil.sendRequestWithOkhttp(s2, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("== 系统出错了！加QQ群：461438830 ==");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                try {
                    updateinfomap=parseJsonWithJsonObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //获取各个数据
                int sversioncode= Integer.parseInt((String) updateinfomap.get("kVersionCode"));
                String mdifyContent= (String) updateinfomap.get("kModifyContent");
                String versionName= (String) updateinfomap.get("kVersionName");
                int apkSize= Integer.parseInt((String) updateinfomap.get("kApkSize"));
                String msg= (String) updateinfomap.get("kMsg");
                String downloadUrl= (String) updateinfomap.get("kDownloadUrl");
                int updateStatus= Integer.parseInt((String) updateinfomap.get("kUpdateStatus"));


                int currentVersion = getVersionCode(MainActivity.this);
                if(updateStatus==1) {
                    if (sversioncode > currentVersion) {
                        //简单弹框升级
                        AppDialogConfig config = new AppDialogConfig(MainActivity.this);
                        config.setTitle("重要版本更新")
                                .setConfirm("升级") //旧版本使用setOk
                                .setContent(mdifyContent + "\r\n版本：" + versionName + " 更新包大小:" + apkSize / 1024 + "MB")
                                .setOnClickConfirm(new View.OnClickListener() { //旧版本使用setOnClickOk
                                    @Override
                                    public void onClick(View v) {
                                        new AppUpdater.Builder()
                                                .setUrl(downloadUrl)
                                                .build(getBaseContext())
                                                .start();
                                        AppDialog.INSTANCE.dismissDialog();
                                    }
                                });
                        Looper.prepare();
                        AppDialog.INSTANCE.showDialog(MainActivity.this, config);
                        Looper.loop();
                    }
                }

            }
        });




    }

        //调用接口 并且json 获取数据的分装
    private Map parseJsonWithJsonObject(Response response) throws IOException, JSONException {
            String responseData;

                responseData=response.body().string();
                System.out.println(responseData);

            JSONObject jsonObject=new JSONObject(responseData);
            //内部版本号 INT 型 用来比对是否需要更新   201
            String VersionCode=jsonObject.getString("VersionCode");
        String ModifyContent=jsonObject.getString("ModifyContent");
        String VersionName=jsonObject.getString("VersionName");
        String ApkSize=jsonObject.getString("ApkSize");
        String Msg=jsonObject.getString("Msg");
        String DownloadUrl=jsonObject.getString("DownloadUrl");
        //判断是否为强制更新  1为强制更新   0为普通更新
        String UpdateStatus=jsonObject.getString("UpdateStatus");

           updatemap.put("kVersionCode",VersionCode);
        updatemap.put("kModifyContent",ModifyContent);
        updatemap.put("kVersionName",VersionName);
        updatemap.put("kApkSize",ApkSize);
        updatemap.put("kMsg",Msg);
        updatemap.put("kUpdateStatus",UpdateStatus);
        updatemap.put("kDownloadUrl",DownloadUrl);

            return  updatemap;
        }


        //获取版本名等信息
        public static String getVersionName (Context context){
            return getPackageInfo(context).versionName;
        }


        public static int getVersionCode (Context context){
            return getPackageInfo(context).versionCode;
        }

        private static PackageInfo getPackageInfo (Context context){
            PackageInfo pi = null;

            try {
                PackageManager pm = context.getPackageManager();
                pi = pm.getPackageInfo(context.getPackageName(),
                        PackageManager.GET_CONFIGURATIONS);

                return pi;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return pi;
        }


}


