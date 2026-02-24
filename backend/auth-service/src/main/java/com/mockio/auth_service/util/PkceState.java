package com.mockio.auth_service.util;

import java.io.Serializable;

public record PkceState(String codeVerifier) implements Serializable {}
