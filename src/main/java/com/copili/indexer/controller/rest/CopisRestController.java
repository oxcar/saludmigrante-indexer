package com.copili.indexer.controller.rest;

import com.copili.indexer.domain.CopiExperience;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Controller
@RequestMapping(value = "/api/copis")
public class CopisRestController extends BaseRestController {

    private final static Logger log = LoggerFactory.getLogger(CopisRestController.class);

    private Node node;

    private Client client;

    private ObjectMapper mapper = new ObjectMapper();

    public CopisRestController() {
        node = nodeBuilder().client(true).node();
        client = node.client();
    }

    @PreDestroy
    public void preDestroy() {
        node.close();
    }

    @RequestMapping(value = "experience", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getExperienceCopis() {
        SearchResponse response = client.prepareSearch("copis")
                .setTypes("experience")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setFrom(0).setSize(10).setExplain(true)
                .execute().actionGet();
        List<CopiExperience> copis = extractCopisFromResponse(response);
        return new ResponseEntity<>(ApiResponse.ok(copis), HttpStatus.OK);
    }

    private List<CopiExperience> extractCopisFromResponse(SearchResponse response) {
        List<CopiExperience> copis = new ArrayList<>();
        for (SearchHit searchHit : response.getHits().getHits()) {
            try {
                String json = searchHit.getSourceAsString();
                CopiExperience copiExperience = mapper.readValue(json, CopiExperience.class);
                copis.add(copiExperience);
            } catch (JsonMappingException e) {
                log.error("Error al extraer Copi: {}", e.getMessage());
            } catch (IOException e) {
                log.error("Error al extraer Copi: {}", e.getMessage());
            }
        }
        return copis;
    }

}
