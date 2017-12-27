package com.isa.arnhem.isarest.dao;

import com.isa.arnhem.isarest.models.Event;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventDao extends CrudDao<Event> {

    @Autowired
    public EventDao(Jongo jongo) {
        super(jongo, "events");
    }


}
