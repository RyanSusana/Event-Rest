package com.isa.arnhem.isarest.dao;

import org.jongo.Find;
import org.jongo.FindOne;


public interface Crud<T> {

    void create(T... items);
    void delete(String query, Object... params);
    void delete(String query);

    void update(T item);

    Find find(String query);
    Find find(String query, Object... params);
    FindOne findOne(String query);
    FindOne findOne(String query, Object... params);

    default Find find() {
        return find("{}");
    }

    default void deleteAll() {
        delete("{}");
    }

    default void update(T[] items) {
        for (T item : items) {
            update(item);
        }
    }


}
