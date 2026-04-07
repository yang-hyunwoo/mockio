package com.mockio.auth_service.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "MOCKIO auth API 명세서", description = "## \uD83D\uDD10 인증 서비스 API\n" +
        "\n" +
        "이 API는 사용자 인증 및 토큰 관리를 담당합니다.\n" +
        "\n" +
        "### \uD83D\uDCCC 주요 기능\n" +
        "- 로그인 / 로그아웃\n" +
        "- 토큰 재발급\n" +
        "- 사용자 정보 조회\n" +
        "\n" +
        "### \uD83D\uDD11 인증 방식\n" +
        "- Bearer Token (JWT)\n" +
        "\n" +
        "### ⚠\uFE0F 주의사항\n" +
        "- 토큰은 Authorization 헤더로 전달해야 합니다.", version = "v1"))
@Configuration
public class OpenAPIConfig {

}
