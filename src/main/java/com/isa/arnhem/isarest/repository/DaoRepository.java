package com.isa.arnhem.isarest.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Getter
public class DaoRepository {
    final EventDao eventDao;
    final UserDao userDao;
    final NotificationDao notificationDao;

}
