package com.example.erplaer;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class FragmentAdapter extends FragmentStatePagerAdapter {

    private  List<Fragment> mfragmentList;

    private List<String> mtitles;
    List playAll= new ArrayList<>();
   // Map palymap=new HashMap();
    public  FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titles){
        super(fm);

        this.mfragmentList=fragmentList;
        this.mtitles=titles;

    }
    @NonNull
    @Override
    public Fragment getItem(int position) {


        return mfragmentList.get(position);
    }
    @Override
    public int getCount() {


        return mfragmentList.size();

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

       // System.out.println("选项卡测试点 listfragmeng 7");
        return mtitles.get(position);
    }

}

