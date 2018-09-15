package com.simfony.mobility.repository;

import com.simfony.mobility.domain.Subscriber;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Subscriber entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long>, JpaSpecificationExecutor<Subscriber> {

}
