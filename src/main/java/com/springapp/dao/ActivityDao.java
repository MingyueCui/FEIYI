package com.springapp.dao;

import com.springapp.entity.Activity;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 11369 on 2016/10/25.
 */
@Repository
public class ActivityDao extends BaseDao{
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-M-d");
    public List<Activity>getList(){
        return this.findAll("from Activity",Activity.class);
    }
    public List<Activity>getListByTitle(String title){
        return this.findAll("from Activity where title like '%"+title+"%'",Activity.class);
    }
    public List<Activity>getListByMonth(Long time1,Long time2){
        return this.findAll("from Activity where fromTime>=? and fromTime<=? group by fromDate",Activity.class, time1,time2);
    }
    //错误的时间格式返回null
    public List<Activity>getListByDate(String date){
        Long time;
        try {
            Date fromDate=sdf.parse(date);
            time=fromDate.getTime();
            return this.findAll("from Activity where fromTime>=? and fromTime<?",Activity.class, time,time+3600*24*1000);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public List<Activity>getListByDateAndTitle(String title,String date){
        Long time;
        try {
            Date fromDate=sdf.parse(date);
            time=fromDate.getTime();
            return this.findAll("from Activity where fromTime>=? and fromTime<=? and title like ?",Activity.class, time,time+3600*24,'%'+title+'%');
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
