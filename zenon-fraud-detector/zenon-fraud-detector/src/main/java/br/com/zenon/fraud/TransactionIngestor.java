package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransactionIngestor {

    public List<Transaction> ingestor(String csvFilePath) throws IOException {
        // o IOException na assinatura do metodo lança uma excessao caso dê algum erro
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean firstLine = true;

            int count = 0;

            while ((line = br.readLine()) != null && count <= 1000) {

                if (firstLine) {
                    firstLine = false;
                    continue; // como está dentro do loop irá pular a primeira linha que é o header
                }

                if (line.isBlank()) {
                    continue; // Se alguma linha estiver em branco, irá pular esta linha
                }

                String[] parts = line.split(",", -1);
                // divide o arquivo limitado por virgula
                // e o -1 não permite obter colunas vazias no final da linha

                if (parts.length < 11) {
                    throw new IllegalArgumentException("Linha inválida: " + line);
                }

                Cliente clienteOrigem = new Cliente(parts[3].trim(),Double.parseDouble(parts[4].trim()),Double.parseDouble(parts[5].trim()));

                Cliente clienteDestino = new Cliente(parts[6].trim(),Double.parseDouble(parts[7].trim()),Double.parseDouble(parts[8].trim()));

                Transaction transaction = new Transaction(
                        Integer.parseInt(parts[0].trim()),
                        Transaction.TransactionType.valueOf(parts[1].trim().toUpperCase()),
                        Double.parseDouble(parts[2].trim()),
                        clienteOrigem,
                        clienteDestino,
                        Boolean.parseBoolean(parts[9].trim()),
                        Boolean.parseBoolean(parts[10].trim())
                );

                transactions.add(transaction);

                count++;
            }

        }
        return transactions;
    }
}
