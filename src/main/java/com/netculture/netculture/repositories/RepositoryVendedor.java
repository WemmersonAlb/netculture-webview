package com.netculture.netculture.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.netculture.netculture.models.Vendedor;


public interface RepositoryVendedor extends MongoRepository<Vendedor, ObjectId>{

    @Query(value = "{ 'email': ?0 }")
    Vendedor findByEmail(String email);
}
