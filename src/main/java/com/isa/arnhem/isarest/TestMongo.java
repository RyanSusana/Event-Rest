package com.isa.arnhem.isarest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.isa.arnhem.isarest.models.*;
import com.isa.arnhem.isarest.repository.EventDao;
import com.isa.arnhem.isarest.repository.UserDao;
import com.isa.arnhem.isarest.repository.serialization.CustomObjectIdUpdater;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.jongo.marshall.jackson.JacksonMapper;
import org.jongo.marshall.jackson.configuration.Mapping;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

import java.util.UUID;

import static javax.mail.Message.RecipientType.BCC;

public class TestMongo {

    public static void main(String[] args) throws UnirestException, JsonProcessingException {



//        User user = new User("Ryan","ryansusana@live.com","Susana",UserType.MEMBER);
//        user.setPassword(new IsaPasswordEncoder().encode(user.getPassword()));
//        new UserDao(new Jongo(new MongoClient().getDB("isa-rest"), new JacksonMapper.Builder().withObjectIdUpdater(new CustomObjectIdUpdater(Mapping.defaultMapping().getObjectMapper())).build()))
//                .create(user);
        NormalEvent testEvent = new NormalEvent();
        testEvent.setName("ISA Prague City Trip 2017 TEST EVENT: Another one");
        testEvent.setDescription(new Description() {{
            setShortened("Roadtrips, sightseeing, meeting and bonding with new people, drinks, dancing, laughter and lot's of fun guaranteed... It's definitely time for a trip, and this time, ISA wants to go with you to the gorgeous city of Prague! ");
            setExtended("Who's ready for an adventure? Been thinking of visiting a beautiful city in Europe?\n" +
                    "\n" +
                    "Roadtrips, sightseeing, meeting and bonding with new people, drinks, dancing, laughter and lot's of fun guaranteed... It's definitely time for a trip, and this time, ISA wants to go with you to the gorgeous city of Prague! \n" +
                    "\n" +
                    "Interested? Keep reading!\n" +
                    "\n" +
                    "Date: From 28th September to 1st October 2017\n" +
                    "\n" +
                    "What's covered in the Price:\n" +
                    "-Transportation via Bus to go and return\n" +
                    "-Hotel accomodation\n" +
                    "-Prague Castle\n" +
                    "-Tons of fun and an unforgettable experience \n" +
                    "\n" +
                    "Tickets: -Early Birds 110 EUR\n" +
                    "-Regular 125 EUR\n" +
                    "\n" +
                    "NOTE: Passport or valid Travel ID required!!! The bus will leave Arnhem on Thursday evening, and will return from Praque on Sunday evening. Bus ride is appoximately 8 hrs duration.");
        }});

        testEvent.setMainImage("https://scontent-ams3-1.xx.fbcdn.net/v/t31.0-8/22829559_1513570975426705_6457539393562076279_o.jpg?oh=9cc4dc4417edcdea3c02ab4b1ce7b0ef&oe=5AEEFB57");
new EventDao(new Jongo(new MongoClient().getDB("isa-rest"), new JacksonMapper.Builder().withObjectIdUpdater(new CustomObjectIdUpdater(Mapping.defaultMapping().getObjectMapper())).build())).create(testEvent);

    }
}
