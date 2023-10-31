package com.example.erplaer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mainRecycleAdapter<listi> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<String> list1;
    private List<String> list2;
    private List<String> list3;
    private List<String> list4;
    private List<String> list5;
    private List<String> listi;
    private List<String> listt;

    Map m_map = new HashMap();

    public int t;

    private final int EMPTY_VIEW = 0;
    private final int PROGRESS_VIEW = 1;
    private final int hot_VIEW = 2;
    private final int normal_VIEW = 3;

    private String name01;
    private String pic01;
    private String content01;
    private String path01;
    private String playfrom01;
    private boolean hasMore = true;





    public mainRecycleAdapter(Context context, Map map) {
        this.mContext = context;
        this.m_map = map;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if(viewType==hot_VIEW){

            HotViewHolder hotViewHolder= new HotViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_hot_gradview, viewGroup, false));
            return hotViewHolder;

        }else if(viewType==PROGRESS_VIEW){

                    FootViewHolder footviewholder = new FootViewHolder(LayoutInflater.from(mContext).inflate(R.layout.end, viewGroup, false));
                return footviewholder;
        }

        else {
            GradViewHolder gradViewHolder= new GradViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_normal_gradview, viewGroup, false));

            return gradViewHolder;
        }
            }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
if (i<listt.size()){

    listi = new ArrayList<>();

    list1 = (List<String>) m_map.get("picList");
    list2 = (List<String>) m_map.get("nameList");
    list3 = (List<String>) m_map.get("contentList");
    list4 = (List<String>) m_map.get("pathList");
    list5 = (List<String>) m_map.get("playfromList");
    name01 = list2.get(i);
    pic01 = list1.get(i);
    content01 = list3.get(i);
    path01 = list4.get(i);
    playfrom01=list5.get(i);

}

if(viewHolder instanceof FootViewHolder){

            FootViewHolder footViewHolder=(FootViewHolder)viewHolder;
            footViewHolder.footText1.setText("...加载更多");

        }else  if(viewHolder instanceof GradViewHolder){


            GradViewHolder gradViewHolder1 = (GradViewHolder)viewHolder;
            gradViewHolder1.textView_name.setText(name01);
            gradViewHolder1.textView_content.setText(content01);
               Glide.with(mContext).load(pic01).into( gradViewHolder1.imageView);

            gradViewHolder1.ll_03.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View view){
                    listi.add(0, list2.get(i));
                    listi.add(1, list1.get(i));
                    listi.add(2, list3.get(i));
                    listi.add(3, list4.get(i));
                    System.out.println( "输出测试"+list5.get(i));
                    listi.add(4, list5.get(i));

                    Intent intent3;
                    intent3 = new Intent(view.getContext(), PlayActivity.class);


                    intent3.putExtra("dbs", (Serializable) listi);

                    view.getContext().startActivity(intent3);

                }
            });

        }else if(viewHolder instanceof HotViewHolder){



            HotViewHolder hotViewHolder = (HotViewHolder) viewHolder;
            hotViewHolder.htextView_name.setText(name01);
            hotViewHolder.htextView_content.setText(content01);
            Glide.with(mContext).load(pic01).into( hotViewHolder.himageView);
            hotViewHolder.hll_03.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View view){

                    listi.add(0, list2.get(i));
                    listi.add(1, list1.get(i));
                    listi.add(2, list3.get(i));
                    listi.add(3, list4.get(i));
                    listi.add(4, list5.get(i));

                    Intent intent3;
                    intent3 = new Intent(view.getContext(), PlayActivity.class);


                    intent3.putExtra("dbs", (Serializable) listi);

                    view.getContext().startActivity(intent3);

                }
            });
        }


}

    @Override
    public int getItemCount() {
        listt=(List<String>) m_map.get("picList");
        return listt.size()+1 ;
    }

//0为item为空返回1
    @Override
    public int getItemViewType(int position) {
       listt=(List<String>) m_map.get("picList");
        if(listt.size() == 0){
            return EMPTY_VIEW;
        } else if(position == listt.size()){
            return PROGRESS_VIEW;
        } else if(position<1){
            return hot_VIEW;
        } else if(position>=1){
            return normal_VIEW;
        }else{
            return super.getItemViewType(position);
        }
    }

    //提供给外部调用的方法 刷新数据
    public void updateData(Map map1){

        System.out.println("====进行数据刷新===="+map1);
        List     listn1 = (List<String>) map1.get("picList");
        List     listn2 = (List<String>) map1.get("nameList");
        List    listn3 = (List<String>) map1.get("contentList");
        List    listn4 = (List<String>) map1.get("pathList");
        List    listn5 = (List<String>) map1.get("playfromList");

        list1.addAll(listn1);
        list2.addAll(listn2);
        list3.addAll(listn3);
        list4.addAll(listn4);
        list5.addAll(listn5);
        m_map.put("picList",list1);
        m_map.put("nameList",list2);
        m_map.put("contentList",list3);
        m_map.put("pathList",list4);
        m_map.put("playfromList",list5);
        listt=(List<String>) m_map.get("picList");
      //  System.out.println("====当前MAP的size===="+listt.size());
        //System.out.println("====输出当前的MAP===="+m_map);
       // System.out.println("=====================输出当前的MAP=================");
        notifyDataSetChanged();
    }


    public static class GradViewHolder extends RecyclerView.ViewHolder{

        private  TextView textView_name;
       private TextView textView_content;
       private ImageView imageView;
       private LinearLayout ll_03;

        public GradViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.main_image);
            textView_name=itemView.findViewById(R.id.main_name);
            textView_content=itemView.findViewById(R.id.main_intro);
            ll_03=itemView.findViewById(R.id.ll_003);



        }
    }

    public static class HotViewHolder extends RecyclerView.ViewHolder{

        private TextView htextView_name;
        private TextView htextView_content;
        private ImageView himageView;
        private LinearLayout hll_03;

        public HotViewHolder(@NonNull View itemView) {
            super(itemView);
           himageView=itemView.findViewById(R.id.main_hotim);
            htextView_name=itemView.findViewById(R.id.main_hotname);
            htextView_content=itemView.findViewById(R.id.main_hotintro);
            hll_03=itemView.findViewById(R.id.hot_ll);


        }
    }

    public static class FootViewHolder extends RecyclerView.ViewHolder{
            private TextView footText1;
        public FootViewHolder(@NonNull View itemView) {
            super(itemView);
            footText1=itemView.findViewById(R.id.foot);


        }
    }
}
