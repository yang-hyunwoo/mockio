package com.mockio.auth_service.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.dto.response.SessionValidateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtClaimUtil {
    private static final ObjectMapper om = new ObjectMapper();

    private final JwtDecoder keycloakJwtDecoder;
    @Value("${keycloak.client-id}")
    private String clientId;

    public SessionValidateResponse verifyAndExtract(String jwt) {
        Jwt decoded = keycloakJwtDecoder.decode(jwt); // ✅ 서명/exp 검증

        String userId = decoded.getSubject();
        String email = decoded.getClaimAsString("email");

        // provider/username은 당신이 커스텀 클레임(profile.attributes.*)을 쓰는 것 같아서 그대로 꺼내되 null-safe
        Map<String, Object> profile = decoded.getClaim("profile");
        String provider = null;
        String username = null;
        if (profile != null) {
            Object attrsObj = profile.get("attributes");
            if (attrsObj instanceof Map<?, ?> attrs) {
                Object p = attrs.get("provider");
                if (p != null) provider = String.valueOf(p);

                Object n = attrs.get("name");
                if (n != null) username = String.valueOf(n);
            }
        }

        List<String> roles = extractRoles(decoded.getClaims(), clientId);

        return new SessionValidateResponse(userId, username, email, provider, roles);
    }

    public static List<String> extractRoles(Map<String, Object> claims, String clientId) {
        List<String> roles = new ArrayList<>();

        Object realmAccess = claims.get("realm_access");
        if (realmAccess instanceof Map<?, ?> m) {
            Object r = m.get("roles");
            if (r instanceof List<?> list) list.forEach(x -> roles.add(String.valueOf(x)));
        }

        Object resourceAccess = claims.get("resource_access");
        if (resourceAccess instanceof Map<?, ?> ra) {
            Object client = ra.get(clientId);
            if (client instanceof Map<?, ?> cm) {
                Object cr = cm.get("roles");
                if (cr instanceof List<?> list) list.forEach(x -> roles.add(String.valueOf(x)));
            }
        }
        return roles;
    }


}
