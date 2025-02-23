package com.netculture.netculture.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "produtos") 
public class Produto {

    @Id
    private ObjectId id;
    private String nome;
    private String descricao;
    private Double preco;
    private String imagem; // URL da imagem do produto
    private ObjectId vendedorId; 
    private ObjectId lojaId;
    public ObjectId getLojaId() {
        return lojaId;
    }

    public void setLojaId(ObjectId lojaId) {
        this.lojaId = lojaId;
    }

    public Produto() {
    }

    public Produto(String nome, String descricao, Double preco, String imagem, ObjectId vendedorId) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.imagem = imagem;
        this.vendedorId = vendedorId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public ObjectId getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(ObjectId vendedorId) {
        this.vendedorId = vendedorId;
    }
}

