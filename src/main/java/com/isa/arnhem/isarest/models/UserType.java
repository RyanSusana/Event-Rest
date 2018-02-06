package com.isa.arnhem.isarest.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum UserType {
    ISA_MEMBER(50), ISA_ADMIN(100), STUDENT(0), SUPER_ADMIN(10000000), SUSPENDED(-1), BANNED(-10), ISA_SALES_TEAM(75), ISA_TECH(75), ISA_CREATIVE(75);

    private final int level;

    public List<UserType> getRanksAbove() {
        List<UserType> list = new ArrayList<>();
        for (UserType userType : UserType.values()) {
            if (userType.level >= level) {
                list.add(userType);
            }
        }
        return list;
    }

    public List<UserType> getRanksBelow() {
        List<UserType> list = new ArrayList<>();
        for (UserType userType : UserType.values()) {
            if (userType.level < level) {
                list.add(userType);
            }
        }
        return list;
    }

    public boolean hasEqualRightsTo(UserType other) {
        return this.level >= other.level;
    }

    public boolean hasMoreRightsThan(UserType other) {
        return this.level > other.level;
    }

    public static Optional<UserType> getRank(String s) {
        try {
            return Optional.of(UserType.valueOf(s.trim().replace(" ", "_").toUpperCase()));
        } catch (Exception e) {
            if (!s.toUpperCase().contains("ISA")) {
                return getRank("ISA_" + s);
            } else {
                return Optional.empty();
            }
        }
    }
}
