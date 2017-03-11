package com.springapp.dao;

import com.springapp.entity.BookInfo;
import org.springframework.stereotype.Repository;

/**
 * Created by 11369 on 2017/2/22.
 */
@Repository
public class BookInfoDao extends BaseDao {
    public BookInfo getByOpenid(String openid){
        String hql = "from BookInfo where openid = '" + openid + "'";
        return this.find(hql, BookInfo.class);
    }
}
