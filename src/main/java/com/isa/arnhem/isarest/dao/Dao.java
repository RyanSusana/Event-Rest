package com.isa.arnhem.isarest.dao;

import com.isa.arnhem.isarest.IsaRestApplication;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

public abstract class Dao {
    final MongoClient client;
    final Jongo jongo;
    final String collectionName;

    public Dao(MongoClient client, String collectionName) {
        this.client = client;
        this.collectionName = collectionName;
        jongo = new Jongo(client.getDB(IsaRestApplication.DATABASE_NAME));
    }

    public MongoCollection getCollection() {
        return jongo.getCollection(collectionName);
    }
}
