package com.lihebin.quartz.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lihebin on 03/01/2018.
 */
@Configuration
public class TokenInterceptor extends HandlerInterceptorAdapter {
    private final Logger log = LoggerFactory.getLogger(getClass());





    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String token;
//        try {
//            token = request.getHeader("token");
//        } catch (Exception e) {
//            throw new BackendException(Code.CODE_TIME_OUT, "无令牌");
//        }
//        log.info("TokenInterceptor:{}", token);
////        String[] param = token.split("-");
////        String method = request.getRequestURI();
//        String username = redisDao.getValue(token);
//        if (StringUtil.empty(username)) {
//            throw new BackendException(Code.CODE_TIME_OUT, "登录超时");
//        }
//
        return true;
    }


}
