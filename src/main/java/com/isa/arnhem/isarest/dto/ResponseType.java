package com.isa.arnhem.isarest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseType {
    CLIENT_ERROR(400), SUCCESSFUL(200), SERVER_ERROR(500), UNAUTHORIZED(401), NOT_FOUND(404), ACCEPTED(202), REDIRECT(301);

    private final int status;

    public boolean isSuccessful() {
        return status >= 200 && status < 300;
    }
}
