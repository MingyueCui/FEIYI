package com.springapp.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 11369 on 2016/10/26.
 */
@Controller
@RequestMapping("/Wx")
public class WxRouteController extends BaseController{
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    public ModelAndView modelAndView(){
        ModelAndView modelAndView=new ModelAndView("result");
        return modelAndView;
    }
}
