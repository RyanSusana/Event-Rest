package com.isa.arnhem.isarest;

import com.isa.arnhem.isarest.dto.*;
import com.isa.arnhem.isarest.models.Description;
import com.isa.arnhem.isarest.models.event.*;
import com.isa.arnhem.isarest.models.user.Attendee;
import com.isa.arnhem.isarest.models.user.User;
import com.isa.arnhem.isarest.models.user.UserType;
import com.isa.arnhem.isarest.repository.DaoRepository;
import com.isa.arnhem.isarest.repository.EventDao;
import com.isa.arnhem.isarest.repository.NotificationDao;
import com.isa.arnhem.isarest.repository.UserDao;
import com.isa.arnhem.isarest.services.EventService;
import nl.stil4m.mollie.Client;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    private EventService eventService;

    private NormalEvent normalEvent;
    private PayedEvent payedEvent;
    private ControlledEvent controlledEvent;

    private User ryan;
    private User admin;

    private int deletedEvents;

    @Before
    public void setUp() {
        deletedEvents = 0;
        Client mollieClient = mock(Client.class);
        DaoRepository daoRepository = mock(DaoRepository.class);
        EventDao eventDao = mock(EventDao.class);
        UserDao userDao = mock(UserDao.class);
        NotificationDao notificationDao = mock(NotificationDao.class);
        ryan = new User();
        ryan.setId("ryan");
        ryan.setUsername("ryan");
        ryan.setType(UserType.STUDENT);

        admin = new User();
        admin.setId("admin");
        admin.setUsername("admin");
        admin.setType(UserType.ISA_ADMIN);


        normalEvent = new NormalEvent();
        normalEvent.setId("normal");
        payedEvent = new PayedEvent();
        payedEvent.setId("payed");
        payedEvent.setPrice(BigDecimal.valueOf(15.00));

        controlledEvent = new ControlledEvent();
        controlledEvent.setId("controlled");
        EventList eventList = new EventList() {{
            add(normalEvent);
            add(payedEvent);
            add(controlledEvent);
        }};

        setupEvents(normalEvent, payedEvent, controlledEvent);
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        controlledEvent.setDate(cal.getTime());
        when(daoRepository.getEventDao()).thenReturn(eventDao);
        when(daoRepository.getUserDao()).thenReturn(userDao);
        when(daoRepository.getNotificationDao()).thenReturn(notificationDao);

        doNothing().when(notificationDao).notifyUsers(any(), any(), any());
        when(eventDao.getAll()).thenReturn(eventList);
        doAnswer((invocation) -> {
            deletedEvents++;
            return null;
        }).when(eventDao).delete(any());
        when(eventDao.findByString("normal")).thenReturn(Optional.of(normalEvent));
        when(eventDao.findById("normal")).thenReturn(Optional.of(normalEvent));

        when(eventDao.findByString("payed")).thenReturn(Optional.of(payedEvent));
        when(eventDao.findById("payed")).thenReturn(Optional.of(payedEvent));

        when(eventDao.findByString("controlled")).thenReturn(Optional.of(controlledEvent));
        when(eventDao.findById("controlled")).thenReturn(Optional.of(controlledEvent));


        when(eventDao.findByString("none")).thenReturn(Optional.empty());

        when(userDao.findByString("ryan")).thenReturn(Optional.of(ryan));
        when(userDao.findByString("none")).thenReturn(Optional.empty());
        eventService = new EventService(daoRepository, mollieClient);

    }

    private void setupEvents(Event... events) {
        for (Event event : events) {
            event.setName(event.getId());
            event.setDate(Calendar.getInstance().getTime());
        }
    }

    @Test
    public void testGetAllEvents() {
        List<EventDTO> eventList = eventService.getAll(true, Optional.of(ryan));
        assertEquals(3, eventList.size());
    }

    @Test
    public void testGetAllEventsNotInThePast() {

        List<EventDTO> eventList = eventService.getAll(false, Optional.of(admin));
        assertEquals(2, eventList.size());
        assertTrue(eventList.get(0) instanceof CalculatedEventDTO);
    }

    @Test
    public void testDeleteEvent() {
        ResponseMessage response = eventService.deleteEvent("payed", Optional.of(admin));

        assertEquals(1, deletedEvents);
        assertEquals(ResponseType.SUCCESSFUL, response.getType());
    }

    @Test
    public void testGetEvent() {
        Optional<EventDTO> event = eventService.getEvent("payed", Optional.of(admin));

        assertTrue(event.isPresent());
        assertTrue(event.get() instanceof CompleteEventDTO);

        event = eventService.getEvent("payed", Optional.of(ryan));

        assertFalse(event.get() instanceof CompleteEventDTO);

    }

    @Test
    public void testCreateEvent() {
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

        Response response = eventService.createEvent(testEvent, Optional.of(admin));

        assertEquals(ResponseType.SUCCESSFUL, response.getType());
    }

    @Test
    public void testAddAndRemoveUser() {

        Response response = eventService.addUserToEvent("payed", "ryan");
        assertTrue(response.isSuccessful());

        response = eventService.addUserToEvent("payed", "ryan");
        assertFalse(response.isSuccessful());
        assertTrue(payedEvent.getRequestedAttendees().containsUser(ryan));
        assertFalse(payedEvent.getAttendees().containsUser(ryan));

        response = eventService.addUserToEvent("normal", "ryan");
        assertTrue(response.isSuccessful());
        assertTrue(normalEvent.getAttendees().containsUser(ryan));

        controlledEvent.getAttendees().add(Attendee.of(ryan.getId(), Calendar.getInstance().getTime()));

        response = eventService.addUserToEvent("controlled", "ryan");
        assertFalse(response.isSuccessful());
        assertTrue(eventService.getAttendees("controlled", Optional.of(admin)).containsUser(ryan));

        response = eventService.removeUserFromEvent("payed", "ryan");
        assertFalse(response.isSuccessful());

        payedEvent.getAttendees().add(Attendee.of("ryan", Calendar.getInstance().getTime()));
        response = eventService.removeUserFromEvent("payed", "ryan");
        assertFalse(response.isSuccessful());

        response = eventService.removeUserFromEvent("normal", "ryan");
        assertTrue(response.isSuccessful());


    }

    @Test
    public void testUpdateEvent() {

        PayedEvent testEvent = new PayedEvent();
        testEvent.setControlledBy(UserType.ISA_ADMIN);
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

        Response response = eventService.updateEvent(payedEvent.getId(), new EventDTO(testEvent), Optional.of(admin));
        assertTrue(response.isSuccessful());
        assertEquals("payed", payedEvent.getName());

        testEvent.setName("newName");

        response = eventService.updateEvent(payedEvent.getId(), new EventDTO(testEvent), Optional.of(admin));
        assertTrue(response.isSuccessful());
        assertEquals("newName", payedEvent.getName());

    }

    @Test
    public void testAddUserToControlledEvent() {
        Response response = eventService.addUserToControlledEvent("payed", "none", Optional.of(admin), -1);
        assertFalse(response.isSuccessful());

        response = eventService.addUserToControlledEvent("payed", "ryan", Optional.of(admin), 10);
        assertTrue(response.isSuccessful());
        assertTrue(payedEvent.getAttendees().containsUser(ryan));
        assertEquals(10, payedEvent.getAttendees().getAttendee(ryan).get().getPlus());

        response = eventService.addUserToControlledEvent("payed", "ryan", Optional.of(admin), 10);
        assertFalse(response.isSuccessful());

        response = eventService.addUserToControlledEvent("normal", "ryan", Optional.of(admin), 10);
        assertTrue(response.isSuccessful());

        controlledEvent.getRequestedAttendees().add(Attendee.of(ryan.getId(), Calendar.getInstance().getTime()));
        response = eventService.addUserToControlledEvent("controlled", "ryan", Optional.of(admin), 10);
        assertTrue(response.isSuccessful());
        assertTrue(controlledEvent.getAttendees().containsUser(ryan));
        assertEquals(10, controlledEvent.getAttendees().getAttendee(ryan).get().getPlus());
    }
}
