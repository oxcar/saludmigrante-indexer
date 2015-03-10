package com.copili.indexer.repository.mongodb;

import com.copili.indexer.domain.Copi;
import com.copili.indexer.domain.CopiLearn;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CopiLearnRepository extends MongoRepository<CopiLearn, String> {

    List<CopiLearn> findByIndexed(Boolean indexed);

}
