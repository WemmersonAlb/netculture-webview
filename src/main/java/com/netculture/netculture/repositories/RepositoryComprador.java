package com.netculture.netculture.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.netculture.netculture.models.Comprador;

public interface RepositoryComprador extends MongoRepository<Comprador, ObjectId>{

    @Query(value = "{ 'email': ?0 }")
    Comprador findByEmail(String email);
}
