package com.isa.arnhem.isarest.dao;

import com.google.common.collect.Lists;
import org.jongo.Find;
import org.jongo.FindOne;

import java.util.List;


public interface Crud<T> {

    void create(T... items);

    void delete(String query);

    void save(T item);

    Find find(String query);
    FindOne findOne(String query);

    default Find find() {
        return find("{}");
    }

    default void deleteAll() {
        delete("{}");
    }

    default void save(T[] items) {
        for (T item : items) {
            save(item);
        }
    }


}
