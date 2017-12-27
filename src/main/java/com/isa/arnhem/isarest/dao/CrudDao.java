package com.isa.arnhem.isarest.dao;

import com.mongodb.MongoClient;
import org.jongo.Find;
import org.jongo.FindOne;

public class CrudDao<T> extends Dao implements Crud<T> {
    public CrudDao(MongoClient client, String collectionName) {
        super(client, collectionName);
    }

    @Override
    public void create(T... items) {
        getCollection().insert(items);
    }

    @Override
    public void delete(String query) {
        getCollection().remove(query);
    }

    @Override
    public void save(T item) {
        getCollection().save(item);
    }

    @Override
    public Find find(String query) {

        return getCollection().find(query);
    }

    @Override
    public FindOne findOne(String query) {
        return getCollection().findOne(query);
    }

}
