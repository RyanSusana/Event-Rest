package com.isa.arnhem.isarest.dao;

import com.isa.arnhem.isarest.models.User;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends CrudDao<User> {
    public UserDao(MongoClient client) {
        super(client, "users");
    }

    @Autowired
    public UserDao(IsaMongoClient client) {
        super(client, "users");
    }

    public User findByUsername(String username) {
        return getCollection().findOne("{username: #}", username).as(User.class);
    }
    public User findByEmail(String email) {
        return getCollection().findOne("{email: #}", email).as(User.class);
    }
    public User findByUserId(String userId) {
        return getCollection().findOne("{user_id: #}", userId).as(User.class);
    }

}
