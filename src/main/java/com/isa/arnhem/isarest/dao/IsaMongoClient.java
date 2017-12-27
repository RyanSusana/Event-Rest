package com.isa.arnhem.isarest.dao;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.springframework.stereotype.Component;

@Component
public class IsaMongoClient extends MongoClient {

    public IsaMongoClient() {
        super(new ServerAddress(
                "localhost", 27017));
    }

}
