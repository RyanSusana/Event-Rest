package com.isa.arnhem.isarest.dao;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.context.annotation.PropertySource;


public abstract class Dao {
    private final Jongo jongo;
    private final String collectionName;

    private String databaseName;

    public Dao(Jongo jongo,  String collectionName) {
        this.collectionName = collectionName;
        this.jongo = jongo;
    }

    MongoCollection getCollection() {
        return jongo.getCollection(collectionName);
    }


}
