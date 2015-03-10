package com.copili.indexer.repository.mongodb;

import com.copili.indexer.domain.Copi;
import com.copili.indexer.domain.CopiExperience;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CopiExperienceRepository extends MongoRepository<CopiExperience, String> {

    List<CopiExperience> findByIndexed(Boolean indexed);

}
