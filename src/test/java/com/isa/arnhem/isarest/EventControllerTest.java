package com.isa.arnhem.isarest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isa.arnhem.isarest.controllers.EventController;
import com.isa.arnhem.isarest.models.*;
import com.isa.arnhem.isarest.models.Event;
import com.isa.arnhem.isarest.repository.EventDao;
import com.isa.arnhem.isarest.repository.NotificationDao;
import com.isa.arnhem.isarest.repository.UserDao;
import com.isa.arnhem.isarest.services.EventService;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EventControllerTest extends BaseIntegrationTest {

    EventService eventService;
    EventController eventController;
    Event testEvent;

    Event testEvent2;
    ControlledEvent testControlledEvent;

    User testSubject;
    private int initSize;

    @Before
    public void setUp() throws Exception {
        EventDao eventDao = new EventDao(getJongo());
        UserDao userDao = new UserDao(getJongo());
        NotificationDao notificationDao = new NotificationDao(getJongo());
        eventService = new EventService(eventDao, userDao, notificationDao);
        eventController = new EventController(eventService);

        testEvent = new NormalEvent();
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


        testEvent2 = new NormalEvent();
        testEvent2.setName("ISA Prague City Trip 2017 TEST EVENT: Another one");
        testEvent2.setDescription(new Description() {{
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
        testEvent2.setMainImage("https://scontent-ams3-1.xx.fbcdn.net/v/t31.0-8/22829559_1513570975426705_6457539393562076279_o.jpg?oh=9cc4dc4417edcdea3c02ab4b1ce7b0ef&oe=5AEEFB57");


        testControlledEvent = new ControlledEvent();
        testControlledEvent.setName("ISA Prague City Trip 2017 TEST EVENT: Another one");
        testControlledEvent.setDescription(new Description() {{
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
        testControlledEvent.setMainImage("https://scontent-ams3-1.xx.fbcdn.net/v/t31.0-8/22829559_1513570975426705_6457539393562076279_o.jpg?oh=9cc4dc4417edcdea3c02ab4b1ce7b0ef&oe=5AEEFB57");

        eventDao.create(testEvent);
        eventDao.create(testControlledEvent);
        initSize = 2;

        User ryan = new User("Ryan", "ryansusana@live.co", "testword", UserType.MEMBER);

        userDao.create(ryan);
        testSubject = userDao.findByUsername("Ryan");


        testEvent = eventDao.findByEventId(testEvent.getId());
    }

    @Test
    public void testListEvents() {
        List<Event> events = eventController.getAll();

        assertEquals(initSize, events.size());
    }

    @Test
    public void testAddEvent() {
        ResponseMessage responseMessage = eventController.addEvent(testEvent2).getBody();

        List<Event> events = eventController.getAll();
        assertEquals(ResponseMessageType.SUCCESSFUL, responseMessage.getMessageType());
        assertEquals(initSize + 1, events.size());
    }


    @Test
    public void testAddUserToEvent() {
        ResponseMessage message = eventController.addAttendee(testEvent.getId(), testSubject.getId()).getBody();

        Event updatedEvent = eventController.getEvent(testEvent.getId());

        assertEquals(1, updatedEvent.getAttendees().size());
    }

    private ControlledEvent getControlledEvent(){
        for (Event event : eventController.getAll()) {
            if (event instanceof ControlledEvent) {
                return (ControlledEvent) event;
            }
        }
        return null;
    }

    @Test
    public void testInitialDataContainsControlledEvent() throws JsonProcessingException {

        assertNotNull(getControlledEvent());
    }

    @Test
    public void testAddedToTheRequestedAttendeesListOnControlledEvent() {
        ResponseMessage message = eventController.addAttendee(testControlledEvent.getId(), testSubject.getId()).getBody();

        ControlledEvent updatedEvent =(ControlledEvent) eventController.getEvent(testControlledEvent.getId());

        assertEquals(0, updatedEvent.getAttendees().size());
        assertEquals(1, updatedEvent.getRequestedAttendees().size());
    }
}
