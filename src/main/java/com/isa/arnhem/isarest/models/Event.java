package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = NormalEvent.class, include = JsonTypeInfo.As.PROPERTY, property = "event_type")
@JsonSubTypes({

        @JsonSubTypes.Type(value = ControlledEvent.class, name = "CONTROLLED"),
        @JsonSubTypes.Type(value = NormalEvent.class, name = "NORMAL")
})
public abstract class Event implements Comparable<Event> {

    @JsonProperty("event_id")
    private final String id;

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


    public Event(String name, Description description) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
    }

    public Event() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }


    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int compareTo(Event o) {
        return Integer.compare(this.getPriority(), o.getPriority());
    }
}
