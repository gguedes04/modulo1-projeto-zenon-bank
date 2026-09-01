package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Cliente(
        Long codCliente,
        String name,
        BigDecimal oldBalance,
        BigDecimal newBalance,
        TipoCliente tipoCliente
) {
    public enum TipoCliente {
        ORIGEM, DESTINO
    }

    //Colocamos 3 construtores pelo seguinte:
    /* 1. public Cliente(String name, double oldBalance, double newBalance)
    É o atalho mais simples.
    Ele serve para quando você só tem os dados básicos e não quer informar codCliente nem tipoCliente.
    Internamente ele assume:
            ◦
    codCliente = null
            ◦
    tipoCliente = ORIGEM */

    public Cliente(String name, double oldBalance, double newBalance) {
        this(null, name, BigDecimal.valueOf(oldBalance), BigDecimal.valueOf(newBalance), TipoCliente.ORIGEM);
    }

    /*2.public Cliente(String name, BigDecimal oldBalance, BigDecimal newBalance, TipoCliente tipoCliente)
    Esse construtor já recebe os valore como BigDecimal.
    Ele serve para qdo está trabalhando com valores monetários sem precisar converter para double.
    Assume o codCliente = null*/

    public Cliente(String name, BigDecimal oldBalance, BigDecimal newBalance, TipoCliente tipoCliente) {
        this(null, name, oldBalance, newBalance, tipoCliente);
    }

    /*3.public Cliente(Long codCliente, String name, double oldBalance, double newBalance, TipoCliente tipoCliente)
    Serve quando você já tem o ID do cliente vindo do banco
    Converte double para BigDecimal.*/

    public Cliente(Long codCliente, String name, double oldBalance, double newBalance, TipoCliente tipoCliente) {
        this(codCliente, name, BigDecimal.valueOf(oldBalance), BigDecimal.valueOf(newBalance), tipoCliente);
    }
}
