package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventPayment {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("payment_id")
    private String paymentId;
}
