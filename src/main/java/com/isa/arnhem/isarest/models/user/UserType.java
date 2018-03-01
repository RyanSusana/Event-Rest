package com.isa.arnhem.isarest.models.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum UserType implements Comparable<UserType> {
    ISA_ADMIN(100), ISA_SALES_TEAM(75), ISA_TECH(75), ISA_CREATIVE(75), ISA_MEMBER(50), SUPER_ADMIN(10000000), STUDENT(0), SUSPENDED(-1), BANNED(-10);

    private final int level;

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


}
