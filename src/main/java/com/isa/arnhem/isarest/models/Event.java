package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.ImmutableMap;
import com.isa.arnhem.isarest.models.Description;
import com.isa.arnhem.isarest.models.Representable;
import com.isa.arnhem.isarest.models.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class Event {

    @JsonProperty("user_id")
    private final String id;
    private String name;
    private Description description;
    @JsonProperty("start_date")
    private LocalDateTime startDate;
    @JsonProperty("end_date")
    private LocalDateTime endDate;
    private final Map<String, Object> details;
    @JsonProperty("main_image")
    private String mainImage;
    private User creator;

    private BigDecimal price;

    public Event(String name, Description description, LocalDateTime startDate, LocalDateTime endDate, Map<String, Object> details, User creator) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.details = details;
        this.creator = creator;
    }

    public Event() {
        id = UUID.randomUUID().toString();
        details = new HashMap<>();
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public <T extends Number> void put(String key, T value) {
        details.put(key, value);
    }

    public void put(String key, String value) {
        details.put(key, value);
    }

    public void put(String key, Boolean value) {
        details.put(key, value);
    }

    public ImmutableMap<String, Object> getDetails() {
        return ImmutableMap.copyOf(details);
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

}
