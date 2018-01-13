package com.isa.arnhem.isarest.repository;

import com.isa.arnhem.isarest.models.Event;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventDao extends CrudDao<Event> {

    @Autowired
    public EventDao(Jongo jongo) {
        super(jongo, "events");
    }

    public Event findByEventId(String eventId) {
        return findOne("{event_id: #}", eventId).as(Event.class);
    }

    @Override
    public void update(Event item) {
        getCollection().update("{event_id: #}", item.getId()).with(item);
    }
}