package com.springapp.classes;

import java.util.*;

/**
 * Created by 11369 on 2016/11/22.
 */
public class WxHelp {
    private static final String APP_ID = "wx03dab806a8de5b9a";
    private static final String OLDAPP_SECRET = "27842e02309c727301349ccbead46c07";
    private static final String OLDDOMAIN = "cx.ecnucpp.com";
    private static final String APP_SECRET = "4ef30e728d1f7d0efe9d00bc4f1401d6";
    private static final String APP_KEY = "shanghaiyuechanxin20160603104666";

    private static final String DOMAIN = "feiyi.ecnucpp.com";
    private static final String MCH_ID = "1253261801";
    private static String TOKEN = "WeiXin";

    /**
     * 微信签名生成
     * @param params
     * @return
     */
    public static String Sign(Map<String,Object> params){
        List<String> keyset = new ArrayList<String>();
        for(String key : params.keySet()){
            keyset.add(key);
        }
        Collections.sort(keyset);
        String stringTemp = "";
        int count = 0;
        for(String key : keyset){
            if(count == 0) {
                stringTemp += key +"=" + params.get(key);
            }else{
                stringTemp += "&" + key +"=" + params.get(key);
            }
            count++ ;
        }
        stringTemp += "&key=" + APP_KEY;
        return MD5.MD5Encode(stringTemp);
    }

    /**
     * 回参转xml
     * @param params
     * @return
     */
    public static String toXML(Map<String,Object> params){
        String xml = "<xml>";
        for(String key:params.keySet()){
            Object value = params.get(key);
            xml+="<"+key+">"+value+"</"+key+">";
        }
        xml+="</xml>";
        return xml;
    }
    public static void main(String[] args){
        Map<String, Object> prepayParams = new HashMap<String, Object>();
        prepayParams.put("return_code", "SUCCESS");
        prepayParams.put("appid", APP_ID);
        prepayParams.put("mch_id", MCH_ID);
        prepayParams.put("nonce_str", System.currentTimeMillis() + "");
        prepayParams.put("prepay_id", "1212121");
        String paysign = Sign(prepayParams);
        prepayParams.put("sign",paysign);
        String xmlParam = toXML(prepayParams);
        System.out.print(xmlParam);
    }
}
