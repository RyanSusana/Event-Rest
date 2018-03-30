package com.isa.arnhem.isarest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.isa.arnhem.isarest.models.user.User;
import com.isa.arnhem.isarest.models.user.UserType;

import java.util.Date;

public class UserDTO extends BasicUserDTO {
    @JsonProperty("type")
    private final UserType type;


    @JsonProperty("creation_date")
    private final Date creationDate;

    @JsonProperty("activated")
    private final boolean activated;

    public UserDTO(User user) {
        super(user);
        this.type = user.getType();
        this.creationDate = user.getCreationDate();
        this.activated = user.isActivated();
    }
}
