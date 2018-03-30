package com.isa.arnhem.isarest.services;

import com.isa.arnhem.isarest.dto.*;
import com.isa.arnhem.isarest.models.event.Event;
import com.isa.arnhem.isarest.models.event.EventList;
import com.isa.arnhem.isarest.models.user.Attendee;
import com.isa.arnhem.isarest.models.user.User;
import com.isa.arnhem.isarest.models.user.UserReference;
import com.isa.arnhem.isarest.models.user.UserType;
import com.isa.arnhem.isarest.repository.DaoRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Service
public class UserService extends BaseService {


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(DaoRepository daoRepository, PasswordEncoder passwordEncoder) {
        super(daoRepository);
        this.passwordEncoder = passwordEncoder;
    }

    public Collection<User> getAll() {
        return userDao.getAll();
    }

    public Collection<BasicUserDTO> getAllUsers(String q, Integer l, String eventId, Optional<User> loggedInUser) {
        int limit = Optional.ofNullable(l).orElse(Integer.MAX_VALUE);


        User user = evaluateUserIsLoggedIn(loggedInUser);

        if (eventId != null) {
            Event event = evaluateEvent(eventId);
            int count = 0;
            final List<BasicUserDTO> result = new ArrayList<>();

            for (Attendee attendee : event.getAttendees()) {
                if (count > limit)
                    break;
                if (userMatch(q, attendee.getUser())) {
                    result.add(new BasicUserDTO(attendee.getUser()));
                    count++;
                }
            }
            return result;
        } else {
            return userDao.search(q, limit);
        }
    }

    private boolean userMatch(String q, UserReference user) {
        q = q.trim().toLowerCase();
        return user.getFullName().toLowerCase().contains(q) || user.getUsername().toLowerCase().contains(q) || user.getEmail().toLowerCase().contains(q);
    }

    public ResponseMessage addUser(User user) {
        final String evaluatedUsername = evaluateUsername(user.getUsername());

        if (evaluatedUsername != null) {
            return ResponseMessage.builder().message(evaluatedUsername).type(ResponseType.CLIENT_ERROR).build();
        }

        if (userDao.findByUsername(user.getUsername()).isPresent()) {
            return ResponseMessage.builder().message("This username is already taken!").type(ResponseType.CLIENT_ERROR).build();
        }
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            return ResponseMessage.builder().message("This is an invalid email!").type(ResponseType.CLIENT_ERROR).build();
        }
        if (userDao.findByEmail(user.getEmail()).isPresent()) {
            return ResponseMessage.builder().message("This e-mail address is already in use!").type(ResponseType.CLIENT_ERROR).build();
        }
        int passwordLength = user.getPassword().length();

        if (passwordLength < 5 || passwordLength > 50) {
            return ResponseMessage.builder().message("Password must be between 4 and 50 characters long").type(ResponseType.CLIENT_ERROR).build();
        }
        user.setType(UserType.STUDENT);
        user.setActivated(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.create(user);
        return ResponseMessage.builder().message(String.format("You have successfully registered at ISA, %s! Please login with your fresh account.", user.getUsername())).type(ResponseType.SUCCESSFUL).build();
    }


    public User getSelfUser(Optional<User> loggedInUser) {
        try {
            return evaluateUserIsLoggedIn(loggedInUser);
        } catch (ResponseException e) {
            throw ResponseException.builder().type(ResponseType.UNAUTHORIZED).message("Incorrect username/password.").build();
        }
    }

    public Iterable<UserType> getRanks(Optional<User> loggedInUser) {
        if (loggedInUser.isPresent() && loggedInUser.get().isAuthorative()) {
            return loggedInUser.get().getType().getRanksBelow();
        }
        return new ArrayList<>();
    }

    public ResponseMessage updateRank(String userId, String rank, Optional<User> loggedInUser) {
        final Optional<UserType> rankToUpdateTo = UserType.getRank(rank);
        final Optional<User> userToUpdate = userDao.findByString(userId);

        if (!userToUpdate.isPresent()) {
            return ResponseMessage.builder().type(ResponseType.CLIENT_ERROR).message("User doesn't exist!").build();
        }
        if (loggedInUser.isPresent() && loggedInUser.get().getType().hasEqualRightsTo(UserType.ISA_MEMBER)) {
            if (userToUpdate.get().equals(loggedInUser.get())) {
                return ResponseMessage.builder().type(ResponseType.UNAUTHORIZED).message("You can't update your own rank!").build();

            }
            if (userToUpdate.get().getType().hasMoreRightsThan(loggedInUser.get().getType())) {
                return ResponseMessage.builder().type(ResponseType.UNAUTHORIZED).message("The user you are trying to update has a similar or higher ranking than you!").build();
            }
            if (rankToUpdateTo.isPresent() && loggedInUser.get().getType().hasMoreRightsThan(rankToUpdateTo.get())) {
                if (userToUpdate.get().getType().equals(rankToUpdateTo.get())) {
                    return ResponseMessage.builder().type(ResponseType.CLIENT_ERROR).message("User already has this rank!").build();
                }
                userToUpdate.get().setType(rankToUpdateTo.get());
                userDao.update(userToUpdate.get());
                return ResponseMessage.builder().type(ResponseType.ACCEPTED).message("Succesfully updated users rank!").build();
            } else {
                List<UserType> ranksAllowed = loggedInUser.get().getType().getRanksBelow();

                StringBuilder stringBuilder = new StringBuilder();
                ranksAllowed.forEach((userType -> stringBuilder.append(userType.name()).append(", ")));
                String ranksAllowedString = stringBuilder.toString().trim();

                ranksAllowedString = ranksAllowedString.substring(0, ranksAllowedString.length() - 1);
                return ResponseMessage.builder().type(ResponseType.CLIENT_ERROR).message(String.format("Unable to update rank using using '%s' available ranks are[%s]", rank, ranksAllowedString)).build();
            }
        } else {
            return ResponseMessage.builder().type(ResponseType.UNAUTHORIZED).message("You must be logged in as an ISA_MEMBER member(atleast) to promote/demote users.").build();

        }
    }


    public Object getUser(String id) {
        return ReturnedObject.of(userDao.findByString(id));
    }

    public Map<String, Set<EventDTO>> getUserSignUps(String id) {

        TreeSet<EventDTO> events = new TreeSet<>();



        EventList.of(eventDao.find("{'attendees.user._id': #}", id)
                .projection("{_id: 1, price: 1, name: 1, main_image: 1, event_type: 1, date: 1, attendees: 1}")
                .as(Event.class)).filterOutPastEvents().forEach((event -> events.add(new AttendedEventDTO(event, id, false))));

        TreeSet<EventDTO> requestedEvents = new TreeSet<>();

        EventList.of(eventDao.find("{'requested_attendees.user._id': #}", id)
                .projection("{_id: 1, price: 1, name: 1, main_image: 1, event_type: 1, date: 1, requested_attendees: 1}")
                .as(Event.class)).filterOutPastEvents().forEach((event -> requestedEvents.add(new AttendedEventDTO(event, id, true))));

        Map<String, Set<EventDTO>> eventMap = new HashMap<>();
        eventMap.put("attending_events", events.descendingSet());
        eventMap.put("requested_events", requestedEvents.descendingSet());
        return eventMap;
    }

    public ResponseMessage activateUser(String id) {
        User user = evaluateUserIsLoggedIn(id);
        if (!user.isActivated()) {
            user.setActivated(true);
            userDao.update(user);
            return ResponseMessage.builder().type(ResponseType.SUCCESSFUL).message("Activated: " + user.getUsername()).build();
        } else {
            return ResponseMessage.builder().type(ResponseType.CLIENT_ERROR).message(user.getUsername() + " is already activated!").build();
        }
    }

    public void logOut(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    private String evaluateUsername(String username) {

        if (username == null || username.isEmpty()) {
            return "Username can't be empty!";
        }

        if (username.startsWith(".") || username.endsWith(".")) {
            return "Username can't begin or end with a dot!";
        }

        Pattern p = Pattern.compile("[^a-z0-9._ ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(username);

        if (m.find()) {
            return "Username can't have special characters!";
        }

        if (username.length() > 35) {
            return "Username must be less than 36 characters long";
        }
        return null;
    }
}
