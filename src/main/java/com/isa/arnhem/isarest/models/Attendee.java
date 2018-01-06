package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Objects;

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

    public String getUserId() {
        return userId;
    }

    public Date getSignUpDate() {
        return signUpDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        Attendee that = (Attendee) (o);

        return userId.equals(that.userId);
}

    @Override
    public int hashCode() {

        return Objects.hash(userId, signUpDate);
    }
}
