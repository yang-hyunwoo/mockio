package com.mockio.auth_service.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.dto.response.SessionValidateResponse;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class JwtClaimUtil {
    private static final ObjectMapper om = new ObjectMapper();

    public static List<String> extractRoles(JwtClaimsSet claims, String clientId) {
        List<String> roles = new ArrayList<>();

        Object realmAccess = claims.getClaims().get("realm_access");
        if (realmAccess instanceof Map<?, ?> m) {
            Object r = m.get("roles");
            if (r instanceof List<?> list) list.forEach(x -> roles.add(String.valueOf(x)));
        }

        Object resourceAccess = claims.getClaims().get("resource_access");
        if (resourceAccess instanceof Map<?, ?> ra) {
            Object client = ra.get(clientId);
            if (client instanceof Map<?, ?> cm) {
                Object cr = cm.get("roles");
                if (cr instanceof List<?> list) list.forEach(x -> roles.add(String.valueOf(x)));
            }
        }
        return roles;
    }

    public static SessionValidateResponse parseSub(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode node = om.readTree(payload);
            JsonNode attributes = node.path("profile").path("attributes");

            String userId = node.get("sub").asText();
            String provider = attributes.path("provider").asText(null);
            String username = attributes.path("name").asText(null);

            return new SessionValidateResponse(userId, username, provider, List.of());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT format", e);
        }
    }
}
