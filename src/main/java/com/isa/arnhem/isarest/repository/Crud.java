package com.isa.arnhem.isarest.repository;

import com.isa.arnhem.isarest.models.Identifiable;
import org.jongo.Find;
import org.jongo.FindOne;

import java.util.Collection;


public interface Crud<T extends Identifiable> {

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

    default void create(Collection<T> items){
        items.forEach(this::create);
    }


}
