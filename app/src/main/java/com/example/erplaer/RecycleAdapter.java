package com.example.erplaer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pili.pldroid.player.widget.PLVideoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.GradViewHolder>{
    private Context mContext;
    private List<String> list1;
    private List<String> list2;
    private List<String> list3;
    List listn=new ArrayList();
    public PLVideoView  uPlVideoView;
    private GradViewHolder.OnClickMyTextView mOnClickMyTextView;
    private String pli;
    private String path01;
    Map map = new HashMap();


    public RecycleAdapter(Context context, Map map){
        this.mContext=context;
        this.map=map;
    }


    public void setOnClickMyTextView(GradViewHolder.OnClickMyTextView mOnClickMyTextView) {
        this.mOnClickMyTextView = mOnClickMyTextView;
    }

    @NonNull
    @Override
    public RecycleAdapter.GradViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {





        return new GradViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_gradview,viewGroup,false));
    }




    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.GradViewHolder gradViewHolder, final int i) {

        list1= (List<String>) map.get("list1");
        list2=(List<String>)map.get("list2");
       // System.out.println("RECYCLEadapter的I值="+i);
        pli=list1.get(i);
        path01=list2.get(i);


        gradViewHolder.textView.setText(pli);

        
        gradViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  System.out.println("===========这次点击的是:=============="+i);
                mOnClickMyTextView.myTextViewClick(i);
                ((ViewGroup)view.getParent()).removeView(view);

               /* PlayActivity.getmPlVideoView.setVideoPath(path01);
                mPlVideoView.start();
                Intent intent3;
                intent3=new Intent(view.getContext(), PlayActivity.class);
                intent3.putExtra("m3u8",path01);
                view.getContext().startActivity(intent3);
                */

            }
        });


    }


    @Override
    public int getItemCount() {

        listn= (List) map.get("list1");
        System.out.println("====getItemCount===="+listn.size());
        return listn.size();


    }
    static class GradViewHolder extends RecyclerView.ViewHolder{


        private TextView textView;

        public GradViewHolder(@NonNull View itemView) {


            super(itemView);
            textView=itemView.findViewById(R.id.view_title);

        }

   public interface OnClickMyTextView {

           void myTextViewClick(int i);


        }


    }


}
