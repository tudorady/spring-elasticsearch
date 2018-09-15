package com.simfony.mobility.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.simfony.mobility.service.SubscriberService;
import com.simfony.mobility.web.rest.errors.BadRequestAlertException;
import com.simfony.mobility.web.rest.util.HeaderUtil;
import com.simfony.mobility.web.rest.util.PaginationUtil;
import com.simfony.mobility.service.dto.SubscriberDTO;
import com.simfony.mobility.service.dto.SubscriberCriteria;
import com.simfony.mobility.service.SubscriberQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Subscriber.
 */
@RestController
@RequestMapping("/api")
public class SubscriberResource {

    private final Logger log = LoggerFactory.getLogger(SubscriberResource.class);

    private static final String ENTITY_NAME = "subscriber";

    private final SubscriberService subscriberService;

    private final SubscriberQueryService subscriberQueryService;

    public SubscriberResource(SubscriberService subscriberService, SubscriberQueryService subscriberQueryService) {
        this.subscriberService = subscriberService;
        this.subscriberQueryService = subscriberQueryService;
    }

    /**
     * POST  /subscribers : Create a new subscriber.
     *
     * @param subscriberDTO the subscriberDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subscriberDTO, or with status 400 (Bad Request) if the subscriber has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/subscribers")
    @Timed
    public ResponseEntity<SubscriberDTO> createSubscriber(@Valid @RequestBody SubscriberDTO subscriberDTO) throws URISyntaxException {
        log.debug("REST request to save Subscriber : {}", subscriberDTO);
        if (subscriberDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriber cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubscriberDTO result = subscriberService.save(subscriberDTO);
        return ResponseEntity.created(new URI("/api/subscribers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subscribers : Updates an existing subscriber.
     *
     * @param subscriberDTO the subscriberDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subscriberDTO,
     * or with status 400 (Bad Request) if the subscriberDTO is not valid,
     * or with status 500 (Internal Server Error) if the subscriberDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/subscribers")
    @Timed
    public ResponseEntity<SubscriberDTO> updateSubscriber(@Valid @RequestBody SubscriberDTO subscriberDTO) throws URISyntaxException {
        log.debug("REST request to update Subscriber : {}", subscriberDTO);
        if (subscriberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubscriberDTO result = subscriberService.save(subscriberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subscriberDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subscribers : get all the subscribers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of subscribers in body
     */
    @GetMapping("/subscribers")
    @Timed
    public ResponseEntity<List<SubscriberDTO>> getAllSubscribers(SubscriberCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Subscribers by criteria: {}", criteria);
        Page<SubscriberDTO> page = subscriberQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/subscribers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /subscribers/:id : get the "id" subscriber.
     *
     * @param id the id of the subscriberDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subscriberDTO, or with status 404 (Not Found)
     */
    @GetMapping("/subscribers/{id}")
    @Timed
    public ResponseEntity<SubscriberDTO> getSubscriber(@PathVariable Long id) {
        log.debug("REST request to get Subscriber : {}", id);
        Optional<SubscriberDTO> subscriberDTO = subscriberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriberDTO);
    }

    /**
     * DELETE  /subscribers/:id : delete the "id" subscriber.
     *
     * @param id the id of the subscriberDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/subscribers/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubscriber(@PathVariable Long id) {
        log.debug("REST request to delete Subscriber : {}", id);
        subscriberService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/subscribers?query=:query : search for the subscriber corresponding
     * to the query.
     *
     * @param query the query of the subscriber search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/subscribers")
    @Timed
    public ResponseEntity<List<SubscriberDTO>> searchSubscribers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Subscribers for query {}", query);
        Page<SubscriberDTO> page = subscriberService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/subscribers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
