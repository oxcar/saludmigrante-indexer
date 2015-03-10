package com.copili.indexer.cron;

import com.copili.indexer.domain.CopiChange;
import com.copili.indexer.domain.CopiExperience;
import com.copili.indexer.domain.CopiLearn;
import com.copili.indexer.domain.Ong;
import com.copili.indexer.repository.mongodb.CopiChangeRepository;
import com.copili.indexer.repository.mongodb.CopiExperienceRepository;
import com.copili.indexer.repository.mongodb.CopiLearnRepository;
import com.copili.indexer.repository.mongodb.OngRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Component
public class IndexerCronJob {

    private final static Logger log = LoggerFactory.getLogger(IndexerCronJob.class);

    @Autowired
    private CopiLearnRepository copiLearnRepository;

    @Autowired
    private CopiExperienceRepository copiExperienceRepository;

    @Autowired
    private CopiChangeRepository copiChangeRepository;

    @Autowired
    private OngRepository ongRepository;

    private Node node;

    private Client client;

    private ObjectMapper mapper = new ObjectMapper();

    public IndexerCronJob() {
        node = nodeBuilder().client(true).node();
        client = node.client();
    }

    @PreDestroy
    public void preDestroy() {
        node.close();
    }

    @Scheduled(fixedDelay = 10000)
    public void indexCopis() {
        log.info("Indexing copis - Start");
        try {

            // Learn Copis
            List<CopiLearn> learnCopis = copiLearnRepository.findByIndexed(false);
            for (CopiLearn copi : learnCopis) {
                if (indexCopiLearn(copi)) {
                    copi.setIndexed(true);
                    copiLearnRepository.save(copi);
                }
            }

            // Experience Copis
            List<CopiExperience> experienceCopis = copiExperienceRepository.findByIndexed(false);
            for (CopiExperience copi : experienceCopis) {
                if (indexCopiExperience(copi)) {
                    copi.setIndexed(true);
                    copiExperienceRepository.save(copi);
                }
            }

            // Change Copis
            List<CopiChange> changeCopis = copiChangeRepository.findByIndexed(false);
            for (CopiChange copi : changeCopis) {
                if (indexCopiChange(copi)) {
                    copi.setIndexed(true);
                    copiChangeRepository.save(copi);
                }
            }

        } catch (JsonProcessingException e) {
            log.error("Error al Indexar copis: {}", e.getMessage());
        }
        log.info("Indexing copis - End");
    }

    private boolean indexCopiLearn(CopiLearn copiLearn) throws JsonProcessingException {
        log.info("Indexing Copi Learn");
        if (copiLearn.isValid()) {
            String urlHash = DigestUtils.shaHex(copiLearn.getUrl());
            if (!existIndexedCopiByUrlHash(urlHash)) {
                copiLearn.setUrlHash(urlHash);
                String json = mapper.writeValueAsString(copiLearn);
                IndexResponse response = client.prepareIndex("copis", "learn").setSource(json).execute().actionGet();
                log.info("Indexed Copi en {}/{}/{}", response.getIndex(), response.getType(), response.getId());
            }
            return true;
        }
        return false;
    }

    private boolean indexCopiExperience(CopiExperience copiExperience) throws JsonProcessingException {
        log.info("Indexing Copi Experience");
        if (copiExperience.isValid()) {
            String urlHash = DigestUtils.shaHex(copiExperience.getUrl());
            if (!existIndexedCopiByUrlHash(urlHash)) {
                copiExperience.setUrlHash(urlHash);
                String json = mapper.writeValueAsString(copiExperience);
                IndexResponse response = client.prepareIndex("copis", "experience").setSource(json).execute().actionGet();
                log.info("Indexed Copi en {}/{}/{}", response.getIndex(), response.getType(), response.getId());
            }
            return true;
        }
        return false;
    }

    private boolean indexCopiChange(CopiChange copiChange) throws JsonProcessingException {
        log.info("Indexing Copi Experience");
        if (copiChange.isValid()) {
            String urlHash = DigestUtils.shaHex(copiChange.getUrl());
            if (!existIndexedCopiByUrlHash(urlHash)) {
                Ong ong = ongRepository.findOne(copiChange.getOngId());
                copiChange.setOng(ong);
                copiChange.setUrlHash(urlHash);
                String json = mapper.writeValueAsString(copiChange);
                IndexResponse response = client.prepareIndex("copis", "change").setSource(json).execute().actionGet();
                log.info("Indexed Copi en {}/{}/{}", response.getIndex(), response.getType(), response.getId());
            }
            return true;
        }
        return false;
    }

    private boolean existIndexedCopiByUrlHash(String urlHash) {
        SearchResponse response = client.prepareSearch("copis")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("urlHash", urlHash.toLowerCase()))
                .execute().actionGet();
        return ArrayUtils.isNotEmpty(response.getHits().getHits());
    }

}
