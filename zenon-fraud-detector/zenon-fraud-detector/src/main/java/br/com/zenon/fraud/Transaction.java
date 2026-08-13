package br.com.zenon.fraud;
import java.math.BigDecimal;
import java.util.Objects;

public record Transaction(
        int step,
        TransactionType type,
        BigDecimal amount,
        Cliente clienteOrigem,
        Cliente clienteDestino,
        boolean isFraud,
        boolean isFlaggedFraud
) {

    public enum TransactionType {
        CASH_IN, CASH_OUT, TRANSFER, DEBIT, PAYMENT
    }

    public Transaction {

        if (step < 1) {
            throw new IllegalArgumentException("step deve ser maior ou igual a 1");
        }

        if (amount.signum() < 0 ) {
            throw new IllegalArgumentException("amount não pode ser negativo");
        }

        type = Objects.requireNonNull(type, "type não pode ser nulo");
        clienteOrigem = Objects.requireNonNull(clienteOrigem, "clienteOrigem não pode ser nulo");
        clienteDestino = Objects.requireNonNull(clienteDestino, "clienteDestino não pode ser nulo");
    }

}