package com.limonnana.skate.repository;


import com.limonnana.skate.domain.Seccion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeccionRepository extends MongoRepository<Seccion, String> {
}
