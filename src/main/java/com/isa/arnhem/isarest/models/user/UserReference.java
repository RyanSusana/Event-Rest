package com.isa.arnhem.isarest.models.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserReference{

    @JsonProperty("_id")
    private String userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("full_name")
    private String fullName;


    @JsonCreator
    private UserReference(@JsonProperty("_id") String userId, @JsonProperty("username") String username,
                          @JsonProperty("email") String email, @JsonProperty("full_name") String fullName) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
    }

    public static UserReference of(User user) {
        return new UserReference(user.getId(), user.getUsername(), user.getEmail(), user.getFullName());
    }
}
