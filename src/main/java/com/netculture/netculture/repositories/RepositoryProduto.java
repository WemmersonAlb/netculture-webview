package com.netculture.netculture.repositories;

import com.netculture.netculture.models.Produto;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RepositoryProduto extends MongoRepository<Produto, ObjectId> {
    @Query(value = "{ 'vendedorId': ?0 }")
    List<Produto> findByVendedorId(ObjectId vendedorId);
    @Query(value = "{ 'lojaId': ?0 }")
    List<Produto> findByLojaId(ObjectId lojaId);
}


