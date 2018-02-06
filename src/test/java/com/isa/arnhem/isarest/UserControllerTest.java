package com.isa.arnhem.isarest;

import com.isa.arnhem.isarest.controllers.UserController;
import com.isa.arnhem.isarest.models.ResponseMessage;
import com.isa.arnhem.isarest.models.ResponseMessageType;
import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserType;
import com.isa.arnhem.isarest.repository.EventDao;
import com.isa.arnhem.isarest.repository.NotificationDao;
import com.isa.arnhem.isarest.repository.UserDao;
import com.isa.arnhem.isarest.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;

public class UserControllerTest extends BaseIntegrationTest {

    UserController userController;
    UserDao userDao;
    User testSubject;

    @Before
    public void setUp() throws Exception {
        EventDao eventDao = new EventDao(getJongo());
        userDao = new UserDao(getJongo());
        NotificationDao notificationDao = new NotificationDao(getJongo());
        UserService userService = new UserService(userDao, eventDao);
        userController = new UserController(userService, new BCryptPasswordEncoder());
        User ryan = new User("Ryan", "ryansusana@live.co", "testword", UserType.ISA_MEMBER);
        userDao.create(ryan);
        testSubject = userDao.findByUsername("Ryan").get();
    }

    @Test
    public void testAddUser() {

        User ryan = new User(testSubject.getUsername() + "o", testSubject.getEmail() + "m", "testword", UserType.ISA_MEMBER);

        ResponseMessage message = userController.addUser(ryan).getBody();

        assertEquals(200, message.getMessageType().getStatus());

    }

    @Test
    public void testAddUserForPasswordTooShort() {

        User ryan = new User(testSubject.getUsername() + "o", testSubject.getEmail() + "m", "te", UserType.ISA_MEMBER);

        ResponseMessage message = userController.addUser(ryan).getBody();

        assertEquals(ResponseMessageType.CLIENT_ERROR, message.getMessageType());

    }

    @Test
    public void testAddUserForInvalidUsername() {

        User ryan = new User(testSubject.getUsername() + "!", testSubject.getEmail() + "m", "testword", UserType.ISA_MEMBER);

        ResponseMessage message = userController.addUser(ryan).getBody();

        assertEquals(ResponseMessageType.CLIENT_ERROR, message.getMessageType());

    }

    @Test
    public void testAddUserForUsernameAlreadyTaken() {

        User ryan = new User(testSubject.getUsername(), testSubject.getEmail() + "m", "testword", UserType.ISA_MEMBER);

        ResponseMessage message = userController.addUser(ryan).getBody();

        assertEquals(ResponseMessageType.CLIENT_ERROR, message.getMessageType());

    }

    @Test
    public void testAddUserForEmailAlreadyTaken() {

        User ryan = new User(testSubject.getUsername() + "o", testSubject.getEmail(), "testword", UserType.ISA_MEMBER);

        ResponseMessage message = userController.addUser(ryan).getBody();

        assertEquals(ResponseMessageType.CLIENT_ERROR, message.getMessageType());

    }

    @Test
    public void testAddUserForInvalidEmail() {

        User ryan = new User(testSubject.getUsername() + "o", "rysanalive.com", "testword", UserType.ISA_MEMBER);

        ResponseMessage message = userController.addUser(ryan).getBody();

        assertEquals(ResponseMessageType.CLIENT_ERROR, message.getMessageType());

    }

}
