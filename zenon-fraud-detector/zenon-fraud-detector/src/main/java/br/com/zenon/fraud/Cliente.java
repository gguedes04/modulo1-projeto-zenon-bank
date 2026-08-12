package br.com.zenon.fraud;

public record Cliente(
        String name,
        double oldbalance,
        double newbalance,
        String tipoCliente
) {
    public enum TipoCliente {
        ORIGEM, DESTINO
    }
}
