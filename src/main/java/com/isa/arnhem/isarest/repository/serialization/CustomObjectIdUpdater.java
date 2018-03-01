package com.isa.arnhem.isarest.repository.serialization;

import com.isa.arnhem.isarest.models.Identifiable;
import org.bson.types.ObjectId;
import org.jongo.ObjectIdUpdater;

public class CustomObjectIdUpdater implements ObjectIdUpdater {


    public boolean mustGenerateObjectId(Object pojo) {
        Identifiable obj = translate(pojo);
        return obj.getId() == null || obj.getId().trim().isEmpty();
    }

    public Object getId(Object pojo) {
        return translate(pojo).getId();
    }

    public void setObjectId(Object target, ObjectId id) {
        Identifiable pojo = translate(target);
        pojo.setId(id.toString());
    }

    private Identifiable translate(Object pojo) {
        if (!(pojo instanceof Identifiable)) {
            throw new IllegalArgumentException("Cannot identify: " + pojo.getClass().getName());
        }

        return (Identifiable) pojo;
    }
}