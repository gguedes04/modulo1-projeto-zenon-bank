package br.com.zenon.fraud;

import java.util.Map;

public class TransactionMapRepository {

    public Transaction buscarTransacaoPorNome(Map<String, Transaction> transactions, String nomeCliente) {
        Transaction transaction = transactions.get(nomeCliente);

        if (transaction == null) {
            throw new IllegalArgumentException("Nenhuma transação encontrada para o cliente: " + nomeCliente);
        }

        return transaction;
    }

    public Transaction buscarTransacaoPorNomeMedindoTempo(Map<String, Transaction> transactions, String nomeCliente) {
        long inicio = System.nanoTime();

        Transaction transaction = buscarTransacaoPorNome(transactions, nomeCliente);

        long fim = System.nanoTime();

        System.out.println("Inicio da busca pelo nome do Cliente: " + inicio);
        System.out.println("Fim da busca pelo nome do Cliente: " + fim);
        System.out.println("Tempo Total da busca pelo nome do Cliente: " + (fim - inicio) / 1_000_000.0);

        return transaction;
    }
}
