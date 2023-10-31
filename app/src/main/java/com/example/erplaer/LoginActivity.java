package com.example.erplaer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    public  String is;
    public String result;
    public SharedPreferences sp;

    public String res002;
    private String vres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp= getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String islogin=sp.getString("status","");
        String prename = sp.getString("name", "");
        String prepwd = sp.getString("passwd", "");
        String expire=sp.getString("ttime", "");
        String nowtoken=sp.getString("token","");
        Button btnlogin, btnback;
        EditText editname;
        EditText editpasswd;
        Map map = new HashMap();
        System.out.println("检查是否登陆"+islogin);
        TextView textname=findViewById(R.id.username);
        TextView textpwd=findViewById(R.id.userpasswd);
        btnback = findViewById(R.id.back);
        btnlogin = findViewById(R.id.usrlogin);
        editname = (EditText) findViewById(R.id.uname);
        editpasswd = (EditText) findViewById(R.id.upasswd);

        TextView named=findViewById(R.id.nowname);
        TextView welcomed=findViewById(R.id.welcome);
        TextView tokentime=findViewById(R.id.tokentime);
//已经登录
        if(islogin.equals("1")) {
            //状态处于登录需检查token
            //获取token


            try {
                vres= checktoken(nowtoken);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

           // System.out.println("测试00========+"+ vres);


           if("1".equals(vres)) {

               btnback.setVisibility(View.GONE);
               btnlogin.setVisibility(View.GONE);
               editname.setVisibility(View.GONE);
               editpasswd.setVisibility(View.GONE);
               textname.setVisibility(View.GONE);
               textpwd.setVisibility(View.GONE);
               named.setVisibility(View.VISIBLE);
               welcomed.setVisibility(View.VISIBLE);
               tokentime.setVisibility(View.VISIBLE);
               named.setText(prename);
               tokentime.setText(expire);
           }
           else{
               String outtime="您的登录已过期 ，请重新登录";
               Toast.makeText(getApplicationContext(), outtime, Toast.LENGTH_LONG).show();
           }


        }

        editname.setText(prename);
        editpasswd.setText(prepwd);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usname = editname.getText().toString().trim();
                String uspasswd = editpasswd.getText().toString().trim();
                String tipempty = "用户名或密码为空";

                if (TextUtils.isEmpty(usname) || TextUtils.isEmpty(uspasswd)) {
                    Toast.makeText(getApplicationContext(), tipempty, Toast.LENGTH_LONG).show();
                } else {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();

                    Okhttp(usname, uspasswd);


                }
            }
        } );

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent001;
                intent001 = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent001);
            }
        } );

    }

//登录
    public void Okhttp(final String pname, final String pwd) {





        new Thread(new Runnable() {//开启线程
            @Override
            public void run() {
                FormBody body = new FormBody.Builder()
                        .add("name", pname)   //提交参数电话和密码
                        .add("pw", pwd)
                        .build();
                Request request = new Request.Builder()
                        .url("http://180.169.217.164:48081/getset/logincheck")  //请求的地址
                        .post(body)
                        .build();
                OkHttpClient client = new OkHttpClient();

                try {
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                    //获得值

                    JX(result,pname,pwd);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();


    }

 //token效验

    public String checktoken(String s) throws ExecutionException, InterruptedException {


        FutureTask<String>  ft=new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                FormBody body = new FormBody.Builder()
                        .add("token", s)   //提交参数电话和密码
                        .build();
                Request request = new Request.Builder()
                        .url("http://180.169.217.164:48081/getset/verity")  //请求的地址
                        .post(body)
                        .build();

                OkHttpClient client = new OkHttpClient();

                Response response = client.newCall(request).execute();
                res002 = response.body().string();
                return res002;

            }

        });



         new Thread(ft).start();
         return ft.get();


    }





//对返回json进行解析
    private void JX(String res,String pname,String pwd) {
        try {
            JSONObject jsonObject = new JSONObject(res);
            String flag,ttoken;
            flag= jsonObject.getString("status");//获取返回值flag的内容

            if (flag.equals("1")) {
                is = jsonObject.getString("description");
                ttoken=jsonObject.getString("token");
               String tttime=jsonObject.getString("timeduration");


                SharedPreferences.Editor editor = sp.edit();
                editor.putString("name",pname);
                editor.putString("passwd",pwd);
                editor.putString("token",ttoken);
                editor.putString("status",flag);
                editor.putString("ttime",tttime);
                editor.commit();

                Intent intent001;
                intent001=new Intent(LoginActivity.this,MainActivity.class );
                startActivity(intent001);


            } else {
                is="密码错误";
                Looper.prepare();
               // System.out.println("登录的结果为token11===="+is);
                Toast.makeText(getApplicationContext(), is, Toast.LENGTH_LONG).show();
                Looper.loop();

            }

          //  System.out.println("登录的结果为token===="+is);
        } catch (JSONException  e) {
            e.printStackTrace();

        }


    }


}