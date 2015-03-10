package com.copili.indexer.repository.mongodb;

import com.copili.indexer.domain.CopiChange;
import com.copili.indexer.domain.CopiLearn;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CopiChangeRepository extends MongoRepository<CopiChange, String> {

    List<CopiChange> findByIndexed(Boolean indexed);

}
