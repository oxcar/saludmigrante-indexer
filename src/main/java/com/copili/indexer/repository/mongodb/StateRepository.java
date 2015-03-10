package com.copili.indexer.repository.mongodb;

import com.copili.indexer.domain.State;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StateRepository extends MongoRepository<State, String> {
}
