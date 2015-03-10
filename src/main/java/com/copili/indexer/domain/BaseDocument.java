package com.copili.indexer.domain;

import com.copili.indexer.domain.listener.BaseDocumentListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EntityListeners;

@Document
@EntityListeners(BaseDocumentListener.class)
public class BaseDocument {

    @Id
    protected String id;

    @JsonIgnore
    @Version
    protected Long version;

    @JsonIgnore
    @CreatedDate
    protected DateTime created;

    @JsonIgnore
    @LastModifiedDate
    protected DateTime updated;

    //----------------------------------------------------------------------
    // Getters & Setters
    //----------------------------------------------------------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public DateTime getUpdated() {
        return updated;
    }

    public void setUpdated(DateTime updated) {
        this.updated = updated;
    }
}
