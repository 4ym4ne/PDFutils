package com.pdfutils.domain.DTO.login;

public record LoginResponse(String accessToken, Long expiresIn) {
}
