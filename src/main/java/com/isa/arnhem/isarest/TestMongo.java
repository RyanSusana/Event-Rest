package com.isa.arnhem.isarest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserType;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TestMongo {

    public static void main(String[] args) throws UnirestException, JsonProcessingException {


        User ryan = new User("Ryiin", "ryansusana@live.com", "test", UserType.MEMBER);
        System.out.println(ryan.getId());
        String res = Unirest.post("http://localhost:8086/api/users").header("accept", "application/json")
                .header("Content-Type", "application/json").body(new ObjectMapper().writeValueAsString(ryan)).asString().getBody();
        String res1 = Unirest.get("http://localhost:8086/api/users").asString().getBody();
        System.out.println(res1);
    }
}
