package com.simfony.mobility.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.simfony.mobility.security.AuthoritiesConstants;
import com.simfony.mobility.security.SecurityUtils;
import com.simfony.mobility.service.ElasticsearchIndexService;
import com.simfony.mobility.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

/**
 * REST controller for managing Elasticsearch index.
 */
@RestController
@RequestMapping("/api")
public class ElasticsearchIndexResource {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexResource.class);

    private final ElasticsearchIndexService elasticsearchIndexService;

    private final ElasticsearchTemplate elasticsearchTemplate;

    public ElasticsearchIndexResource(ElasticsearchIndexService elasticsearchIndexService, ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchIndexService = elasticsearchIndexService;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    /**
     * POST  /elasticsearch/index -> Reindex all Elasticsearch documents
     */
    @PostMapping("/elasticsearch/index")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> reindexAll() throws URISyntaxException {
        log.info("REST request to reindex Elasticsearch by user : {}", SecurityUtils.getCurrentUserLogin());
        elasticsearchIndexService.reindexAll();
        return ResponseEntity.accepted()
            .headers(HeaderUtil.createAlert("elasticsearch.reindex.accepted", null))
            .build();
    }
}
