package com.netculture.netculture.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.netculture.netculture.models.Loja;

public interface RepositoryLoja extends MongoRepository<Loja, ObjectId>{

}
