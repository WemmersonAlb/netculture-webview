package com.netculture.netculture.models;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("loja")
public class Loja {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String titulo;
    private String descricao;
    private Map<String, String> imagens;//key: link da imagem no imgur, value: deleteHash
    private String whatsapp;
    private Vendedor vendedor;
    private List<Item> itens;
}
