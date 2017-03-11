package com.springapp.entity;

import com.springapp.classes.DateUtil;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 11369 on 2016/10/25.
 */
@Entity
public class Activity {
    private Long id;
    private String title;//活动标题
    private String content;//活动内容
    private String editor;//创建人
    //时间均采用时间戳存
    private Long createTime;//创建时间
    private String createDate;
    private Long fromTime;//开始时间
    private String fromDate;//yyyy-MM-dd
    private Long toTime;//结束时间
    private String toDate;//yyyy-MM-dd
    private Integer fromYear;
    private Integer fromMonth;
    private Integer fromDay;
    private Integer toYear;
    private Integer toMonth;
    private Integer toDay;

    @Transient
    public String getCreateDate() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date =new Date(createTime);
        return sdf.format(date);
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Transient
    public Integer getFromYear() throws ParseException {
        SimpleDateFormat sdf_YYYY_M_dd=new SimpleDateFormat("yyyy-M-d");
        Date date=sdf_YYYY_M_dd.parse(fromDate);
        return DateUtil.getYear(date);
    }

    public void setFromYear(Integer fromYear) {
        this.fromYear = fromYear;
    }
    @Transient
    public Integer getFromMonth() throws ParseException {
        SimpleDateFormat sdf_YYYY_M_dd=new SimpleDateFormat("yyyy-M-d");
        Date date=sdf_YYYY_M_dd.parse(fromDate);
        return DateUtil.getMonth(date);
    }

    public void setFromMonth(Integer fromMonth) {
        this.fromMonth = fromMonth;
    }
    @Transient
    public Integer getFromDay() throws ParseException {
        SimpleDateFormat sdf_YYYY_M_dd=new SimpleDateFormat("yyyy-M-d");
        Date date=sdf_YYYY_M_dd.parse(fromDate);
        return DateUtil.getDay(date);
    }

    public void setFromDay(Integer fromDay) {
        this.fromDay = fromDay;
    }
    @Transient
    public Integer getToYear() throws ParseException {
        SimpleDateFormat sdf_YYYY_M_dd=new SimpleDateFormat("yyyy-M-d");
        Date date=sdf_YYYY_M_dd.parse(toDate);
        return DateUtil.getYear(date);
    }

    public void setToYear(Integer toYear) {
        this.toYear = toYear;
    }
    @Transient
    public Integer getToMonth() throws ParseException {
        SimpleDateFormat sdf_YYYY_M_dd=new SimpleDateFormat("yyyy-M-d");
        Date date=sdf_YYYY_M_dd.parse(toDate);
        return DateUtil.getMonth(date);
    }

    public void setToMonth(Integer toMonth) {
        this.toMonth = toMonth;
    }
    @Transient
    public Integer getToDay() throws ParseException {
        SimpleDateFormat sdf_YYYY_M_dd=new SimpleDateFormat("yyyy-M-d");
        Date date=sdf_YYYY_M_dd.parse(toDate);
        return DateUtil.getDay(date);
    }

    public void setToDay(Integer toDay) {
        this.toDay = toDay;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getFromTime() {
        return fromTime;
    }

    public void setFromTime(Long fromTime) {
        this.fromTime = fromTime;
    }

    public Long getToTime() {
        return toTime;
    }

    public void setToTime(Long toTime) {
        this.toTime = toTime;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
