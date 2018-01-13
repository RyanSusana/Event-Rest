package com.isa.arnhem.isarest.dao;

import org.jongo.Jongo;
import org.jongo.MongoCollection;


public abstract class Dao {
    private final Jongo jongo;
    private final String collectionName;


    public Dao(Jongo jongo,  String collectionName) {
        this.collectionName = collectionName;
        this.jongo = jongo;
    }

    public MongoCollection getCollection() {
        return jongo.getCollection(collectionName);
    }


}
