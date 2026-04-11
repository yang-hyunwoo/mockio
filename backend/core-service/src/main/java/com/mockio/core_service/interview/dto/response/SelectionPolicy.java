package com.mockio.core_service.interview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record SelectionPolicy(

      @Schema(description = "기본질문갯수", example = "1")
      int basicCount,

      @Schema(description = "기본->심화질문갯수", example = "1")
      int linkedHardCount,

      @Schema(description = "심화질문갯수", example = "1")
      int hardCount

) {}
