package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;


public class Log implements Identifiable {

    @Getter
    @Setter
    @JsonProperty("_id")
    private String id;

    @JsonProperty("date")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date date;


    @JsonProperty("properties")
    private Map<String, Object> properties;


    public ImmutableMap<String, Object> getProperties() {
        return ImmutableMap.copyOf(properties);
    }

    public void setProperties(Map<String, Object> properties) {
        for (String key : properties.keySet()) {
            Object obj = properties.get(key);
            if (!(obj instanceof Number || obj instanceof CharSequence || obj instanceof Date)) {
                throw new IllegalArgumentException("Log properties can not contain Objects!");
            }
        }

        this.properties = properties;
    }

    public void putProperty(String key, Number value) {
        this.properties.put(key, value);
    }

    public void putProperty(String key, CharSequence value) {
        this.properties.put(key, value);
    }


}
