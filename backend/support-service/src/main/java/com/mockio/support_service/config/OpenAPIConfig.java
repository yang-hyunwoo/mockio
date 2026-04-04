package com.mockio.support_service.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "MOCKIO Support API 명세서", description = "" +
        "## \uD83D\uDCE6 Support 서비스 API\n" +
        "\n" +
        "FAQ, 알림, 파일, 공지 등 **Support 도메인 비즈니스 로직**을 처리하는 서비스입니다.\n\n" +
        "\n" +
        "---\n" +
        "\n" +
        "- ### 📁 파일 관리 (내부)\n" +
        " - 파일 업로드 및 관리\n\n" +
        "\n" +
        "- ### ❓ FAQ 관리\n" +
        " - FAQ 목록 조회\n\n" +
        "\n" +
        "- ### 🔔 알림 관리\n" +
        " - 사용자 알림 처리\n\n" +

        "- ### 📁 공지 관리\n" +
        " - 공지 목록 조회\n\n" +
        " - 공지 상세 조회\n\n" +

        "---\n" +
        "\n" +
        "### \uD83D\uDD11 인증 방식\n" +
        "\n" +
        "- Authorization: Bearer {accessToken}\n" +
        "- 인증이 필요한 API는 JWT 토큰을 포함해야 합니다.\n" +
        "\n" +
        "---\n" +

        "- ## 📝 참고 사항\n\n" +
        "- 모든 응답은 JSON 형식으로 반환됩니다.\n" +
        "- 인증 실패 시 공통 에러 포맷이 반환됩니다.\n"
        , version = "v1"))
@Configuration
public class OpenAPIConfig {
}
