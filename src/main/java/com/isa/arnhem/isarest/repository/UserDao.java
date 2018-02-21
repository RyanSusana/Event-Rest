package com.isa.arnhem.isarest.repository;

import com.google.common.collect.Lists;
import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserType;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class UserDao extends CrudDao<User> {

    @Autowired
    public UserDao(Jongo jongo) {
        super(jongo, "users");
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(findOne("{username: #}", Pattern.compile("(?i)" + username)).as(User.class));
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
        return findById(s).or(() -> findByUsernameOrEmail(s));
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(findOne("{email: #}", Pattern.compile("(?i)" + email)).as(User.class));
    }

    public List<User> searchByUsername(String s) {
        return Lists.newArrayList(find("{username: #}", Pattern.compile("(?i).*" + s + ".*")).as(User.class).iterator());
    }

    public List<User> searchByEmail(String s) {
        return Lists.newArrayList(find("{email: #}", Pattern.compile("(?i).*" + s + ".*")).as(User.class).iterator());
    }

    public Set<User> search(String s) {
        Set<User> results = new TreeSet<>((o1, o2) -> o1.getUsername().compareToIgnoreCase(o2.getUsername()));
        results.addAll(searchByUsername(s));
        results.addAll(searchByEmail(s));
        return results;
    }

    public Set<User> getAll() {
        return Lists.newArrayList(find().as(User.class).iterator()).stream().collect(Collectors.toSet());
    }

    public Optional<User> findById(String userId) {
        return Optional.ofNullable(findOne("{_id: #}", userId).as(User.class));
    }

    @Override
    public void update(User item) {
        getCollection().update("{_id: #}", item.getId()).with(item);
    }


}
