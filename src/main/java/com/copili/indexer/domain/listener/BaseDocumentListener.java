package com.copili.indexer.domain.listener;

import com.copili.indexer.domain.BaseDocument;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class BaseDocumentListener {

    private final static Logger log = LoggerFactory.getLogger(BaseDocumentListener.class);

    @PrePersist
    protected void prePersist(BaseDocument baseDocument) {
        log.info("@PrePersist : {}", baseDocument.getClass());
        baseDocument.setCreated(new DateTime());
        baseDocument.setUpdated(new DateTime());
    }

    @PreUpdate
    protected void preUpdate(BaseDocument baseDocument) {
        baseDocument.setUpdated(new DateTime());
    }

}
