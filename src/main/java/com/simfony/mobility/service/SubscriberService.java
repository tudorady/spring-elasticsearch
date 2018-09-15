package com.simfony.mobility.service;

import com.simfony.mobility.domain.Subscriber;
import com.simfony.mobility.repository.SubscriberRepository;
import com.simfony.mobility.repository.search.SubscriberSearchRepository;
import com.simfony.mobility.service.dto.SubscriberDTO;
import com.simfony.mobility.service.mapper.SubscriberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Subscriber.
 */
@Service
@Transactional
public class SubscriberService {

    private final Logger log = LoggerFactory.getLogger(SubscriberService.class);

    private final SubscriberRepository subscriberRepository;

    private final SubscriberMapper subscriberMapper;

    private final SubscriberSearchRepository subscriberSearchRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SubscriberMapper subscriberMapper, SubscriberSearchRepository subscriberSearchRepository) {
        this.subscriberRepository = subscriberRepository;
        this.subscriberMapper = subscriberMapper;
        this.subscriberSearchRepository = subscriberSearchRepository;
    }

    /**
     * Save a subscriber.
     *
     * @param subscriberDTO the entity to save
     * @return the persisted entity
     */
    public SubscriberDTO save(SubscriberDTO subscriberDTO) {
        log.debug("Request to save Subscriber : {}", subscriberDTO);
        Subscriber subscriber = subscriberMapper.toEntity(subscriberDTO);
        subscriber = subscriberRepository.save(subscriber);
        SubscriberDTO result = subscriberMapper.toDto(subscriber);
        subscriberSearchRepository.save(subscriber);
        return result;
    }

    /**
     * Get all the subscribers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SubscriberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Subscribers");
        return subscriberRepository.findAll(pageable)
            .map(subscriberMapper::toDto);
    }


    /**
     * Get one subscriber by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SubscriberDTO> findOne(Long id) {
        log.debug("Request to get Subscriber : {}", id);
        return subscriberRepository.findById(id)
            .map(subscriberMapper::toDto);
    }

    /**
     * Delete the subscriber by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Subscriber : {}", id);
        subscriberRepository.deleteById(id);
        subscriberSearchRepository.deleteById(id);
    }

    /**
     * Search for the subscriber corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SubscriberDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Subscribers for query {}", query);
        return subscriberSearchRepository.search(queryStringQuery(query), pageable)
            .map(subscriberMapper::toDto);
    }
}
