package com.springapp.mvc;

import com.springapp.entity.Activity;
import com.springapp.entity.BookInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by 11369 on 2017/2/22.
 */
@Controller
@RequestMapping(value = "Wx")
public class BookController extends BaseController {
    @RequestMapping(value = "bookActivity/{id}", method = RequestMethod.GET)
    public ModelAndView bookActivity(@PathVariable("id") Long id , HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        Activity activity = activityDao.get(Activity.class, id);
        String openid = (String) session.getAttribute("openid");
        if (openid == null) {
            response.sendRedirect(request.getContextPath() + "/Wx/GetOpenId?returnUrl=" + URLEncoder.encode(request.getRequestURI(), "utf-8"));
        }
        BookInfo bookInfo = bookInfoDao.getByOpenid(openid);
        modelAndView.addObject("openid", openid);
        modelAndView.addObject("bookInfo", bookInfo);
        modelAndView.addObject("activity", activity);
        modelAndView.setViewName("Wx/bookActivity");
        return  modelAndView;
    }

    @RequestMapping(value = "bookActivity", method = RequestMethod.POST)
    @ResponseBody
    public String bookActivity(Long aid, String openid, String username, String phoneNum){
        try {
            BookInfo bookInfo = new BookInfo();
            bookInfo.setAid(aid);
            bookInfo.setOpenid(openid);
            bookInfo.setName(username);
            bookInfo.setPhoneNum(phoneNum);
            bookInfoDao.save(bookInfo);
            return "success";
        }catch (Exception e){
            return "fail";
        }
    }
}

