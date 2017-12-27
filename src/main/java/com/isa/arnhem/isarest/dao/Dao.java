package com.isa.arnhem.isarest.dao;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.context.annotation.PropertySource;


@PropertySource("application.properties")
public abstract class Dao {
    final Jongo jongo;
    final String collectionName;

    private String databaseName;

    public Dao(Jongo jongo,  String collectionName) {
        this.collectionName = collectionName;
        this.jongo = jongo;
    }

    public MongoCollection getCollection() {
        return jongo.getCollection(collectionName);
    }


}
