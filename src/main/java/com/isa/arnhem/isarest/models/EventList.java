package com.isa.arnhem.isarest.models;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class EventList extends ArrayList<Event> {

    public EventList() {
        super();
    }

    public EventList(Collection<Event> list) {
        super();
        this.addAll(list);
    }

    public static Date getYesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static EventList of(Iterable<Event> events) {
        return new EventList(Sets.newTreeSet(events));

    }

    public EventList filterOutPastEvents() {
        return new EventList(this.stream().filter((event) -> event.getDate().after(getYesterday())).collect(Collectors.toSet()));
    }

    public EventList filterOutFutureEvents() {
        return new EventList(this.stream().filter((event) -> event.getDate().before(getYesterday())).collect(Collectors.toSet()));
    }
}
