package com.isa.arnhem.isarest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fakemongo.Fongo;
import com.isa.arnhem.isarest.controllers.UserController;
import com.isa.arnhem.isarest.dao.UserDao;
import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserType;
import com.mashape.unirest.http.Unirest;
import org.jongo.Jongo;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserControllerTest {
    UserController controller;
    UserDao userDao;
    Fongo fongo;

    @Before
    public void setUp() throws Exception {
        fongo = new Fongo("Fongo Test Server");
        userDao = new UserDao(new Jongo(fongo.getDB("isa-test")));
        controller = new UserController(userDao);

        User ryan = new User("Ryiin", "ryansusana@live.com", "test", UserType.MEMBER);
        userDao.create(ryan);
    }

    @Test
    public void testAddUser() throws JsonProcessingException {
        User john = new User("John", "", "test", UserType.MEMBER);
        controller.addUser(john);
        assertNotNull(userDao.findByUsername(john.getUsername()));
    }
}
