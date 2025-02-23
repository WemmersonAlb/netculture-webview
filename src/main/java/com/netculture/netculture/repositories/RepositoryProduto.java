package com.netculture.netculture.repositories;

import com.netculture.netculture.models.Produto;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RepositoryProduto extends MongoRepository<Produto, ObjectId> {
    List<Produto> findByVendedorId(ObjectId vendedorId);
}


