package com.example.erplaer;

import java.util.ArrayList;
import java.util.List;

public class split_from {

    public static List<String> split(String string){

        String[] sppath=string.split("\\$\\$\\$");
        int t=sppath.length;
        List<String> list1 = new ArrayList<>();

        for(int i=0;i<t;i++) {
            String str=sppath[i];

           //System.out.println("第一次分解 ："+str+"序号："+i );
            list1.add(str);

        }

        return list1;
    }
}
