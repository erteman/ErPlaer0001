package com.example.erplaer;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class RestoutActivity extends AppCompatActivity {


    List list2 = new ArrayList();
    List list1 = new ArrayList();
   private  String key;
   private String sql;
    Map map = new HashMap();

SearchView research;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restout);
        LinearLayout line001=(LinearLayout)findViewById(R.id.rest_ll);
        research=findViewById(R.id.search_rest);


        Bundle bundle1 = getIntent().getExtras();
        key= (String) bundle1.get("dbs");



        research.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                HttpUtil.sendRequestWithOkhttp("http://180.169.217.164:48081/getset/getssome?key1="+s, new okhttp3.Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        Anjson anjson = new Anjson();
                        map=anjson.parseJsonWithJsonObject(response);


                        list1= (List<String>) map.get("idList");


                         if(list1.size()==0){
                             System.out.println("未查询到");
                             TextView mtext=findViewById(R.id.text_rest);
                             mtext.setText("抱歉！ 未查询到 =.= 换个名字查询吧");

                         }else{


                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                TextView mtext=findViewById(R.id.text_rest);

                                mtext.setText("查询到的结果");

                                RecyclerView mrecyview3= (RecyclerView) findViewById(R.id.recycle_rest);
                              //  TextView mtextview=(TextView)findViewById(R.id.text_rest);

                                mrecyview3.setLayoutManager(new GridLayoutManager(RestoutActivity.this,2));
                                mrecyview3.setAdapter(new NormalAdapter(RestoutActivity.this, map));
                             //   mtextview.setText("结果为空");

                            }

                        });
                         }
                    }}
                );

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


//更新显示



        HttpUtil.sendRequestWithOkhttp("http://180.169.217.164:48081/getset/getssome?key1="+key, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Anjson anjson = new Anjson();
                map = anjson.parseJsonWithJsonObject(response);

                list2 = (List<String>) map.get("idList");


                if (list2.size() == 0) {

                    TextView mtext=findViewById(R.id.text_rest);
                    mtext.setText("抱歉！ 未查询到 >.< 换个名字查询吧");

                } else {
                    
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView mtext=findViewById(R.id.text_rest);
                            mtext.setText("查询到的结果");


                            RecyclerView mrecyview3 = (RecyclerView) findViewById(R.id.recycle_rest);

                            mrecyview3.setLayoutManager(new GridLayoutManager(RestoutActivity.this, 2));
                            mrecyview3.setAdapter(new NormalAdapter(RestoutActivity.this, map));

                        }
                    });
                }
            }
        });


    }
}