package com.isa.arnhem.isarest.dao;

import com.isa.arnhem.isarest.models.Event;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventDao extends CrudDao<Event> {
    public EventDao(MongoClient client) {
        super(client, "events");
    }

    @Autowired
    public EventDao(IsaMongoClient client) {
        super(client, "events");
    }

}
