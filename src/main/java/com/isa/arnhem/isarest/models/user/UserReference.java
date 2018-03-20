package com.isa.arnhem.isarest.models.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isa.arnhem.isarest.models.Identifiable;
import lombok.Getter;
import lombok.Setter;

import javax.websocket.server.ServerEndpoint;

@Getter
public class UserReference{

    @JsonProperty("id")
    private String userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("full_name")
    private String fullName;


    @JsonCreator
    private UserReference(String userId, String username, String email, String fullName) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
    }

    public static UserReference of(User user){
        return new UserReference(user.getId(), user.getUsername(), user.getEmail(), user.getFullName());
    }
}
