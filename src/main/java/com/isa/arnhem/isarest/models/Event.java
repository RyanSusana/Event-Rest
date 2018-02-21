package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.slugify.Slugify;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "event_type", visible = true)
@JsonSubTypes({

        @JsonSubTypes.Type(value = ControlledEvent.class, name = "CONTROLLED"),
        @JsonSubTypes.Type(value = NormalEvent.class, name = "NORMAL"),
        @JsonSubTypes.Type(value = PayedEvent.class, name = "ONLINE_PAYED")
})
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public abstract class Event implements Comparable<Event>, Identifiable {

    private String id;

    @JsonProperty("slug")

    private String slug;

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

    @JsonProperty("event_type")
    private String type;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlugToName() {
        slug = new Slugify().withCustomReplacement("'s", "s").slugify(name);
    }

}
