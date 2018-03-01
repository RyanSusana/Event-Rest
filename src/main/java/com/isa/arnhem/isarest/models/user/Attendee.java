package com.isa.arnhem.isarest.models.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class Attendee {
    @JsonProperty("user_id")
    private final String userId;

    @JsonProperty("sign_up_date")
    private final Date signUpDate;

    @JsonProperty("plus")
    @Setter
    private int plus;

    @JsonCreator
    private Attendee(Map<String, Object> props) {
        Optional<String> userId = Optional.of((String) props.get("user_id"));
        Optional<Date> signUpDate = Optional.of((Date) props.get("sign_up_date"));
        Optional<Integer> plus = Optional.ofNullable((Integer) props.get("plus"));

        if (!userId.isPresent() || !signUpDate.isPresent()) {
            throw new IllegalArgumentException("Must have a user_id and a sign_up_date");
        }
        this.userId = userId.get();
        this.signUpDate = signUpDate.get();

        this.plus = plus.orElse(0);
    }


    public static Attendee of(String userId, Date signUpDate) {
        Map<String, Object> props = new HashMap<>();
        props.put("user_id", userId);
        if (signUpDate == null) {
            props.put("sign_up_date", Calendar.getInstance().getTime());
        } else {
            props.put("sign_up_date", signUpDate);
        }
        return new Attendee(props);
    }

    public static Attendee of(String userId, Date signUpDate, int plus) {
        Map<String, Object> props = new HashMap<>();
        props.put("user_id", userId);
        props.put("sign_up_date", signUpDate);
        props.put("plus", plus);
        return new Attendee(props);
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
            return (((User) obj).getId().equals(this.getUserId()));
        } else if (obj instanceof Attendee) {
            return ((Attendee) obj).getUserId().equals(this.getUserId());
        } else {
            return Objects.equals(this, obj);
        }
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.getUserId());
    }
}