package com.isa.arnhem.isarest.repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.isa.arnhem.isarest.models.Event;
import com.isa.arnhem.isarest.models.EventSet;
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

    public Optional<Event> findByEventId(String eventId) {
        return Optional.ofNullable(findOne("{_id: #}", eventId).as(Event.class));
    }

    @Override
    public void update(Event item) {
        getCollection().save(item);
    }

    public EventSet getAll() {
        return new EventSet(Sets.newTreeSet(Lists.newArrayList(find().as(Event.class).iterator())));
    }
}
