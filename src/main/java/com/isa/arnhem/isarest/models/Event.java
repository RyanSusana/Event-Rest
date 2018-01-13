package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,   property = "event_type")
@JsonSubTypes({

       @JsonSubTypes.Type(value = ControlledEvent.class, name = "CONTROLLED"),
      @JsonSubTypes.Type(value = NormalEvent.class, name = "NORMAL")
})
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public  abstract class Event implements Comparable<Event>, Identifiable {

    private String id = UUID.randomUUID().toString();

    @JsonProperty("name")
    private String name;

    @JsonProperty("priority")
    private int priority = 0;

    @JsonProperty("description")
    private Description description;

    @JsonProperty("main_image")
    private String mainImage;

    @JsonProperty("attendees")
    private List<Attendee> attendees = new ArrayList<>();

    @JsonProperty("price")
    private BigDecimal price = new BigDecimal(0);

    @JsonProperty("properties")
    private Map<String, Object> properties = new HashMap<>();

    @JsonProperty("date")
    private ZonedDateTime date;

    @Override
    public int compareTo(Event o) {
        int priorityCompare = Integer.compare(this.priority, o.priority);

        if (priorityCompare == 0) {
            return this.date.compareTo(o.date);
        }

        return priorityCompare;
    }
}
