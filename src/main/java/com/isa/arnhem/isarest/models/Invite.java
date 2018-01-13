package com.isa.arnhem.isarest.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Invite {

    private String id, inviter, invitee;

    private String eventId;

    private Date inviteDate;
}
