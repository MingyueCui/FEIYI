package com.springapp.mvc;


import com.springapp.dao.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by anc on 15/3/13.
 */
public class BaseController {
    @Autowired
    protected BaseDao baseDao;
    @Autowired
    protected ActivityDao activityDao;
    @Autowired
    protected UserDao userDao;
    @Autowired
    protected ResultDao resultDao;
    @Autowired
    protected BookInfoDao bookInfoDao;
    //过滤出中文
    public static void main(String []args) throws IOException {
        System.out.println(System.getProperty("user.dir"));
        /*String str = "二语学习";
        str=str.replaceAll("[^(\\u4e00-\\u9fa5)]", "");
        String[] result= ToAnalysis.parse(str).toStringWithOutNature().split(",");
        UserDefineLibrary.insertWord("二语学习");
        for(String string:result){
            System.out.print(string+"\n");
        }*/
    }
}
