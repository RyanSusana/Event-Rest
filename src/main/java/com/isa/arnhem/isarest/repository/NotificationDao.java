package com.isa.arnhem.isarest.repository;

import com.isa.arnhem.isarest.models.Notification;
import org.jongo.Jongo;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationDao extends CrudDao<Notification> {

    public NotificationDao(Jongo jongo) {
        super(jongo, "notifications");
    }

    @Override
    public void update(Notification item) {

    }
}
