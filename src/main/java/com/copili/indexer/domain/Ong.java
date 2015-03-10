package com.copili.indexer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "ongs")
public class Ong extends BaseDocument implements Serializable {

    @JsonProperty("name")
    protected String name;

    @JsonProperty("logo")
    protected byte[] logo;

    @JsonProperty("description")
    protected String description;

    @JsonProperty("country")
    protected String country;

    @JsonProperty("city")
    protected String city;

    @JsonProperty("website")
    protected String website;

    @JsonProperty("email")
    protected String email;

    @JsonProperty("ranking")
    protected Long ranking;

    //----------------------------------------------------------------------
    // Constructors
    //----------------------------------------------------------------------

    public Ong() {
        super();
    }

    //----------------------------------------------------------------------
    // Inner types
    //----------------------------------------------------------------------


    //----------------------------------------------------------------------
    // Getters & Setters
    //----------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getRanking() {
        return ranking;
    }

    public void setRanking(Long ranking) {
        this.ranking = ranking;
    }

}
