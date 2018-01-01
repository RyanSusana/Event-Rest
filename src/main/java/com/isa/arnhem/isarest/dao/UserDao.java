package com.isa.arnhem.isarest.dao;

import com.isa.arnhem.isarest.models.User;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends CrudDao<User> {

    @Autowired
    public UserDao(Jongo jongo) {
        super(jongo, "users");
    }

    public User findByUsername(String username) {
        return findOne("{username: #}", username).as(User.class);
    }
    public User findByEmail(String email) {
        return findOne("{email: #}", email).as(User.class);
    }
    public User findByUserId(String userId) {
        return findOne("{user_id: #}", userId).as(User.class);
    }

}
