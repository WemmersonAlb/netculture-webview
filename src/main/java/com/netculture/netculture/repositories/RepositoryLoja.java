package com.netculture.netculture.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import com.netculture.netculture.models.Loja;
import com.netculture.netculture.models.Produto;

public interface RepositoryLoja extends MongoRepository<Loja, ObjectId>{
    @Query(value = "{ 'vendedorId': ?0 }")
    List<Loja> findByVendedorId(ObjectId vendedorId);

    @Query("{ '_id': ?0 }")
    @Update("{ '$push': { 'produtos': ?1 } }")
    void adicionarItem(ObjectId lojaId, ObjectId produtoId);
}
