package com.aitest.common.util;

import java.util.UUID;

public class TokenUtil {
    private static final String SECRET = "ai-test-platform-secret-key-2024";

    /**
     * 生成用户认证令牌
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return 返回Base64编码后的令牌字符串
     */
    public static String generateToken(Long userId, String username) {
        // 将用户ID、用户名和时间戳组合成数据字符串
        String data = userId + ":" + username + ":" + System.currentTimeMillis();
        // 将数据字符串与密钥组合后进行Base64编码，返回令牌
        return Base64Util.encode(data + ":" + SECRET);
    }

    /**
     * 验证令牌的有效性
     *
     * @param token 需要验证的令牌字符串
     * @return 如果令牌有效返回true，否则返回false
     */
    public static boolean validateToken(String token) {
        // 检查令牌是否为null或空字符串
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            // 使用Base64Util解码令牌
            String decoded = Base64Util.decode(token);
            // 将解码后的字符串按冒号分割成数组
            String[] parts = decoded.split(":");
            // 检查分割后的数组长度是否为4
            return parts.length == 4;
        } catch (Exception e) {
            // 捕获所有异常并返回false
            return false;
        }
    }

    /**
     * 从令牌中解析用户ID
     *
     * @param token 包含用户ID的令牌字符串，格式为"userID:其他信息"
     * @return 解析出的用户ID(Long类型)，如果解析失败则返回null
     */
    public static Long getUserIdFromToken(String token) {
        try {
            // 使用Base64解码令牌
            String decoded = Base64Util.decode(token);
            // 将解码后的字符串按冒号分割成数组
            String[] parts = decoded.split(":");
            // 返回分割后的第一部分，即用户ID，并转换为Long类型
            return Long.parseLong(parts[0]);
        } catch (Exception e) {
            // 如果发生任何异常，返回null
            return null;
        }
    }
}