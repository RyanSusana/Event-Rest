package com.isa.arnhem.isarest.repository;

import com.google.common.collect.Lists;
import com.isa.arnhem.isarest.models.Notification;
import com.isa.arnhem.isarest.models.NotificationType;
import com.isa.arnhem.isarest.models.ResultPage;
import com.isa.arnhem.isarest.models.User;
import org.jongo.Jongo;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public class NotificationDao extends CrudDao<Notification> {

    public NotificationDao(Jongo jongo) {
        super(jongo, "notifications");
    }

    @Override
    public void update(Notification item) {

    }

    public ResultPage<Notification> query(int pageSize, int pageNumber, String query) {
        ResultPage<Notification> results = new ResultPage<>();
        if (query == null) {
            query = "{}";
        }
        int amountOfLogs = Math.toIntExact(getCollection().count(query));
        int numberOfPages = amountOfLogs / pageSize;

        if (amountOfLogs % pageSize > 0) numberOfPages++;
        if (pageNumber > numberOfPages)
            results.setValues(Lists.newArrayList(find(query).skip((pageSize * pageNumber)).limit(pageSize).as(Notification.class).iterator()));


        results.setNumberOfPages(numberOfPages);
        results.setPageNumber(pageNumber);
        results.setPageSize(pageSize);
        return results;
    }

    public void notify(String notification, NotificationType type, User... users) {
        notify(notification, type, Lists.newArrayList(users));
    }

    public void notify(String notif, NotificationType type, Collection<User> users) {
        users.forEach((user) -> {
            Notification notification = new Notification();
            notification.setMessage(notif);
            notification.setUserId(user.getId());
            notification.setType(type);
            notification.setDate(Calendar.getInstance().getTime());
            create(notification);
        });
    }

    @Override
    public Optional<Notification> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Set<Notification> getAll() {
        return null;
    }
}
