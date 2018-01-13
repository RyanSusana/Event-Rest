package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;

@Getter
@EqualsAndHashCode(of = "userId")
public class Attendee {
    @JsonProperty("user_id")
    private final String userId;

    @JsonProperty("sign_up_date")
    private final Date signUpDate;

    @JsonCreator
    public Attendee(@JsonProperty("user_id") String userId, @JsonProperty("sign_up_date") Date signUpDate) {
        this.userId = userId;
        this.signUpDate = signUpDate;
    }
}
