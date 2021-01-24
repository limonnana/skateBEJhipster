package com.limonnana.skate.repository;

import com.limonnana.skate.domain.Fan;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Fan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FanRepository extends MongoRepository<Fan, String> {
}
