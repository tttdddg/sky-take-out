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
import java.io.PrintWriter;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中读取Authorization（前端实际传递的Token头）
        String authHeader = request.getHeader("Authorization");
        log.info("接收到的Authorization请求头：{}", authHeader);

        //2、处理Token格式：提取真正的Token（去掉Bearer前缀）
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // 去掉"Bearer "前缀（7个字符）
        }

        //3、校验令牌
        try {
            log.info("jwt校验:{}", token);
            // 校验Token有效性（使用与生成时相同的密钥）
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("当前员工id：{}", empId); // 修复日志占位符错误
            BaseContext.setCurrentId(empId);
            //4、校验通过，放行
            return true;
        } catch (Exception ex) {
            log.error("JWT校验失败：{}", ex.getMessage());
            //5、校验失败，返回标准401响应（JSON格式）
            response.setStatus(401);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            // 返回标准JSON，前端能明确感知鉴权失败
            writer.write("{\"code\":401,\"msg\":\"Token无效或未携带有效Token\"}");
            writer.flush();
            writer.close();
            return false;
        }
    }
}