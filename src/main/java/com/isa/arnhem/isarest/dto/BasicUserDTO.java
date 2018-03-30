package com.isa.arnhem.isarest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.isa.arnhem.isarest.models.user.User;
import com.isa.arnhem.isarest.models.user.UserReference;
import lombok.Getter;

@Getter
public class BasicUserDTO {
    @JsonProperty("_id")
    private final String id;

    @JsonProperty("username")
    private final String username;

    @JsonProperty("email")
    private final String email;

    @JsonProperty("full_name")
    private final String fullName;

    public BasicUserDTO(UserReference userReference) {
        this.id = userReference.getUserId();
        this.username = userReference.getUsername();
        this.email = userReference.getEmail();
        this.fullName = userReference.getFullName();
    }

    public BasicUserDTO(User user) {
        this(user.getReference());
    }
}
