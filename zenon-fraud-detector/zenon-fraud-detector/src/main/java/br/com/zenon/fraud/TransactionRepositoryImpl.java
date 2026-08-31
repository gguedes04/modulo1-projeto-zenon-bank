package br.com.zenon.fraud;

import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {
    @Override
    public List<Transaction> buscarTransacaoPorNome(List<Transaction> transactions, String nomeCliente) {
        return List.of();
    }

    @Override
    public List<Transaction> buscarTransacaoPorNomeMedindoTempo(List<Transaction> transactions, String nomeCliente) {
        return List.of();
    }
}
