package com.limonnana.skate.repository;

import com.limonnana.skate.domain.Spot;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Spot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpotRepository extends MongoRepository<Spot, String> {
}
