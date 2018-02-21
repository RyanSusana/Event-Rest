package com.isa.arnhem.isarest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isa.arnhem.isarest.models.Description;
import com.isa.arnhem.isarest.models.NormalEvent;
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
        testEvent.setName("Ryan's Graduation");
        testEvent.setDate(Date.from(LocalDateTime.of(2020, 6, 20, 20, 0).atZone(ZoneId.systemDefault()).toInstant()));
        //testEvent.setPrice(BigDecimal.valueOf(15.00));
        testEvent.setDescription(new Description() {{
            setShortened("Hey guys this is a test 'free/pay at the door' event! \n" +
                    "It's more of an informative event page, but you can attend it as well with no hassle. Enjoy my cringey graduation picture(that dude is not my dad, he's my english teacher). And this is just really random text I had to type out to fill out this box because I'm really good at making up long strings of text.");
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

        testEvent.setMainImage("https://scontent-ams3-1.xx.fbcdn.net/v/t31.0-8/13908825_1388571371171873_8804349362418421261_o.jpg?oh=118248c78cf5bbd1c163152843b52287&oe=5B07A714");
//new EventDao(new Jongo(new MongoClient().getDB("isa-rest"), new JacksonMapper.Builder().withObjectIdUpdater(new CustomObjectIdUpdater(Mapping.defaultMapping().getObjectMapper())).build())).create(testEvent);

        System.out.println(Unirest.post("https://isa.ryansusana.com:8086/api/events/").header("Content-Type", "application/json").basicAuth("admin", "Susana").body(new ObjectMapper().writeValueAsString(testEvent)).asString().getBody());

//        UserDao ud = new UserDao(new Jongo(new MongoClient().getDB("isa-rest"), new JacksonMapper.Builder().withObjectIdUpdater(new CustomObjectIdUpdater(Mapping.defaultMapping().getObjectMapper())).build()));
//        User ryan = ud.findByUsername("Ryan").get();
//
//        ryan.setType(UserType.ISA_ADMIN);
//
//        ud.update(ryan);
    }
}
