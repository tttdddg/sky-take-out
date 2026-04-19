package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.io.PrintWriter;

/**
 * JWT admin token interceptor.
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        try {
            // 1) Read token by configured header name first.
            String configuredTokenName = jwtProperties.getAdminTokenName();
            String token = request.getHeader(configuredTokenName);

            // 2) Compatibility: common header names.
            if (isBlank(token)) {
                token = request.getHeader("Authorization");
            }
            if (isBlank(token)) {
                token = request.getHeader("token");
            }
            if (isBlank(token)) {
                token = request.getParameter("token");
            }
            if (isBlank(token)) {
                token = readTokenFromCookies(request, configuredTokenName);
            }

            // 3) Compatibility: Bearer prefix.
            if (!isBlank(token) && token.regionMatches(true, 0, "Bearer ", 0, 7)) {
                token = token.substring(7).trim();
            }

            if (isBlank(token)) {
                throw new IllegalArgumentException("JWT token is missing in request headers");
            }

            log.info("jwt校验 tokenName={}, token={}", configuredTokenName, token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("当前员工id: {}", empId);
            BaseContext.setCurrentId(empId);
            return true;
        } catch (Exception ex) {
            log.error("JWT校验失败: {}", ex.getMessage());
            response.setStatus(401);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"code\":401,\"msg\":\"Token无效或未携带有效Token\"}");
            writer.flush();
            writer.close();
            return false;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String readTokenFromCookies(HttpServletRequest request, String configuredTokenName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie == null || isBlank(cookie.getName()) || isBlank(cookie.getValue())) {
                continue;
            }
            String name = cookie.getName();
            if (name.equals(configuredTokenName) || "token".equalsIgnoreCase(name) || "Authorization".equalsIgnoreCase(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
