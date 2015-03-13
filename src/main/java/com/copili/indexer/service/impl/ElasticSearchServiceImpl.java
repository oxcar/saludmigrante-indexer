package com.copili.indexer.service.impl;

import com.copili.indexer.domain.CopiChange;
import com.copili.indexer.domain.CopiExperience;
import com.copili.indexer.domain.CopiLearn;
import com.copili.indexer.service.ElasticSearchService;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Component("elasticSearchService")
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private final static Logger log = LoggerFactory.getLogger(ElasticSearchService.class);

    private Node node;

    private Client client;

    private ObjectMapper mapper = new ObjectMapper();

    //----------------------------------------------------------------------
    // Constructor & Destructor
    //----------------------------------------------------------------------

    public ElasticSearchServiceImpl() {
        super();
        node = nodeBuilder().client(true).node();
        client = node.client();
    }

    @PreDestroy
    public void preDestroy() {
        node.close();
    }

    //----------------------------------------------------------------------
    // Public methods
    //----------------------------------------------------------------------

    @Override
    public List<CopiExperience> findExperienceCopis(String query, String state, Integer from, Integer size) {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("copis")
                .setTypes("experience")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setFrom(from)
                .setSize(size)
                .setExplain(true);

        if(StringUtils.isBlank(state)) {
            return new ArrayList<>();
        }

        if (StringUtils.isNotBlank(query)) {
            QueryBuilder qb = QueryBuilders.boolQuery()
                    .should(wildcardQuery("text", "*" + query.toLowerCase().trim() + "*"))
                    .should(wildcardQuery("keywords", "*" + query.toLowerCase().trim() + "*"))
                    .should(termQuery("state", state));
            searchRequestBuilder.setQuery(qb);
        } else {
            QueryBuilder qb = QueryBuilders.termQuery("state", state);
            searchRequestBuilder.setQuery(qb);
        }
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        return extractCopisFromResponse(response, CopiExperience.class);
    }

    @Override
    public List<CopiLearn> findLearnCopis(String id, Integer from, Integer size) {
        CopiExperience copiExperience = findCopiExperienceById(id);
        if(null == copiExperience) {
            return new ArrayList<>();
        }
        List<String> keywords = copiExperience.getKeywords();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for(String keyword : keywords) {
            boolQueryBuilder.should(wildcardQuery("keywords", "*" + keyword.toLowerCase().trim() + "*"));
        }
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("copis")
                .setTypes("learn")
                .setQuery(boolQueryBuilder)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setFrom(from)
                .setSize(size)
                // .setMinScore(0f)
                .setExplain(true);
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        return extractCopisFromResponse(response, CopiLearn.class);
    }

    @Override
    public List<CopiChange> findChangeCopis(String id, Integer from, Integer size) {
        CopiExperience copiExperience = findCopiExperienceById(id);
        if(null == copiExperience) {
            return new ArrayList<>();
        }
        List<String> keywords = copiExperience.getKeywords();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for(String keyword : keywords) {
            boolQueryBuilder.should(wildcardQuery("keywords", "*" + keyword.toLowerCase().trim() + "*"));
        }
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("copis")
                .setTypes("change")
                .setQuery(boolQueryBuilder)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setFrom(from)
                .setSize(size)
                // .setMinScore(0f)
                .setExplain(true);
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        return extractCopisFromResponse(response, CopiChange.class);
    }

    //----------------------------------------------------------------------
    // Private methods
    //----------------------------------------------------------------------

    private CopiExperience findCopiExperienceById(String id) {
        if(StringUtils.isBlank(id)) {
            return null;
        }
        SearchResponse response = client.prepareSearch("copis")
                .setTypes("experience")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("id", id))
                .execute().actionGet();
        if(response.getHits().getHits().length == 1) {
            try {
                String json = response.getHits().getAt(0).getSourceAsString();
                return mapper.readValue(json, CopiExperience.class);
            } catch (JsonMappingException e) {
                log.error("Error al extraer Copi: {}", e.getMessage());
            } catch (IOException e) {
                log.error("Error al extraer Copi: {}", e.getMessage());
            }
        }
        return null;
    }

    private <T> List<T> extractCopisFromResponse(SearchResponse response, Class<T> clazz) {
        List<T> copis = new ArrayList<>();
        for (SearchHit searchHit : response.getHits().getHits()) {
            try {
                String json = searchHit.getSourceAsString();
                T copi = mapper.readValue(json, clazz);
                copis.add(copi);
            } catch (JsonMappingException e) {
                log.error("Error al extraer Copi: {}", e.getMessage());
            } catch (IOException e) {
                log.error("Error al extraer Copi: {}", e.getMessage());
            }
        }
        return copis;
    }

}
