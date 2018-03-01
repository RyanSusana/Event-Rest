package com.isa.arnhem.isarest.models.event;

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

    private EventList(Collection<Event> list) {
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
        return this.stream().filter((event) -> event.getDate().after(getYesterday())).distinct().collect(Collectors.toCollection(EventList::new));
    }

    public EventList filterOutFutureEvents() {
        return this.stream().filter((event) -> event.getDate().before(getYesterday())).distinct().collect(Collectors.toCollection(EventList::new));
    }
}
