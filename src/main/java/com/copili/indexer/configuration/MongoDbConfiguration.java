package com.copili.indexer.configuration;

import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.copili.indexer.repository.mongodb")
@EnableMongoAuditing
public class MongoDbConfiguration {

    private final static Logger log = LoggerFactory.getLogger(MongoDbConfiguration.class);

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        log.debug("Instanciando MongoDbFactory");
        return new SimpleMongoDbFactory(new MongoClient(), "feeder");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        log.debug("Instanciando MongoTemplate");
        return new MongoTemplate(mongoDbFactory());
    }

}
