package com.isa.arnhem.isarest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.isa.arnhem.isarest.dao.EventDao;
import com.isa.arnhem.isarest.dao.UserDao;
import com.isa.arnhem.isarest.models.Description;
import com.isa.arnhem.isarest.models.Event;
import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserType;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.Jongo;

public class TestMongo {

    public static void main(String[] args) throws UnirestException {


        User ryan = new User("Ryiin", "rysana@live.com", "test1hfjhs", UserType.MEMBER);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);


        DB db = new MongoClient().getDB("isa-rest");
        //com.mongodb.MongoClient db1 = new MongoClientFactory(null,null).createMongoClient(null);

        EventDao eventDao = new EventDao(new Jongo(db));
        Event event = eventDao.findByEventId("81694530-2592-43bd-b7aa-2d1b9e93e44c");

        event.getAttendees().clear();
        eventDao.update(event);

        Event evt = new Event() {{
            setName("Random event");
            setDescription(new Description() {{
                setShortened("duhfi");
                setExtended("sgkjgsjgpijhgrwoluhtlwiueahpgiuehiduhwladiguhwaliuh");
            }});
            setMainImage("https://www.hollandevenementengroep.nl/foto/11902/858/500/!crop!/files/Accent%20Fotos/party2.jpg");
        }};
        UserDao ud = new UserDao(new Jongo(db));
        ud.deleteAll();
        eventDao.deleteAll();
        eventDao.create(evt);

    }
}
