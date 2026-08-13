package br.com.zenon.fraud;

public record Cliente(
        String name,
        double oldBalance,
        double newBalance
) {
    public enum TipoCliente {
        ORIGEM, DESTINO
    }
}
