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

    public Cliente(String name, double oldBalance, double newBalance) {
        this(null, name, BigDecimal.valueOf(oldBalance), BigDecimal.valueOf(newBalance), TipoCliente.ORIGEM);
    }

    public Cliente(String name, BigDecimal oldBalance, BigDecimal newBalance, TipoCliente tipoCliente) {
        this(null, name, oldBalance, newBalance, tipoCliente);
    }

    public Cliente(Long codCliente, String name, double oldBalance, double newBalance, TipoCliente tipoCliente) {
        this(codCliente, name, BigDecimal.valueOf(oldBalance), BigDecimal.valueOf(newBalance), tipoCliente);
    }
}
