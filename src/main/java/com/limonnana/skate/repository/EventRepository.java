package com.limonnana.skate.repository;

import com.limonnana.skate.domain.Event;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Event entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    public Iterable<Event> findByActiveTrue();


}
