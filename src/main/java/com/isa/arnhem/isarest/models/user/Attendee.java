package com.isa.arnhem.isarest.models.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
public class Attendee {
    @JsonProperty("user")
    private final UserReference user;

    @JsonProperty("sign_up_date")
    private final Date signUpDate;

    @JsonProperty("plus")
    @Setter
    private int plus;

    @JsonCreator
    private Attendee(UserReference user, Date signUpDate, int plus) {
        this.user = user;
        this.signUpDate = signUpDate;
        this.plus = plus;
    }


    public static Attendee of(User user, Date signUpDate) {
        return new Attendee(UserReference.of(user), signUpDate, 0);
    }

    public static Attendee of(User user, Date signUpDate, int plus) {
        return new Attendee(UserReference.of(user), signUpDate, plus);
    }

    public int getPlus() {
        if (plus < 0) {
            return 0;
        }
        return plus;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return (((User) obj).getId().equals(this.getUser().getUserId()));
        } else if (obj instanceof Attendee) {
            return ((Attendee) obj).getUser().getUserId().equals(this.getUser().getUserId());
        } else {
            return Objects.equals(this, obj);
        }
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.getUser().getUserId());
    }
}
