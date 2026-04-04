package com.mockio.core_service.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "MOCKIO core API 명세서", description = "" +
        "## \uD83D\uDCE6 Core 서비스 API\n" +
        "\n" +
        "이 서비스는 사용자, 인터뷰, 피드백 등 \n" +
        "핵심 비즈니스 로직을 처리합니다.\n" +
        "\n" +
        "---\n" +
        "\n" +
        "### \uD83D\uDCCC 주요 기능\n" +
        "\n" +
        "- \uD83D\uDC64 사용자 관리 (회원 정보 조회/수정)\n" +
        "- \uD83C\uDFA4 인터뷰 관리 (질문/답변)\n" +
        "- \uD83D\uDCDD 피드백 관리 (평가 및 코멘트)\n" +
        "\n" +
        "---\n" +
        "\n" +
        "### \uD83D\uDD11 인증 방식\n" +
        "\n" +
        "- Authorization: Bearer {accessToken}\n" +
        "- 인증이 필요한 API는 JWT 토큰을 포함해야 합니다.\n" +
        "\n" +
        "---\n" +

        "## 📝 참고 사항\n\n" +
        "- 모든 응답은 JSON 형식으로 반환됩니다.\n" +
        "- 인증 실패 시 공통 에러 포맷이 반환됩니다.\n"
        , version = "v1"))
@Configuration
public class OpenAPIConfig {
}
