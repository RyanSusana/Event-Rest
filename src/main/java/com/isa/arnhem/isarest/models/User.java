package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    @JsonProperty("user_id")
    private String id;

    private String username, email, password;

    private UserType type;

    @JsonProperty("creation_date")
    private final LocalDateTime creationDate;

    public User() {
        id = UUID.randomUUID().toString();
        creationDate = LocalDateTime.now();
    }

    @JsonCreator
    public User(@JsonProperty("user_id") String id, @JsonProperty("username") String username, @JsonProperty("email") String email, @JsonProperty("password") String password, @JsonProperty("type") UserType type, @JsonProperty("creation_date") LocalDateTime creationDate) {

        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.type = type;
        this.creationDate = creationDate;
    }


    public User(String username, String email, String password, UserType type) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type +
                '}';
    }

}
