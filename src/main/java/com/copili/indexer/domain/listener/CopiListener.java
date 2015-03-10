package com.copili.indexer.domain.listener;

import com.copili.indexer.domain.Copi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PrePersist;

public class CopiListener {

    private final static Logger log = LoggerFactory.getLogger(CopiListener.class);

    @PrePersist
    protected void prePersist(Copi copi) {
        log.info("@PrePersist : {}", copi.getClass());
        copi.setIndexed(false);
    }

}
