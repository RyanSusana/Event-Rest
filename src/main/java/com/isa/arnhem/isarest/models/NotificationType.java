package com.isa.arnhem.isarest.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationType {
    MESSAGE(0), NEW_SALE(0);
    private final int priority;
}
