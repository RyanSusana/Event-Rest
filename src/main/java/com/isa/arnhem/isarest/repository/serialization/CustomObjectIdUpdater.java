package com.isa.arnhem.isarest.repository.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.introspect.BasicClassIntrospector;
import com.isa.arnhem.isarest.models.Identifiable;
import org.bson.types.ObjectId;
import org.jongo.ObjectIdUpdater;

public class CustomObjectIdUpdater implements ObjectIdUpdater {

    private final ObjectMapper mapper;

    public CustomObjectIdUpdater(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public boolean mustGenerateObjectId(Object pojo) {
        Identifiable obj = translate(pojo);
        return obj.getId() == null;
    }

    public Object getId(Object pojo) {
        return translate(pojo).getId();
    }

    public void setObjectId(Object target, ObjectId id) {
        Identifiable pojo = translate(target);
        pojo.setId(id.toString());
    }

    private BasicBeanDescription beanDescription(Class<?> cls) {
        BasicClassIntrospector bci = new BasicClassIntrospector();
        return bci.forSerialization(mapper.getSerializationConfig(), mapper.constructType(cls), mapper.getSerializationConfig());
    }

    private Identifiable translate(Object pojo) {
        if (!(pojo instanceof Identifiable)) {
            throw new IllegalArgumentException("Cannot identify: " + pojo.getClass().getName());
        }

        Identifiable object = (Identifiable) pojo;
        return object;
    }
}