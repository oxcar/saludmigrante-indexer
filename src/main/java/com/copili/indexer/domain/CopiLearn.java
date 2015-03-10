package com.copili.indexer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "copis")
public class CopiLearn extends Copi implements Serializable {

    @JsonProperty("title")
    private String title;

    @JsonProperty("authors")
    private List<String> authors;

    @JsonProperty("journal")
    private String journal;

    @JsonProperty("volume")
    private String volume;

    @JsonProperty("issue")
    private String issue;

    @JsonProperty("pages")
    private Long pages;

    @JsonProperty("year")
    private Long year;

    @JsonProperty("doi")
    private String doi;

    //----------------------------------------------------------------------
    // Constructors
    //----------------------------------------------------------------------
    public CopiLearn() {
        super();
        this.type = Type.LEARN;
    }

    //----------------------------------------------------------------------
    // Bussiness logic
    //----------------------------------------------------------------------

    @Override
    public boolean isValid() {
        boolean ok = super.isValid();
        ok = ok && (type.equals(Type.LEARN));
        ok = ok && StringUtils.isNotBlank(title);
        ok = ok && CollectionUtils.isNotEmpty(authors);
        ok = ok && StringUtils.isNotBlank(doi);
        return ok;
    }

    //----------------------------------------------------------------------
    // Getters & Setters
    //----------------------------------------------------------------------


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        if(StringUtils.isBlank(authors)) {
            return;
        }
        String[] authorsArray = StringUtils.split(authors, ",");
        this.authors = Arrays.asList(authorsArray);
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

}
