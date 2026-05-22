package com.aitest.common.util;

import java.util.UUID;

public class TokenUtil {
    private static final String SECRET = "ai-test-platform-secret-key-2024";

    public static String generateToken(Long userId, String username) {
        String data = userId + ":" + username + ":" + System.currentTimeMillis();
        return Base64Util.encode(data + ":" + SECRET);
    }

    public static boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            String decoded = Base64Util.decode(token);
            String[] parts = decoded.split(":");
            return parts.length == 4;
        } catch (Exception e) {
            return false;
        }
    }

    public static Long getUserIdFromToken(String token) {
        try {
            String decoded = Base64Util.decode(token);
            String[] parts = decoded.split(":");
            return Long.parseLong(parts[0]);
        } catch (Exception e) {
            return null;
        }
    }
}