package com.action;
import  java.util.*;
import  com.service.*;
import  com.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class indexController {

    @Value("${spring.datasource.url}")
    private String url;

    @Autowired
    private  IEmpService es;
    @Autowired
    private  IDeptService ds;
    @RequestMapping(value ="/index")
    public ModelAndView index(ModelAndView mvo){
        System.out.println("--------index +-----url ="+url);
        List<Dept> list =ds.selectAll();
        for (Dept p: list){
            System.out.println(p);
        }
        mvo.addObject("index");
        return mvo;
    }
/*    @RequestMapping(value = "/testAction")
    public String testAction(HttpServletRequest request){
        String content = request.getParameter("editorValue");
        System.out.println("editorValue : /n"+content);
        request.setAttribute("content", content);
        request.getRequestDispatcher("success.jsp").forward(request, response);
        return "success";
    }*/
}
