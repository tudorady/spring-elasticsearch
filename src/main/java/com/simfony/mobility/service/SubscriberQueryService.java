package com.simfony.mobility.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.simfony.mobility.domain.Subscriber;
import com.simfony.mobility.domain.*; // for static metamodels
import com.simfony.mobility.repository.SubscriberRepository;
import com.simfony.mobility.repository.search.SubscriberSearchRepository;
import com.simfony.mobility.service.dto.SubscriberCriteria;

import com.simfony.mobility.service.dto.SubscriberDTO;
import com.simfony.mobility.service.mapper.SubscriberMapper;

/**
 * Service for executing complex queries for Subscriber entities in the database.
 * The main input is a {@link SubscriberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubscriberDTO} or a {@link Page} of {@link SubscriberDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubscriberQueryService extends QueryService<Subscriber> {

    private final Logger log = LoggerFactory.getLogger(SubscriberQueryService.class);

    private final SubscriberRepository subscriberRepository;

    private final SubscriberMapper subscriberMapper;

    private final SubscriberSearchRepository subscriberSearchRepository;

    public SubscriberQueryService(SubscriberRepository subscriberRepository, SubscriberMapper subscriberMapper, SubscriberSearchRepository subscriberSearchRepository) {
        this.subscriberRepository = subscriberRepository;
        this.subscriberMapper = subscriberMapper;
        this.subscriberSearchRepository = subscriberSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SubscriberDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubscriberDTO> findByCriteria(SubscriberCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Subscriber> specification = createSpecification(criteria);
        return subscriberMapper.toDto(subscriberRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SubscriberDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubscriberDTO> findByCriteria(SubscriberCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Subscriber> specification = createSpecification(criteria);
        return subscriberRepository.findAll(specification, page)
            .map(subscriberMapper::toDto);
    }

    /**
     * Function to convert SubscriberCriteria to a {@link Specification}
     */
    private Specification<Subscriber> createSpecification(SubscriberCriteria criteria) {
        Specification<Subscriber> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Subscriber_.id));
            }
            if (criteria.getImsi() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImsi(), Subscriber_.imsi));
            }
            if (criteria.getMsisdn() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMsisdn(), Subscriber_.msisdn));
            }
            if (criteria.getNam() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNam(), Subscriber_.nam));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getState(), Subscriber_.state));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCategory(), Subscriber_.category));
            }
            if (criteria.getSubscriberChargingCharacteristics() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubscriberChargingCharacteristics(), Subscriber_.subscriberChargingCharacteristics));
            }
            if (criteria.getIccid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIccid(), Subscriber_.iccid));
            }
        }
        return specification;
    }

}
