package com.isa.arnhem.isarest.dao;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource("application.properties")
public class IsaMongoConfiguration  {

    @Value("${database_server_address}")
    private String serverAddress;

    @Value("${database_server_port}")
    private int serverPort;

    @Bean
    public MongoClient mongoClient(){
        return new MongoClient(new ServerAddress(
         serverAddress, serverPort));
    }

}
