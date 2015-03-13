package com.copili.indexer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "copis")
public class CopiExperience extends Copi implements Serializable {

    @JsonProperty("published_date")
    private Date publishedDate;

    @JsonProperty("source")
    private String source;

    @JsonProperty("thumbnail")
    private String thumbnail;

    //----------------------------------------------------------------------
    // Constructors
    //----------------------------------------------------------------------

    public CopiExperience() {
        super();
        this.type = Type.EXPERIENCE;
    }

    //----------------------------------------------------------------------
    // Bussiness logic
    //----------------------------------------------------------------------

    @Override
    public boolean isValid() {
        boolean ok = super.isValid();
        ok = ok && StringUtils.isNotBlank(url);
        ok = ok && (type.equals(Type.EXPERIENCE));
        ok = ok && null != publishedDate;
        ok = ok && StringUtils.isNotBlank(source);
        return ok;
    }

    //----------------------------------------------------------------------
    // Getters & Setters
    //----------------------------------------------------------------------

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
