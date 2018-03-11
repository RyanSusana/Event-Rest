package com.isa.arnhem.isarest;

import com.isa.arnhem.isarest.controllers.UserController;
import com.isa.arnhem.isarest.dto.Response;
import com.isa.arnhem.isarest.dto.ResponseType;
import com.isa.arnhem.isarest.models.user.User;
import com.isa.arnhem.isarest.models.user.UserType;
import com.isa.arnhem.isarest.repository.EventDao;
import com.isa.arnhem.isarest.repository.NotificationDao;
import com.isa.arnhem.isarest.repository.UserDao;
import com.isa.arnhem.isarest.services.UserService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class UserControllerTest extends BaseIntegrationTest {

    private UserController userController;
    private UserDao userDao;
    private User testSubject;

    @Before
    public void setUp() {
        EventDao eventDao = new EventDao(getJongo());
        userDao = new UserDao(getJongo());
        NotificationDao notificationDao = new NotificationDao(getJongo());
        UserService userService = new UserService(null, null);
        userController = new UserController(userService);
        User ryan = new User("Ryan", "ryansusana@live.co", "testword", UserType.ISA_MEMBER);
        userDao.create(ryan);
        testSubject = userDao.findByUsername("Ryan").get();
    }

    @Test
    @Ignore
    public void testAddUser() {

        User ryan = new User(testSubject.getUsername() + "o", testSubject.getEmail() + "m", "testword", UserType.ISA_MEMBER);

        Response message = userController.addUser(ryan).getBody();

        assertEquals(200, message.getType().getStatus());

    }
    @Ignore
    @Test
    public void testAddUserForPasswordTooShort() {

        User ryan = new User(testSubject.getUsername() + "o", testSubject.getEmail() + "m", "te", UserType.ISA_MEMBER);

        Response message = userController.addUser(ryan).getBody();

        assertEquals(ResponseType.CLIENT_ERROR, message.getType());

    }
    @Ignore
    @Test
    public void testAddUserForInvalidUsername() {

        User ryan = new User(testSubject.getUsername() + "!", testSubject.getEmail() + "m", "testword", UserType.ISA_MEMBER);

        Response message = userController.addUser(ryan).getBody();

        assertEquals(ResponseType.CLIENT_ERROR, message.getType());

    }
    @Ignore
    @Test
    public void testAddUserForUsernameAlreadyTaken() {

        User ryan = new User(testSubject.getUsername(), testSubject.getEmail() + "m", "testword", UserType.ISA_MEMBER);

        Response message = userController.addUser(ryan).getBody();

        assertEquals(ResponseType.CLIENT_ERROR, message.getType());

    }
    @Ignore
    @Test
    public void testAddUserForEmailAlreadyTaken() {

        User ryan = new User(testSubject.getUsername() + "o", testSubject.getEmail(), "testword", UserType.ISA_MEMBER);

        Response message = userController.addUser(ryan).getBody();

        assertEquals(ResponseType.CLIENT_ERROR, message.getType());

    }
    @Ignore
    @Test
    public void testAddUserForInvalidEmail() {

        User ryan = new User(testSubject.getUsername() + "o", "rysanalive.com", "testword", UserType.ISA_MEMBER);

        Response message = userController.addUser(ryan).getBody();

        assertEquals(ResponseType.CLIENT_ERROR, message.getType());

    }

}
