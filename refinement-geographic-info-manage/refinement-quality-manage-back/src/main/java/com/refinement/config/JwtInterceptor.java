package com.refinement.config;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.nimbusds.jose.JWSObject;
import com.refinement.http.DefaultResponseCode;
import com.refinement.http.WrapMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

@Slf4j
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ParseException {
        request.getMethod();
        // 获取请求头信息authorization信息
        final String authHeader = request.getHeader("Authorization");
        if (StringUtils.isBlank(authHeader)) {
            returnJson(response, JSON.toJSONString(WrapMapper.wrap(DefaultResponseCode.NOT_LOGGED_IN)));
            return false;
        }
        //获取token
        String token;
        if (authHeader.startsWith("Bearer")) {
            token = authHeader.substring(7);
        } else {
            token = authHeader;
        }
        JWSObject jwsObject = JWSObject.parse(token);
        String userStr = jwsObject.getPayload().toString();
        JSONObject jsonObject = JSONUtil.parseObj(userStr);
        //用户id
        MyThreadLocal.getInstance().set(jsonObject.get("id"));
        return true;
    }

    private void returnJson(HttpServletResponse response, String json) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
