package com.copili.indexer.controller.rest;

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
import org.elasticsearch.index.query.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Controller
@RequestMapping(value = "/api/copis")
public class CopisRestController extends BaseRestController {

    private final static Logger log = LoggerFactory.getLogger(CopisRestController.class);

    @Autowired
    private ElasticSearchService elasticSearchService;

    @RequestMapping(value = "experience", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getExperienceCopis(
            @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(value = "query", defaultValue = "", required = false) String query,
            @RequestParam(value = "state", defaultValue = "US", required = false) String state) {
        if(!isValidSearch(state, from, size)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        List<CopiExperience> copis = elasticSearchService.findExperienceCopis(query, state, from, size);
        return new ResponseEntity<>(ApiResponse.ok(copis), HttpStatus.OK);

    }

    @RequestMapping(value = "experience/{id}/learn", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getLearnCopis(
            @PathVariable(value = "id") String id,
            @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {

        if(!isValidSearch(from, size)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        List<CopiLearn> copis = elasticSearchService.findLearnCopis(id, from, size);
        return new ResponseEntity<>(ApiResponse.ok(copis),HttpStatus.OK);

    }

    @RequestMapping(value = "experience/{id}/change", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getChangeCopis(
            @PathVariable(value = "id") String id,
            @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {

        if(!isValidSearch(from, size)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        List<CopiChange> copis = elasticSearchService.findChangeCopis(id, from, size);
        return new ResponseEntity<>(ApiResponse.ok(copis),HttpStatus.OK);
    }

    //----------------------------------------------------------------------
    // Validations
    //----------------------------------------------------------------------

    private boolean isValidSearch(Integer from, Integer size) {
        return isValidSearch("US", from, size);
    }

    private boolean isValidSearch(String state, Integer from, Integer size) {
        if(StringUtils.isBlank(state) || 2 != state.length()) {
            return false;
        }
        if(null == from || from.compareTo(0) < 0) {
            return false;
        }
        if(null == size || size.compareTo(0) < 1) {
            return false;
        }
        return true;
    }


}
