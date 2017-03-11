package com.springapp.dao;

import com.springapp.entity.WxUser;
import org.springframework.stereotype.Repository;

/**
 * Created by 11369 on 2016/10/25.
 */
@Repository
public class UserDao extends BaseDao{
    public WxUser getByOpenID(String openid){
        return this.find("from WxUser where openID='"+openid+"'",WxUser.class);
    }
}
