package com.isa.arnhem.isarest.models.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.isa.arnhem.isarest.models.user.UserReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventPayment {
    @JsonProperty("user")
    private UserReference user;

    @JsonProperty("payment_id")
    private String paymentId;

    @JsonProperty("payed_amount")
    private BigDecimal payedAmount;

}
