package com.isa.arnhem.isarest.dto;

import java.util.Map;

public interface Response {

    ResponseType getType();

    String getMessage();

    Map<String, Object> getProperties();

    default boolean isSuccessful() {
        return !(this instanceof Exception) && getType().isSuccessful();
    }
}
