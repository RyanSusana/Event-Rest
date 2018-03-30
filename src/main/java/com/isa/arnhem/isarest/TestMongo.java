package com.isa.arnhem.isarest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isa.arnhem.isarest.models.Description;
import com.isa.arnhem.isarest.models.event.PayedEvent;
import com.isa.arnhem.isarest.models.user.UserType;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TestMongo {

    public static void main(String[] args) throws JsonProcessingException, UnirestException {
        PayedEvent testEvent = new PayedEvent();

        testEvent.setControlledBy(UserType.ISA_ADMIN);
        testEvent.setName("Ryan's Graduation");
        testEvent.setDate(Date.from(LocalDateTime.of(2020, 6, 20, 20, 0).atZone(ZoneId.systemDefault()).toInstant()));
        testEvent.setPrice(BigDecimal.valueOf(15.00));
        testEvent.setDescription(new Description() {{
            setShortened("Hey guys this is a test 'free/pay at the door' event! \n" +
                    "It's more of an informative event page, but you can attend it as well with no hassle. Enjoy my cringey graduation picture(that dude is not my dad, he's my english teacher). And this is just really random text I had to type out to fill out this box because I'm really good at making up long strings of text.");
            setExtended("Hello beautiful people! ♡\n" +
                    "\n"
            );
        }});
        testEvent.setMainImage("https://scontent-ams3-1.xx.fbcdn.net/v/t31.0-8/13908825_1388571371171873_8804349362418421261_o.jpg?oh=118248c78cf5bbd1c163152843b52287&oe=5B07A714");

        System.out.println(Unirest.post("http://localhost:8086/api/events").basicAuth("admin","Susana").header("Content-Type", "application/json").body(new ObjectMapper().writeValueAsString(testEvent)).asString().getBody());
    }
}
