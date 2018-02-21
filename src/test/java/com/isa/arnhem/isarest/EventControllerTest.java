package com.isa.arnhem.isarest;

public class EventControllerTest extends BaseIntegrationTest {
/*
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
        testEvent.setName("ISA_MEMBER Prague City Trip 2017 TEST EVENT: Another one");
        testEvent.setDescription(new Description() {{
            setShortened("Roadtrips, sightseeing, meeting and bonding with new people, drinks, dancing, laughter and lot's of fun guaranteed... It's definitely time for a trip, and this time, ISA_MEMBER wants to go with you to the gorgeous city of Prague! ");
            setExtended("Who's ready for an adventure? Been thinking of visiting a beautiful city in Europe?\n" +
                    "\n" +
                    "Roadtrips, sightseeing, meeting and bonding with new people, drinks, dancing, laughter and lot's of fun guaranteed... It's definitely time for a trip, and this time, ISA_MEMBER wants to go with you to the gorgeous city of Prague! \n" +
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
        testEvent2.setName("ISA_MEMBER Prague City Trip 2017 TEST EVENT: Another one");
        testEvent2.setDescription(new Description() {{
            setShortened("Roadtrips, sightseeing, meeting and bonding with new people, drinks, dancing, laughter and lot's of fun guaranteed... It's definitely time for a trip, and this time, ISA_MEMBER wants to go with you to the gorgeous city of Prague! ");
            setExtended("Who's ready for an adventure? Been thinking of visiting a beautiful city in Europe?\n" +
                    "\n" +
                    "Roadtrips, sightseeing, meeting and bonding with new people, drinks, dancing, laughter and lot's of fun guaranteed... It's definitely time for a trip, and this time, ISA_MEMBER wants to go with you to the gorgeous city of Prague! \n" +
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
        testControlledEvent.setName("ISA_MEMBER Prague City Trip 2017 TEST EVENT: Another one");
        testControlledEvent.setDescription(new Description() {{
            setShortened("Roadtrips, sightseeing, meeting and bonding with new people, drinks, dancing, laughter and lot's of fun guaranteed... It's definitely time for a trip, and this time, ISA_MEMBER wants to go with you to the gorgeous city of Prague! ");
            setExtended("Who's ready for an adventure? Been thinking of visiting a beautiful city in Europe?\n" +
                    "\n" +
                    "Roadtrips, sightseeing, meeting and bonding with new people, drinks, dancing, laughter and lot's of fun guaranteed... It's definitely time for a trip, and this time, ISA_MEMBER wants to go with you to the gorgeous city of Prague! \n" +
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

        User ryan = new User("Ryan", "ryansusana@live.co", "testword", UserType.STUDENT);

        userDao.create(ryan);
        testSubject = userDao.findByUsername("Ryan").get();


        testEvent = eventDao.findById(testEvent.getId()).get();
    }

    @Test
    public void testListEvents() {
        List<EventDTO> events = eventController.getAll();

        assertEquals(initSize, events.size());
    }

    @Test
    public void testAddEvent() {
        ResponseMessage responseMessage = eventController.addEvent(testEvent2).getBody();

        List<EventDTO> events = eventController.getAll();
        assertEquals(ResponseMessageType.SUCCESSFUL, responseMessage.getMessageType());
        assertEquals(initSize + 1, events.size());
    }


    @Test
    public void testAddUserToEvent() {
        ResponseMessage message = eventController.addAttendee(testEvent.getId(), testSubject.getId(),false).getBody();

        Event updatedEvent = eventController.getEvent(testEvent.getId());

        assertEquals(1, updatedEvent.getAttendees().size());
    }





    @Test
    public void testAddedToTheRequestedAttendeesListOnControlledEvent() {
        ResponseMessage message = eventController.addAttendee(testControlledEvent.getId(), testSubject.getId(),false ).getBody();

        ControlledEvent updatedEvent =(ControlledEvent) eventController.getEvent(testControlledEvent.getId());

        assertEquals(0, updatedEvent.getAttendees().size());
        assertEquals(1, updatedEvent.getRequestedAttendees().size());
    }
    @Test
    public void testNotAddedToTheAttendeesListOnControlledEventWithLowRankingUser() {
        ResponseMessage message = eventController.addUserToControlledEvent(testControlledEvent.getId(),testSubject.getId(), Optional.of(testSubject)).getBody();

        ControlledEvent updatedEvent =(ControlledEvent) eventController.getEvent(testControlledEvent.getId());

        assertEquals(0, updatedEvent.getAttendees().size());
        assertEquals(ResponseMessageType.UNAUTHORIZED, message.getMessageType());
    }
    @Test
    public void testAddedToTheAttendeesListOnControlledEventWithAppropriateRankingUser() {
        testSubject.setType(UserType.ISA_ADMIN);
        ResponseMessage message = eventController.addUserToControlledEvent(testControlledEvent.getId(),testSubject.getId(), Optional.of(testSubject)).getBody();

        ControlledEvent updatedEvent =(ControlledEvent) eventController.getEvent(testControlledEvent.getId());

        assertEquals(1, updatedEvent.getAttendees().size());
        assertEquals(0, updatedEvent.getRequestedAttendees().size());
        assertEquals(ResponseMessageType.SUCCESSFUL, message.getMessageType());
    }
    */
}
