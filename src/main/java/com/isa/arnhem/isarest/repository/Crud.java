package com.isa.arnhem.isarest.repository;

import com.isa.arnhem.isarest.models.Identifiable;
import org.jongo.Find;
import org.jongo.FindOne;

import java.util.Collection;
import java.util.Optional;


public interface Crud<T extends Identifiable> {

    void create(T... items);

    void delete(String query, Object... params);

    void delete(String query);

    void delete(T item);

    void update(T item);

    Find find(String query);

    Find find(String query, Object... params);


    FindOne findOne(String query);

    FindOne findOne(String query, Object... params);

    Optional<T> findById(String id);

    Collection<T> getAll();

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

    default void create(Collection<T> items) {
        items.forEach(this::create);
    }


}
