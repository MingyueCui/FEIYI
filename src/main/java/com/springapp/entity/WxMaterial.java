package com.springapp.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by 11369 on 2016/11/25.
 */
public class WxMaterial {
    private String media_id;
    private String content;
    private String update_time;
    private List<WxNews> newsList;

    public List<WxNews> getNewsList() {
        JSONObject jsonObject = JSON.parseObject(content);
        this.newsList = JSONArray.parseArray(jsonObject.getString("news_item"), WxNews.class);
        return this.newsList;
    }

    public void setNewsList(List<WxNews> newsList) {
        this.newsList = newsList;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public boolean isIn(String keyword){
        for(WxNews wxNews:getNewsList()){
            System.out.print(wxNews.getTitle());
            if(wxNews.getTitle().contains(keyword))
                return true;
        }
        return false;
    }

    public WxNews newsForSend(String keyword){
        for(WxNews wxNews:getNewsList()){
            System.out.print(wxNews.getTitle());
            if(wxNews.getTitle().contains(keyword))
                return wxNews;
        }
        return null;
    }
}
