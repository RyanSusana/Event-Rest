package com.isa.arnhem.isarest.repository;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.properties")
public class MongoConfiguration  {

    @Value("${database_server_address}")
    private String serverAddress;

    @Value("${database_server_port}")
    private int serverPort;

    @Value("${database_name}")
    private String databaseName;

    @Bean
    public MongoClient mongoClient(){
        return new MongoClient(new ServerAddress(
                serverAddress, serverPort));
    }
    @Bean
    public Jongo jongo(){
        return new Jongo(mongoClient().getDB(databaseName));
    }

}
