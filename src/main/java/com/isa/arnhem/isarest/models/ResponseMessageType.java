package com.isa.arnhem.isarest.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessageType {
    CLIENT_ERROR(400), SUCCESSFUL(200), SERVER_ERROR(500), UNAUTHORIZED(401), NOT_FOUND(404), ACCEPTED(202);

    private final int status;

}
