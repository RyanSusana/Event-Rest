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
    @JsonProperty("message")
    private final String message;

    @JsonProperty("message_type")
    @NonNull
    private final ResponseType type;

    @JsonProperty("properties")
    @Singular
    private final Map<String, Object> properties;

    @JsonProperty("status")
    public int getStatusCode() {
        return type.getStatus();
    }

    public ResponseEntity<ResponseMessage> toResponseEntity() {
        return new ResponseEntity<>(this, HttpStatus.valueOf(type.getStatus()));
    }
}
