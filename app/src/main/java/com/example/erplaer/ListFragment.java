package com.example.erplaer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//因为是直接拉过来所以是ListFragment 实际应该recycleFrament
public class ListFragment extends Fragment {

    //private MyListener myListener;
    private RecycleAdapter recadapter;
   private String  playurlallstring;
    Map playurlmap=new HashMap();
    private String forurl;
    public View viewState;
    private String forespode;
    List episode= new ArrayList<>();
    List playurls= new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
   // private JZVideoPlayerStandard myJzvdStd;

    public void setPalymap(String s) {
        //播放路径传递过来（集数+URL） map  splitpath.split((String) playAll.get(0));
        this.playurlallstring= s;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ///View view=inflater.inflate(R.layout.list_fragment,container,false);
        //System.out.println("：：：：============viewState=========：：："+viewState);
            viewState = inflater.inflate(R.layout.list_fragment, container,
                    false);

        playurlmap= splitpath.split(this.playurlallstring);
        episode = (List<String>) playurlmap.get("list1");
        playurls = (List<String>) playurlmap.get("list2");


        RecyclerView recy_paly = viewState.findViewById(R.id.recycle_play);

        if(recy_paly.getParent()!=null){
           // System.out.println("：：：：============输出测试移除调用=========9999999999：：：");
            ((ViewGroup) recy_paly.getParent()).removeView(recy_paly);
        }

        recy_paly.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        recadapter = new RecycleAdapter(getActivity(), playurlmap);
        recy_paly.setAdapter(recadapter);


        recadapter.setOnClickMyTextView(new RecycleAdapter.GradViewHolder.OnClickMyTextView() {
            @Override
            public void myTextViewClick(int i) {

                forurl= (String) playurls.get(i);
                forespode= (String) episode.get(i);
                //配置一个所在ACTIVITY对象 方便调用方法。
                PlayActivity plActivity = (PlayActivity) getActivity();
                plActivity.refreshvideo(forurl,forespode);

            }
        });

        return recy_paly;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }


}
