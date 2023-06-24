package com.devsuperior.desafio.services.exceptions;

//Observação: Esta outra opção de tratamento de exceção está sendo implementada exclusivamente
//com o objetivo de aplicar o conteúdo estudado no capítulo, haja vista que conforme o solicitado
// neste desafio, não há caso onde o Cliente esteja vinculado a outra entidade.

public class DataBaseException extends RuntimeException {

    public DataBaseException(String msg) {
        super(msg);
    }
}
