package com.isa.arnhem.isarest.models;

import java.util.HashSet;
import java.util.Optional;

public class AttendeeSet extends HashSet<Attendee> {

    public Optional<Attendee> getAttendee(String userId) {
        for (Attendee attendee : this) {
            if (attendee.getUserId().equals(userId)) {
                return Optional.of(attendee);
            }
        }
        return Optional.empty();
    }

    public boolean containsUser(User user) {
        return getAttendee(user).isPresent();
    }

    public Optional<Attendee> getAttendee(User user) {
        return getAttendee(user.getId());
    }


}
