package br.com.zenon;

import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionIngestor;
import br.com.zenon.fraud.TransactionSqlRepository;

import java.io.IOException;
import java.util.List;

public class DBMain {

    private static final String CSV_FILE = "src/data/PS_20174392719_1491204439457_log.csv";
    private static final String JDBC_URL =
            "jdbc:mysql://localhost:3306/zenon_fraud_detector?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";
    private static final int LIMIT = 10000;

    public static void main(String[] args) {
        TransactionIngestor ingestor = new TransactionIngestor();

        List<Transaction> transactions;
        try {
            transactions = ingestor.ingestor(CSV_FILE, LIMIT);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler CSV do PaySim", e);
        }

        long inicioInsercao = System.nanoTime();

        try (TransactionSqlRepository repository = new TransactionSqlRepository(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            for (Transaction transaction : transactions) {
                repository.save(transaction);
            }

            long fimInsercao = System.nanoTime();

            System.out.println("Tempo total de inserção: " + (fimInsercao - inicioInsercao) / 1_000_000.0 + " ms");

            Transaction encontrada = repository.findByOriginName("C1231006815");
            System.out.println("Transação existente encontrada: " + encontrada);

            try {
                Transaction inexistente = repository.findByOriginName("C12345");
                System.out.println("Transação inexistente encontrada: " + inexistente);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
