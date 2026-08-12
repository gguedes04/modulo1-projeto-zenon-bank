package br.com.zenon.fraud;

public record Transaction(
        int step,
        TransactionType type,
        double amount,
        String clienteOrigem,
        String clienteDestino,
        boolean isFraud,
        boolean isFlaggedFraud
) {
    public enum TransactionType {
        CASH_IN, CASH_OUT, TRANSFER, DEBIT, PAYMENT
    }
}