package com.isa.arnhem.isarest.dao;

import com.google.common.collect.Lists;
import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserType;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao extends CrudDao<User> {

    @Autowired
    public UserDao(Jongo jongo) {
        super(jongo, "users");
    }

    public User findByUsername(String username) {
        return findOne("{username: #}", username).as(User.class);
    }

    public List<User> findByTypeAndAbove(UserType type) {

        List<UserType> ranksToCheck = type.getRanksAbove();
        ranksToCheck.add(type);
        return Lists.newArrayList(find("{type: {$in:#}}}", ranksToCheck).as(User.class).iterator());
    }

    public User findByEmail(String email) {
        return findOne("{email: #}", email).as(User.class);
    }

    public User findByUserId(String userId) {
        return findOne("{user_id: #}", userId).as(User.class);
    }

    public void deleteByUserId(String userId) {
        delete("{user_id: #}", userId);
    }

    @Override
    public void update(User item) {
        getCollection().update("{user_id: #}", item.getId()).with(item);
    }


}
