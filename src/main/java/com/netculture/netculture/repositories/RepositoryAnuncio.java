package com.netculture.netculture.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.netculture.netculture.models.Anuncio;

public interface RepositoryAnuncio extends MongoRepository<Anuncio, ObjectId>{

}
