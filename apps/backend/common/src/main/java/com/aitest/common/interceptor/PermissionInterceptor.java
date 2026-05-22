package com.aitest.common.interceptor;

import com.aitest.common.util.JwtUtil;
import com.aitest.common.util.PermissionContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashSet;

/**
 * 权限拦截器
 * 从请求头获取token，解码获取用户信息并设置到PermissionContext
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(PermissionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractToken(request);

        if (token != null && !token.isEmpty()) {
            try {
                Claims claims = JwtUtil.extractClaims(token);
                Long userId = claims.get("userId", Long.class);
                String username = claims.getSubject();

                if (userId != null && username != null) {
                    PermissionContext.setUserInfo(userId, username, new HashSet<>(), new HashSet<>(), null);
                    log.debug("设置用户权限上下文: userId={}, username={}", userId, username);
                }
            } catch (Exception e) {
                log.warn("解析Token失败: {}", e.getMessage());
                PermissionContext.clear();
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        PermissionContext.clear();
    }

    /**
     * 从请求头提取Token
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
