package com.isa.arnhem.isarest.services;

import com.isa.arnhem.isarest.dto.ResponseException;
import com.isa.arnhem.isarest.dto.ResponseType;
import com.isa.arnhem.isarest.models.event.Event;
import com.isa.arnhem.isarest.models.user.User;
import com.isa.arnhem.isarest.models.user.UserType;
import com.isa.arnhem.isarest.repository.DaoRepository;
import com.isa.arnhem.isarest.repository.EventDao;
import com.isa.arnhem.isarest.repository.NotificationDao;
import com.isa.arnhem.isarest.repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseService {
    final EventDao eventDao;
    final UserDao userDao;

    final NotificationDao notificationDao;

    @Autowired
    public BaseService(DaoRepository daoRepository) {
        this(daoRepository.getEventDao(), daoRepository.getUserDao(), daoRepository.getNotificationDao());

    }

    private User evaluateUser(Optional<User> user, Optional<UserType> userType) {
        if (!user.isPresent()) {
            throw ResponseException.builder().message("You must be logged in to do this.").type(ResponseType.UNAUTHORIZED).build();
        }
        userType.ifPresent(userType1 -> {
            if (!user.get().getType().hasEqualRightsTo(userType1)) {
                throw ResponseException.builder().message("You are not authorized to make this type of request").type(ResponseType.UNAUTHORIZED).build();
            }
        });
        return user.get();
    }

    User evaluateUserIsAuthorized(Optional<User> user, UserType type) {
        return evaluateUser(user, Optional.ofNullable(type));
    }

    private User evaluateUserIsAuthorized(String userString, UserType userType) {
        return evaluateUser(userDao.findByString(userString), Optional.ofNullable(userType));
    }

    void evaluateUserIsAuthorized(User user, UserType userType) {
        evaluateUser(Optional.ofNullable(user), Optional.ofNullable(userType));
    }

    User evaluateUserLoggedIn(String userString) {
        return evaluateUserIsAuthorized(userString, UserType.STUDENT);
    }

    User evaluateUserLoggedIn(Optional<User> user) {
        return evaluateUserIsAuthorized(user, UserType.STUDENT);
    }

    Event evaluateEvent(String eventId) {
        Optional<Event> event = eventDao.findById(eventId);
        if (!event.isPresent()) {
            throw ResponseException.builder().message("Event not found!").type(ResponseType.NOT_FOUND).build();
        }
        return event.get();
    }
}
