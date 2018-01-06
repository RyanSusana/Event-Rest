package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Event {

    @JsonProperty("event_id")
    private final String id;
    private String name;
    private Description description;

    @JsonProperty("main_image")
    private String mainImage;
    private List<Attendee> attendees = new ArrayList<>();
    private BigDecimal price = new BigDecimal(0);


    private Map<String, Object> properties = new HashMap<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }




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
}
