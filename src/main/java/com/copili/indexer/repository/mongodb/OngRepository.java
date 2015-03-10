package com.copili.indexer.repository.mongodb;

import com.copili.indexer.domain.Ong;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OngRepository extends MongoRepository<Ong, String> {
}
