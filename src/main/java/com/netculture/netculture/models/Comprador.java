package com.netculture.netculture.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("comprador")
public class Comprador extends Pessoa{
    public static boolean isNull(Comprador c){
        if( c.getNome() == null ||
            c.getWhatsapp() == null ||
            c.getEmail() == null ||
            c.getSenha() == null){
                return true;
        }
        return false;
    }
}
