package com.isa.arnhem.isarest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserType;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TestMongo {

    public static void main(String[] args) throws UnirestException, JsonProcessingException {


        User ryan = new User("Ryiin", "ryansusana@live.com", "test", UserType.MEMBER);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String res = Unirest.post("http://localhost:8086/api/users").header("accept", "application/json")
                .header("Content-Type", "application/json").body(mapper.writeValueAsString(ryan)).asString().getBody();
        String res1 = Unirest.get("http://localhost:8086/api/users").asString().getBody();
        System.out.println(mapper.writeValueAsString(ryan));
        System.out.println(res);
    }
}
