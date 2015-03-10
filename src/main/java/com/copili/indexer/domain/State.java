package com.copili.indexer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "states")
public class State implements Serializable {

    @Id
    @JsonProperty("_id")
    protected String id;

    @JsonProperty("state")
    private String state;

    @JsonProperty("code")
    private String code;

    //----------------------------------------------------------------------
    // Constructors
    //----------------------------------------------------------------------

    public State() {
        super();
    }

    public State(String state, String code) {
        this.state = state;
        this.code = code;
    }

    //----------------------------------------------------------------------
    // Getters & Setters
    //----------------------------------------------------------------------

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
