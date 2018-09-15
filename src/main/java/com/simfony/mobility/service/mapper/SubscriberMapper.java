package com.simfony.mobility.service.mapper;

import com.simfony.mobility.domain.*;
import com.simfony.mobility.service.dto.SubscriberDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Subscriber and its DTO SubscriberDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriberMapper extends EntityMapper<SubscriberDTO, Subscriber> {



    default Subscriber fromId(Long id) {
        if (id == null) {
            return null;
        }
        Subscriber subscriber = new Subscriber();
        subscriber.setId(id);
        return subscriber;
    }
}
