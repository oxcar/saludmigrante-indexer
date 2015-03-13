package com.copili.indexer.service;

import com.copili.indexer.domain.CopiChange;
import com.copili.indexer.domain.CopiExperience;
import com.copili.indexer.domain.CopiLearn;

import java.util.List;

public interface ElasticSearchService {

    List<CopiExperience> findExperienceCopis(String query, String state, Integer from, Integer size);

    List<CopiLearn> findLearnCopis(String id, Integer from, Integer size);

    List<CopiChange> findChangeCopis(String id, Integer from, Integer size);

}
