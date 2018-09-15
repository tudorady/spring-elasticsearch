package com.simfony.mobility.repository.search;

import com.simfony.mobility.domain.Subscriber;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Subscriber entity.
 */
public interface SubscriberSearchRepository extends ElasticsearchRepository<Subscriber, Long> {
}
