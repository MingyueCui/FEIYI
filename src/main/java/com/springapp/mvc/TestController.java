package com.springapp.mvc;

import com.springapp.entity.IConst;
import com.springapp.entity.WxResult;
import com.springapp.entity.WxUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by 11369 on 2016/11/21.
 * 前端答题
 */
@Controller
@RequestMapping(value = "Wx/")
public class TestController extends BaseController {
    @RequestMapping(value = "/question")
    public ModelAndView Test(HttpSession session , HttpServletResponse response, HttpServletRequest request) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
/*
        session.setAttribute("openid","o8d06w-sgpEBE0ZxdbsyksZLBQ6I");
*/
        String openid = (String) session.getAttribute("openid");
        if (openid == null) {
            response.sendRedirect(request.getContextPath() + "/Wx/GetOpenId?returnUrl=" + URLEncoder.encode(request.getRequestURI(), "utf-8"));
        }
        WxResult wxResult = resultDao.getResultByUid(openid);
        //不能重复答题
        if(wxResult == null){
            modelAndView.addObject("openid",openid);
            modelAndView.setViewName("Wx/question");
        }else{
            modelAndView.setViewName("Wx/warning");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/contact")
    public ModelAndView Contact(String openid, HttpSession session, HttpServletResponse response,HttpServletRequest request) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        if(openid ==null || openid.equals("")) {
            openid = (String) session.getAttribute("openid");
            if (openid == null) {
                response.sendRedirect(request.getContextPath() + "/Wx/GetOpenId?returnUrl=" + URLEncoder.encode(request.getRequestURI(), "utf-8"));
            }
            WxResult wxResult = resultDao.getResultByUid(openid);
            //不能重复答题
            if(wxResult == null){
                modelAndView.addObject("openid",openid);
                modelAndView.setViewName("Wx/question");
            }else{
                modelAndView.setViewName("Wx/warning");
            }
        } else {
            modelAndView.addObject("openid", openid);
            modelAndView.setViewName("Wx/contact");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/wrong")
    public ModelAndView Wrong(HttpSession session , HttpServletResponse response, HttpServletRequest request, @RequestParam(required = false) String openid) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        /*String openid = (String) session.getAttribute("openid");
        String redirect_url = request.getContextPath() + "/Wx/GetOpenId?returnUrl=" + URLEncoder.encode(request.getRequestURI(), "utf-8");
        System.out.print(redirect_url);
        if (openid == null) {
            response.sendRedirect(redirect_url);
        }
        WxResult wxResult = resultDao.getResultByUid(openid);
        if(wxResult == null){
            modelAndView.addObject("openid",openid);
            modelAndView.setViewName("Wx/question");
        }else{
            modelAndView.setViewName("Wx/warning");
        }*/
        if(openid ==null || openid.equals("")) {
            openid = (String) session.getAttribute("openid");
            if (openid == null) {
                response.sendRedirect(request.getContextPath() + "/Wx/GetOpenId?returnUrl=" + URLEncoder.encode(request.getRequestURI(), "utf-8"));
            }
            WxResult wxResult = resultDao.getResultByUid(openid);
            //不能重复答题
            if(wxResult == null){
                modelAndView.addObject("openid",openid);
                modelAndView.setViewName("Wx/question");
            }else{
                modelAndView.setViewName("Wx/warning");
            }
        } else {
            modelAndView.addObject("openid", openid);
            modelAndView.setViewName("Wx/wrong");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/warning")
    public ModelAndView Warning(HttpSession session , HttpServletResponse response, HttpServletRequest request) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        String openid = (String) session.getAttribute("openid");
        String redirect_url = request.getContextPath() + "/Wx/GetOpenId?returnUrl=" + URLEncoder.encode(request.getRequestURI(), "utf-8");
        System.out.print(redirect_url);
        if (openid == null) {
            response.sendRedirect(redirect_url);
        }
        WxResult wxResult = resultDao.getResultByUid(openid);
        if(wxResult == null){
            modelAndView.addObject("openid",openid);
            modelAndView.setViewName("Wx/question");
        }
        modelAndView.setViewName("Wx/warning");
        return modelAndView;
    }

    @RequestMapping(value = "/complete")
    public ModelAndView complete(HttpSession session , HttpServletResponse response, HttpServletRequest request) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        String openid = (String) session.getAttribute("openid");
        String redirect_url = request.getContextPath() + "/Wx/GetOpenId?returnUrl=" + URLEncoder.encode(request.getRequestURI(), "utf-8");
        System.out.print(redirect_url);
        if (openid == null) {
            response.sendRedirect(redirect_url);
        }
        WxResult wxResult = resultDao.getResultByUid(openid);
        if(wxResult == null){
            modelAndView.addObject("openid",openid);
            modelAndView.setViewName("Wx/question");
        }else{
            modelAndView.setViewName("Wx/warning");
        }
        modelAndView.setViewName("Wx/complete");
        return modelAndView;
    }

    /**
     *
     * @param request
     * @param validity 1 表示全对
     * @param openid
     * @param getPrize
     * @param name
     * @param tel
     * @param address
     * @return
     */
    @RequestMapping(value = "submitResult",method = RequestMethod.POST)
    public String submitResult(HttpServletRequest request, Integer validity , String openid ,Integer getPrize, String name, String tel, String address){
        String fetchAddress = "徐汇区古宜路125号群众艺术馆文化遗产办公室2楼主题图书馆";
        WxUser wxUser = userDao.getByOpenID(openid);
        if(wxUser !=null){
            WxResult wxResult = resultDao.getResultByUid(openid);
            if(wxResult == null){
                wxResult = new WxResult();
                wxResult.setOpenid(openid);
                wxResult.setWxUser(wxUser);
                wxResult.setStatus(IConst.incomplete);
                wxResult.setValidity(validity);
                wxResult.setGetPrize(getPrize);
                if(validity == 1){
                    if(getPrize.equals(IConst.FETCH))
                        wxResult.setFetchAddress(fetchAddress);
                    else{
                        wxResult.setName(name);
                        wxResult.setAddress(address);
                        wxResult.setTel(tel);
                    }
                }
                resultDao.save(wxResult);
            }
        }
        return "redirect:/Wx/complete";//成功跳转
    }

    @RequestMapping(value = "wrongResult",method = RequestMethod.POST)
    @ResponseBody
    public String wrongResult(Integer validity , String openid){
        WxUser wxUser = userDao.getByOpenID(openid);
        if(wxUser !=null){
            WxResult wxResult = resultDao.getResultByUid(openid);
            if(wxResult == null){
                wxResult = new WxResult();
                wxResult.setOpenid(openid);
                wxResult.setWxUser(wxUser);
                wxResult.setStatus(IConst.incomplete);
                wxResult.setValidity(validity);
                resultDao.save(wxResult);
            }
        }
        return "success";//成功跳转
    }
}
