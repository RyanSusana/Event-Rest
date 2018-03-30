package com.isa.arnhem.isarest.repository;

import com.google.common.collect.Lists;
import com.isa.arnhem.isarest.dto.BasicUserDTO;
import com.isa.arnhem.isarest.models.user.User;
import com.isa.arnhem.isarest.models.user.UserType;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationStrength;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class UserDao extends CrudDao<User> {

    @Autowired
    public UserDao(Jongo jongo) {
        super(jongo, "users");
    }

    public Optional<User> findByUsername(String username) {
        final Iterator<User> user = getCollection().find("{username: #}", username)
                .with(dbCursor -> dbCursor.setCollation(Collation.builder()
                        .collationStrength(CollationStrength.PRIMARY)
                        .locale("en")
                        .build())).as(User.class).iterator();
        return user.hasNext() ? Optional.of(user.next()) : Optional.empty();
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
        final Iterator<User> user = getCollection().find("{email: #}", email)
                .with(dbCursor -> dbCursor.setCollation(Collation.builder()
                        .collationStrength(CollationStrength.PRIMARY)
                        .locale("en")
                        .build())).as(User.class).iterator();
        return user.hasNext() ? Optional.of(user.next()) : Optional.empty();
    }

    public Set<BasicUserDTO> search(String s, int limit) {
        Set<BasicUserDTO> results = new TreeSet<>((o1, o2) -> o1.getUsername().compareToIgnoreCase(o2.getUsername()));
        final Pattern pattern = Pattern.compile("(?i).*" + s + ".*");
        find("{$or: [{email: #}, {username: #}]}", pattern, pattern).limit(limit).as(User.class).iterator().forEachRemaining(u -> results.add(new BasicUserDTO(u.getReference())));
        return results;
    }

    public Set<User> getAll() {
        return Lists.newArrayList(find().as(User.class).iterator())
                .stream()
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>((o1, o2) -> o1.getUsername().compareToIgnoreCase(o2.getUsername()))));
    }

    public Optional<User> findById(String userId) {
        return Optional.ofNullable(findOne("{_id: #}", userId).as(User.class));
    }

    public Optional<User> findByTempId(String userId) {
        return Optional.ofNullable(findOne("{temp_id: #}", userId).as(User.class));
    }

    @Override
    public void update(User item) {
        getCollection().update("{_id: #}", item.getId()).with(item);
    }


}
