package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "event_type")
@JsonSubTypes({

        @JsonSubTypes.Type(value = ControlledEvent.class, name = "CONTROLLED"),
        @JsonSubTypes.Type(value = NormalEvent.class, name = "NORMAL")
})
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public abstract class Event implements Comparable<Event>, Identifiable {

    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("priority")
    private int priority = 0;

    @JsonProperty("description")
    private Description description;

    @JsonProperty("main_image")
    private String mainImage;

    @JsonProperty("attendees")
    private AttendeeSet attendees = new AttendeeSet();

    @JsonProperty("price")
    private BigDecimal price = new BigDecimal(0);

    @JsonProperty("properties")
    private Map<String, Object> properties = new HashMap<>();

    @JsonProperty("date")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date date;

    @Override
    public int compareTo(Event o) {
        int priorityCompare = Integer.compare(this.priority, o.priority);

        if (priorityCompare == 0) {
            return o.date.compareTo(this.date);
        }

        return priorityCompare;
    }


}
