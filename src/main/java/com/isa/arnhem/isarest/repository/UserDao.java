package com.isa.arnhem.isarest.repository;

import com.google.common.collect.Lists;
import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserType;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao extends CrudDao<User> {

    @Autowired
    public UserDao(Jongo jongo) {
        super(jongo, "users");
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(findOne("{username: #}", username).as(User.class));
    }

    public List<User> findByTypeAndAbove(UserType type) {

        List<UserType> ranksToCheck = type.getRanksAbove();
        ranksToCheck.add(type);
        return Lists.newArrayList(find("{type: {$in:#}}}", ranksToCheck).as(User.class).iterator());
    }

    public Optional<User> findByUsernameOrEmail(String s) {
        return findByUsername(s).or(() -> findByEmail(s));
    }

    public Optional<User> findByString(String s) {
        return findByUserId(s).or(() -> findByUsernameOrEmail(s));
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(findOne("{email: #}", email).as(User.class));
    }

    public Optional<User> findByUserId(String userId) {
        return Optional.ofNullable(findOne("{_id: #}", userId).as(User.class));
    }

    public void deleteByUserId(String userId) {
        delete("{_id: #}", userId);
    }

    @Override
    public void update(User item) {
        getCollection().update("{_id: #}", item.getId()).with(item);
    }


}
