package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User implements Identifiable {

    private String id;

    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("type")
    @JsonDeserialize(using = UserTypeDeserializer.class)
    private UserType type;


    @JsonProperty("creation_date")
    private Date creationDate;

    @JsonProperty("activated")
    private boolean activated;

    @JsonProperty("banned")
    private boolean banned = false;

    public User() {
        this.creationDate = Calendar.getInstance().getTime();
        this.id = UUID.randomUUID().toString();
    }


    public User(String username, String email, String password, UserType type) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    @JsonProperty("type")
    public UserType getType() {
        if (username.equals("admin")) {
            return UserType.SUPER_ADMIN;
        }
        return type;
    }

    @JsonProperty("authorative")
    public boolean isAuthorative(){
        return getType().hasEqualRightsTo(UserType.ISA_MEMBER);
    }


}
