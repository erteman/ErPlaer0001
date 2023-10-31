package com.example.erplaer;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class Anjson {

    private  String responseData;


    public Map parseJsonWithJsonObject(@NotNull Response response) throws IOException {
        responseData=response.body().string();

        List<String> idList =new ArrayList<>();
        List<String> nameList=new ArrayList<>();
        List<String> pathList=new ArrayList<>();
        List<String> picList=new ArrayList<>();
        List<String> contentList=new ArrayList<>();
        List<String> palyfromList=new ArrayList<>();
        Map map001=new HashMap();
        try{
            JSONArray jsonArray=new JSONArray(responseData);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String id=jsonObject.getString("id");
                String name=jsonObject.getString("name");
                String path =jsonObject.getString("path");
                String picurl =jsonObject.getString("picurl");
                String content =jsonObject.getString("content");
                String playfrom =jsonObject.getString("play_from");
                idList.add(id);
                nameList.add(name);
                pathList.add(path);
                picList.add(picurl);
                contentList.add(content);
                palyfromList.add(playfrom);



            }

            map001.put("idList",idList);
            map001.put("nameList",nameList);
            map001.put("pathList",pathList);
            map001.put("picList",picList);
            map001.put("contentList",contentList);
            map001.put("playfromList",palyfromList);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map001;

    }

}
