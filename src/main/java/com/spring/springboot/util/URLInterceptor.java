package com.spring.springboot.util;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 拦截器拦截请求
 */
//重写方法中内容根据具体项目来
public class URLInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String uri = request.getRequestURI();
        //拦截interceptor请求
        if(uri.contains("interceptorRequest")){
            System.out.println("请求被拦截："+uri);
            return false;
        }

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