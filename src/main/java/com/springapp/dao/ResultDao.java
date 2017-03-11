package com.springapp.dao;

import com.springapp.entity.WxResult;
import com.springapp.entity.WxUser;
import org.springframework.stereotype.Repository;

/**
 * Created by 11369 on 2016/11/21.
 */
@Repository
public class ResultDao extends BaseDao {
    public WxResult getResultByUid(String openid){
        return this.find("from WxResult where openid=?" , WxResult.class ,new Object[]{openid});
    }
}
