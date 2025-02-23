package com.netculture.netculture.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.netculture.netculture.models.Loja;

public interface RepositoryLoja extends MongoRepository<Loja, ObjectId>{
    List<Loja> findByVendedorId(ObjectId vendedorId);
}
