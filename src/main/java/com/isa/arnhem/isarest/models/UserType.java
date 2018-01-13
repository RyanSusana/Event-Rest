package com.isa.arnhem.isarest.models;

import java.util.ArrayList;
import java.util.List;

public enum UserType {
    MEMBER(50), ADMIN(100), STUDENT(0);

    private final int level;

    UserType(final int level) {
        this.level = level;
    }


    public List<UserType> getRanksAbove() {
        List<UserType> list = new ArrayList<>();
        for (UserType userType : UserType.values()) {
            if (userType.level > level) {
                list.add(userType);
            }
        }
        return list;
    }

    public int getLevel() {
        return level;
    }
}
