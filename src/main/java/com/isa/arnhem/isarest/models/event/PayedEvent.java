package com.isa.arnhem.isarest.models.event;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
public class PayedEvent extends ControlledEvent {

    private Set<EventPayment> payments = new HashSet<>();


    public BigDecimal getRevenueGenerated() {
        return getPrice().multiply(BigDecimal.valueOf(payments.size()));
    }
}
