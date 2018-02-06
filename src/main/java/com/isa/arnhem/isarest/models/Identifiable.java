package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Identifiable {
    @JsonProperty("_id")
    String getId();

    void setId(String id);
}
