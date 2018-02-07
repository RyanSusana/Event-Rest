package com.isa.arnhem.isarest.models;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

public class EventSet extends TreeSet<Event> {

    public EventSet() {
        super();
    }

    public EventSet(Collection<Event> list) {
        super(list);
    }

    public EventSet filterOutPastEvents() {
        return new EventSet(this.stream().filter((event) -> event.getDate().after(getYesterday())).collect(Collectors.toSet()));
    }

    public EventSet filterOutFutureEvents() {
        return new EventSet(this.stream().filter((event) -> event.getDate().before(getYesterday())).collect(Collectors.toSet()));
    }

    private Date getYesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
    public static EventSet of(Iterable<Event> events){
        return new EventSet(Sets.newTreeSet(events));

    }
}
