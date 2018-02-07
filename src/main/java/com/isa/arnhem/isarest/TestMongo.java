package com.isa.arnhem.isarest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isa.arnhem.isarest.models.ControlledEvent;
import com.isa.arnhem.isarest.models.Description;
import com.isa.arnhem.isarest.models.NormalEvent;
import com.isa.arnhem.isarest.models.UserType;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TestMongo {

    public static void main(String[] args) throws UnirestException, JsonProcessingException {


//        User user = new User("Ryan","ryansusana@live.com","Susana",UserType.MEMBER);
//        user.setPassword(new IsaPasswordEncoder().encode(user.getPassword()));
//        new UserDao(new Jongo(new MongoClient().getDB("isa-rest"), new JacksonMapper.Builder().withObjectIdUpdater(new CustomObjectIdUpdater(Mapping.defaultMapping().getObjectMapper())).build()))
//                .create(user);
        NormalEvent testEvent = new NormalEvent();
        //testEvent.setControlledBy(UserType.ISA_ADMIN);
        testEvent.setName("Valentine's Party 2020");
        testEvent.setDate(Date.from(LocalDateTime.of(2020, 2, 8, 23, 0).atZone(ZoneId.systemDefault()).toInstant()));

        testEvent.setDescription(new Description() {{
            setShortened("Open yourself up to the new adventures of love! \n" +
                    "If you still don’t feel the atmosphere of upcoming Valentine’s Day, this is your turn. Make this day special joining us on Valentine’s Party. Don’t forget to finally show your feelings and invite your lovely one.");
            setExtended("Hello beautiful people! ♡\n" +
                    "\n" +
                    "Open yourself up to the new adventures of love! \n" +
                    "If you still don’t feel the atmosphere of upcoming Valentine’s Day, this is your turn. Make this day special joining us on Valentine’s Party. Don’t forget to finally show your feelings and invite your lovely one. \n" +
                    "\n" +
                    "Place – Loft (Korenmarkt Arnhem)\n" +
                    "Time – 23:00-4:00\n" +
                    "2nd floor opens at 00:00\n" +
                    "Price – 3€\n" +
                    "Dj Da Phonk\n" +
                    "\n" +
                    "*Specials*\n" +
                    "Tequila and Sambuca shots - 1,50€ \n" +
                    "Free Loft shots every hour \uD83D\uDE0F\n" +
                    "DRUNK MAILBOX!!! -> leave your love declarations in the party mailbox! :) \n" +
                    "\n" +
                    "It's a party for sharing your love and making new connections! See you there!\n" +
                    "\n" +
                    "Your ISA_MEMBER \n" +
                    "Xoxo ♡");
        }});

        testEvent.setMainImage("http://www.jimmyvalentineslhc.com/wp-content/uploads/2015/03/IMG_9229-1000x600.jpg");
//new EventDao(new Jongo(new MongoClient().getDB("isa-rest"), new JacksonMapper.Builder().withObjectIdUpdater(new CustomObjectIdUpdater(Mapping.defaultMapping().getObjectMapper())).build())).create(testEvent);

        System.out.println(Unirest.post("http://localhost:8086/api/events/").header("Content-Type", "application/json").basicAuth("admin","Susana").body(new ObjectMapper().writeValueAsString(testEvent)).asString().getBody());


//        UserDao ud = new UserDao(new Jongo(new MongoClient().getDB("isa-rest"), new JacksonMapper.Builder().withObjectIdUpdater(new CustomObjectIdUpdater(Mapping.defaultMapping().getObjectMapper())).build()));
//        User ryan = ud.findByUsername("Ryan").get();
//
//        ryan.setType(UserType.ISA_ADMIN);
//
//        ud.update(ryan);
    }
}
