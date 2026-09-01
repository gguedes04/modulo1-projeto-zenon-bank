package br.com.zenon.fraud;

import java.util.List;

public interface TransactionRepository {

    List<Transaction> buscarTransacaoPorNome(List<Transaction> transactions, String nomeCliente);

    List<Transaction> buscarTransacaoPorNomeMedindoTempo(List<Transaction> transactions, String nomeCliente);

    Transaction save(Transaction transaction);
}
