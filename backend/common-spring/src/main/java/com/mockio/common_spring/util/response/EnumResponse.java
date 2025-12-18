package com.mockio.common_spring.util.response;

public record EnumResponse(
        String code,
        String label
) {
    public static EnumResponse of(Enum<?> e, String label) {
        return new EnumResponse(e.name(), label);
    }

    public static EnumResponse of(String code, String label) {
        return new EnumResponse(code, label);
    }
}