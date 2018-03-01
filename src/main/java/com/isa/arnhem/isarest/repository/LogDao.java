package com.isa.arnhem.isarest.repository;

import com.google.common.collect.Lists;
import com.isa.arnhem.isarest.models.Log;
import com.isa.arnhem.isarest.models.ResultPage;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class LogDao extends CrudDao<Log> {

    @Autowired
    public LogDao(Jongo jongo) {
        super(jongo, "logs");
    }


    public ResultPage<Log> query(int pageSize, int pageNumber, String query) {
        ResultPage<Log> results = new ResultPage<>();
        if (query == null) {
            query = "{}";
        }
        results.setValues(Lists.newArrayList(find(query).skip((pageSize * pageNumber)).limit(pageSize).as(Log.class).iterator()));
        int amountOfLogs = Math.toIntExact(getCollection().count(query));
        int numberOfPages = amountOfLogs / pageSize;

        if (amountOfLogs % pageSize > 0) numberOfPages++;

        results.setNumberOfPages(numberOfPages);
        results.setPageNumber(pageNumber);
        results.setPageSize(pageSize);
        return results;
    }

    public Optional<Log> findById(String id) {
        return Optional.ofNullable(findOne("{_id: #}", id).as(Log.class));
    }

    public Set<Log> getAll() {
        return new HashSet<>(Lists.newArrayList(find().as(Log.class).iterator()));
    }


}