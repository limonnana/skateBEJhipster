package com.limonnana.skate.repository;

import com.limonnana.skate.domain.Player;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Player entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {
}
