package com.isa.arnhem.isarest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseMessage extends ReturnedObject implements Response {

    private final String message;


    @NonNull
    private final ResponseType type;


    @Singular
    private final Map<String, Object> properties;



}
