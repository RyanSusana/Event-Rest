package com.isa.arnhem.isarest.dao;

import org.jongo.Find;
import org.jongo.FindOne;
import org.jongo.Jongo;

public class CrudDao<T> extends Dao implements Crud<T> {
    public CrudDao(Jongo jongo, String collectionName) {
        super(jongo, collectionName);
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
