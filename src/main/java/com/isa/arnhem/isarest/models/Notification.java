package com.isa.arnhem.isarest.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class Notification implements Comparable<Notification>, Identifiable {

    private String id;
    private boolean seen = false;

    private String userId;

    private String message;

    private NotificationType type;

    private Date date;

    @Override
    @NotNull
    public int compareTo(Notification o) {
        int priorityCompare = Integer.compare(this.type.getPriority(), o.type.getPriority());

        if (priorityCompare == 0) {
            return this.date.compareTo(o.date);
        }

        return priorityCompare;
    }
}
