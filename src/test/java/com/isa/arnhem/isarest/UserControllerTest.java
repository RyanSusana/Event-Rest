package com.isa.arnhem.isarest;

import com.github.fakemongo.Fongo;
import com.isa.arnhem.isarest.controllers.UserController;
import com.isa.arnhem.isarest.repository.EventDao;
import com.isa.arnhem.isarest.repository.UserDao;
import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserType;
import org.jongo.Jongo;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class UserControllerTest {
    UserController controller;
    UserDao userDao;

    //Mocked Mongo database
    Fongo fongo;

    @Before
    public void setUp() {
        fongo = new Fongo("Fongo Test Server");
        userDao = new UserDao(new Jongo(fongo.getDB("isa-test")));
        EventDao eventDao = new EventDao(new Jongo(fongo.getDB("isa-test")));
        controller = null; //new UserController(userDao, eventDao);

        User ryan = new User("Ryiin", "ryansusana@live.com", "test", UserType.MEMBER);
        userDao.create(ryan);
    }

    @Test
    public void testAddUser() {
        User john = new User("Joe", "ryauana@live.com", "test5", UserType.MEMBER);
        controller.addUser(john);
        assertNotNull(userDao.findByUsername(john.getUsername()));
    }
}
