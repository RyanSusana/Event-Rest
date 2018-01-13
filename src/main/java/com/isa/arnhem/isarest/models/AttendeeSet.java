package com.isa.arnhem.isarest.models;

import java.util.ArrayList;
import java.util.HashSet;

public class AttendeeSet extends HashSet<Attendee> {

    public Attendee getAttendee(String userId){
        for (Attendee attendee : this) {
            if(attendee.getUserId().equals(userId)){
                return attendee;
            }
        }
        return null;
    }
    public Attendee getAttendee(User user){
        return getAttendee(user.getId());
    }
}
