package com.isa.arnhem.isarest.repository;

import lombok.Getter;
import org.jongo.Jongo;
import org.jongo.MongoCollection;


public abstract class Dao {
    private final Jongo jongo;
    @Getter
    private final String collectionName;


    public Dao(Jongo jongo, String collectionName) {
        this.collectionName = collectionName;
        this.jongo = jongo;
    }

    MongoCollection getCollection() {
        return jongo.getCollection(collectionName);
    }


}
