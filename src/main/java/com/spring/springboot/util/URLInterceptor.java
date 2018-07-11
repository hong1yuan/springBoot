package com.spring.springboot.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 拦截器拦截请求
 */
//重写方法中内容根据具体项目来
public class URLInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String uri = request.getRequestURI();
       String aa = request.getParameter("aa");
        // 拦截参数等于aa 内容等于js的请求
        Map<String,String[]> params = request.getParameterMap();
        for(String key:params.keySet()){
            String param[] = params.get(key);
            System.out.println(param[0]);
            if(param != null){
                if( StringUtils.equals(key,"aa") && StringUtils.equals(param[0],"js")){
                    System.out.println("请求被拦截："+uri);
                    System.out.println("参数："+key+"不能为js");
                    return false;
                }
            }

        }
        // 拦截请求中包含interceptorRequest
        /*if(uri.contains("interceptorRequest")){
            System.out.println("请求被拦截："+uri);
            System.out.println(aa);
            return false;
        }*/

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null){
            if(! (modelAndView.getView() instanceof RedirectView)){
                String basePath = request.getContextPath();
                //modelAndView.addObject("basePath",basePath);
                request.setAttribute("basePath",basePath);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}