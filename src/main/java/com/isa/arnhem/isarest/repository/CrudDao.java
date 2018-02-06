package com.isa.arnhem.isarest.repository;

import com.isa.arnhem.isarest.models.Identifiable;
import org.jongo.Find;
import org.jongo.FindOne;
import org.jongo.Jongo;

public abstract class CrudDao<T extends Identifiable> extends Dao implements Crud<T> {
    public CrudDao(Jongo jongo, String collectionName) {
        super(jongo, collectionName);
    }

    @Override
    public void create(T... items) {
        getCollection().insert(items);
    }

    @Override
    public void delete(String query, Object... params) {
        getCollection().remove(query, params);
    }

    @Override
    public void delete(String query) {
        getCollection().remove(query);
    }

    @Override
    public void delete(T item) {
        getCollection().remove("{_id: #}", item.getId());
    }

    @Override
    public void update(T item) {
        getCollection().update("{_id: #}", item.getId()).with(item);
    }

    @Override
    public Find find(String query) {
        return getCollection().find(query);
    }

    @Override
    public Find find(String query, Object... params) {
        return getCollection().find(query, params);
    }

    @Override
    public FindOne findOne(String query) {
        return getCollection().findOne(query);
    }

    @Override
    public FindOne findOne(String query, Object... params) {
        return getCollection().findOne(query, params);
    }

}
