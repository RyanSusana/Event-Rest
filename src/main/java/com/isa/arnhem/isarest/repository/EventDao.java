package com.isa.arnhem.isarest.repository;

import com.isa.arnhem.isarest.models.event.Event;
import com.isa.arnhem.isarest.models.event.EventList;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class EventDao extends CrudDao<Event> {

    @Autowired
    public EventDao(Jongo jongo) {
        super(jongo, "events");
    }

    public Optional<Event> findById(String eventId) {
        return Optional.ofNullable(findOne("{_id: #}", eventId).as(Event.class));
    }

    private Optional<Event> findBySlug(String slug) {
        return Optional.ofNullable(findOne("{slug: #, date: {$gt: #}}", slug, EventList.getYesterday().getTime()).as(Event.class));
    }

    public Optional<Event> findByString(final String slug) {
        return findById(slug).or(() -> findBySlug(slug));
    }

    @Override
    public void update(Event item) {
        getCollection().save(item);
    }


    public EventList getAll() {
        final EventList events = new EventList();
        find().as(Event.class).iterator().forEachRemaining(events::add);
        return events;
    }
}
