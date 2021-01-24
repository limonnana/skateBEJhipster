package com.limonnana.skate.repository;

import com.limonnana.skate.domain.Trick;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Trick entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrickRepository extends MongoRepository<Trick, String> {
}
