package com.isa.arnhem.isarest;

import com.isa.arnhem.isarest.dto.Response;
import com.isa.arnhem.isarest.models.event.ControlledEvent;
import com.isa.arnhem.isarest.models.event.NormalEvent;
import com.isa.arnhem.isarest.models.event.PayedEvent;
import com.isa.arnhem.isarest.models.user.User;
import com.isa.arnhem.isarest.models.user.UserType;
import com.isa.arnhem.isarest.repository.DaoRepository;
import com.isa.arnhem.isarest.repository.EventDao;
import com.isa.arnhem.isarest.repository.NotificationDao;
import com.isa.arnhem.isarest.repository.UserDao;
import com.isa.arnhem.isarest.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService;

    private NormalEvent normalEvent;
    private PayedEvent payedEvent;
    private ControlledEvent controlledEvent;

    private User ryan;

    private User admin;

    @Before
    public void setUp() throws Exception {

        resetRyan();
        admin = new User() {{
            this.setUsername("admin");
            this.setType(UserType.SUPER_ADMIN);
            this.setId("admin");
            this.setFullName("Ryan Susana");
            this.setActivated(true);
            this.setEmail("ryansusana@live.com");

            this.setPassword("WordPass");
        }};
        DaoRepository daoRepository = mock(DaoRepository.class);
        EventDao eventDao = mock(EventDao.class);
        UserDao userDao = mock(UserDao.class);
        NotificationDao notificationDao = mock(NotificationDao.class);

        when(daoRepository.getEventDao()).thenReturn(eventDao);
        when(daoRepository.getUserDao()).thenReturn(userDao);
        when(daoRepository.getNotificationDao()).thenReturn(notificationDao);



        when(userDao.findByEmail(any())).thenReturn(Optional.empty());
        when(userDao.findByUsername(any())).thenReturn(Optional.empty());
        when(userDao.findById(any())).thenReturn(Optional.empty());

        when(userDao.findByEmail("ryansusana2@live.com")).thenReturn(Optional.of(admin));
        when(userDao.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(userDao.findById("admin")).thenReturn(Optional.of(admin));

        doNothing().when(notificationDao).notifyUsers(any(), any(), any());
        doNothing().when(userDao).create((User) any());
        doNothing().when(userDao).create(anyCollection());


        userService = new UserService(daoRepository, new BCryptPasswordEncoder());
    }
    private void resetRyan(){
        ryan = new User() {{
            this.setUsername("Ryan");
            this.setType(UserType.ISA_MEMBER);
            this.setId("Ryan");
            this.setFullName("Ryan Susana");
            this.setActivated(true);
            this.setEmail("ryansusana@live.com");
            this.setPassword("WordPass");
        }};
    }

    @Test
    public void testAddUser() {

        Response responseSuccessful = userService.addUser(ryan);

        ryan.setPassword("r");
        Response responsePasswordTooShort = userService.addUser(ryan);
        resetRyan();

        ryan.setUsername("admin");
        Response responseUsernameExists = userService.addUser(ryan);
        resetRyan();

        ryan.setEmail("ryansusana2@live.com");
        Response responseEmailTaken = userService.addUser(ryan);
        resetRyan();

        ryan.setUsername("_RyAn@#");
        Response responseInvalidCharacters= userService.addUser(ryan);
        resetRyan();

        assertTrue(responseSuccessful.isSuccessful());
        assertFalse(responseEmailTaken.isSuccessful());
        assertFalse(responsePasswordTooShort.isSuccessful());
        assertFalse(responseUsernameExists.isSuccessful());
        assertFalse(responseInvalidCharacters.isSuccessful());
    }


}

