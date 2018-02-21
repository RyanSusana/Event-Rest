package com.isa.arnhem.isarest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.isa.arnhem.isarest.models.Description;
import com.isa.arnhem.isarest.models.Event;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Getter
public class EventDTO implements Comparable<EventDTO> {
    @JsonProperty("_id")
    private final String id;
    @JsonProperty("priority")
    private final int priority;
    @JsonProperty("description")
    private final Description description;
    @JsonProperty("main_image")
    private final String mainImage;
    @JsonProperty("event_type")
    private final String type;
    @JsonProperty("price")
    private final BigDecimal price;
    @JsonProperty("properties")
    private final Map<String, Object> properties;
    @JsonProperty("date")
    private final Date date;
    @JsonProperty("slug")
    private final String slug;
    @JsonProperty("name")
    private String name;

    public EventDTO(Event event) {
        this.name = event.getName();
        this.description = event.getDescription();
        this.priority = event.getPriority();
        this.mainImage = event.getMainImage();
        this.price = event.getPrice();
        this.properties = event.getProperties();
        this.id = event.getId();
        this.date = event.getDate();
        this.type = event.getType();
        this.slug = event.getSlug();
    }

    @Override
    public int compareTo(EventDTO o) {
        int priorityCompare = Integer.compare(this.priority, o.priority);

        if (priorityCompare == 0) {
            return o.date.compareTo(this.date);
        }

        return priorityCompare;
    }

    void setName(String name) {
        this.name = name;
    }
}
