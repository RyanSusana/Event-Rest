package com.isa.arnhem.isarest.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Getter
@Setter
public class Notification implements Comparable<Notification> , Identifiable{

    private String id;
    private boolean seen = false;

    private String userId;

    private String message;

    private NotificationType type;

    private int priority = 0;

    private ZonedDateTime date;

    @Override
    @NotNull
    public int compareTo(Notification o) {
        int priorityCompare = Integer.compare(this.priority, o.priority);

        if (priorityCompare == 0) {
            return this.date.compareTo(o.date);
        }

        return priorityCompare;
    }
}
