package com.netculture.netculture.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("vendedor")
public class Vendedor extends Pessoa{
    private String descricao;
    private List<Loja> lojas = new ArrayList<>();

    public static boolean isNull(Vendedor v){
        return v.getDescricao() == null ||
               v.getNome() == null ||
               v.getWhatsapp() == null ||
               v.getEmail() == null ||
               v.getSenha() == null;
    }
}
