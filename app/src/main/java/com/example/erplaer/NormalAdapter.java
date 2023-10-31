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

public class NormalAdapter<listi> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<String> list1;
    private List<String> list2;
    private List<String> list3;
    private List<String> list4;
    private List<String> list5;
    private List<String> listi;
    private List<String> listt;
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


    Map map = new HashMap();


    public NormalAdapter(Context context, Map map) {
        this.mContext = context;
        this.map = map;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {



            return new GradViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_normal_gradview, viewGroup, false));


            }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {



            listi = new ArrayList<>();

            list1 = (List<String>) map.get("picList");
            list2 = (List<String>) map.get("nameList");
            list3 = (List<String>) map.get("contentList");
            list4 = (List<String>) map.get("pathList");
            list5 = (List<String>) map.get("playfromList");
                name01 = list2.get(i);
                pic01 = list1.get(i);
                content01 = list3.get(i);
                path01 = list4.get(i);
                 playfrom01 = list5.get(i);
            GradViewHolder gradViewHolder = (GradViewHolder)viewHolder;
            gradViewHolder.textView_name.setText(name01);
            gradViewHolder.textView_content.setText(content01);
               Glide.with(mContext).load(pic01).into( gradViewHolder.imageView);

            gradViewHolder.ll_03.setOnClickListener(new View.OnClickListener()

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

    @Override
    public int getItemCount() {
        listt=(List<String>) map.get("picList");

        return listt.size() ;
    }

//0为item为空返回1



    public class GradViewHolder extends RecyclerView.ViewHolder{

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


}
