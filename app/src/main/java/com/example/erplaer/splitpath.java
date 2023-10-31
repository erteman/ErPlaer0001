package com.example.erplaer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class splitpath {
    public static Map split(String string){
        Map map002 = new HashMap();
        String[] sppath=string.split("\\#");
        int t=sppath.length;
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        for(int i=0;i<t;i++) {
            String str=sppath[i];

            String str1 = str.substring(0, str.indexOf("$"));

            String str2 = str.substring(str.indexOf("$") + 1, str.length());
          // System.out.println("第"+i+"次"+"内容"+str);

           // System.out.println("str1,str2:"+str1+":"+str2);
            list1.add(str1);
            list2.add(str2);
        }
        map002.put("list1", list1);
        map002.put("list2", list2);
        return map002;
    }
}
