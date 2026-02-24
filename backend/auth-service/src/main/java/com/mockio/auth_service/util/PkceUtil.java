package com.mockio.auth_service.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class PkceUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    private PkceUtil() {}

    // RFC7636: code_verifier는 43~128 chars
    public static String generateCodeVerifier() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return base64Url(bytes);
    }

    public static String generateState() {
        byte[] bytes = new byte[16];
        RANDOM.nextBytes(bytes);
        return base64Url(bytes);
    }

    public static String toCodeChallengeS256(String verifier) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(verifier.getBytes(StandardCharsets.US_ASCII));
            return base64Url(digest);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create code_challenge", e);
        }
    }

    private static String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
