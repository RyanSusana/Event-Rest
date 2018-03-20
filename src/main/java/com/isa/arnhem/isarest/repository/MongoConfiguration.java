package com.isa.arnhem.isarest.repository;

import com.isa.arnhem.isarest.repository.serialization.CustomObjectIdUpdater;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.jongo.Jongo;
import org.jongo.marshall.jackson.JacksonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;

@Configuration
@PropertySource("application.properties")
public class MongoConfiguration {

    @Value("${database_server_address}")
    private String serverAddress;

    @Value("${database_server_port}")
    private int serverPort;

    @Value("${database_name}")
    private String databaseName;

    @Value("${database_username}")
    private String databaseUsername;

    @Value("${database_password}")
    private String databasePassword;

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient(new ServerAddress(
                serverAddress, serverPort), Arrays.asList(MongoCredential.createCredential(databaseUsername,databaseName,databasePassword.toCharArray())));
    }

    @Bean
    public Jongo jongo() {
        return new Jongo(mongoClient().getDB(databaseName), new JacksonMapper.Builder()
                .withObjectIdUpdater(new CustomObjectIdUpdater())
                .build());
    }

}
