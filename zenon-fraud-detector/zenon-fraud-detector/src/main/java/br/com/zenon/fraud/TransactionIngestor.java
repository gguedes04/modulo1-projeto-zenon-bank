package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Double.parseDouble;

public class TransactionIngestor {

    public List<Transaction> ingestor(String csvFilePath, int numerorLinhas) throws IOException {
        // o IOException na assinatura do metodo lança uma excessao caso dê algum erro
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean firstLine = true;

            int count = 0;

            while ((line = br.readLine()) != null && count <= numerorLinhas) {

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

                Cliente clienteOrigem = new Cliente(parts[3].trim(), parseDouble(parts[4].trim()), parseDouble(parts[5].trim()));

                Cliente clienteDestino = new Cliente(parts[6].trim(), parseDouble(parts[7].trim()), parseDouble(parts[8].trim()));

                boolean isFraud = parts[9].trim().equals("1");
                boolean isFlaggedFraud = parts[10].trim().equals("1");

                Transaction transaction = new Transaction(
                        Integer.parseInt(parts[0].trim()),
                        Transaction.TransactionType.valueOf(parts[1].trim().toUpperCase()),
                        new BigDecimal(parts[2].trim()),
                        clienteOrigem,
                        clienteDestino,
                        isFraud,
                        isFlaggedFraud
                );

                transactions.add(transaction);

                count++;
            }

        }
        return transactions;
    }

    public List<Transaction> ingestorBadData(String csvFilePath)  {
        // o IOException na assinatura do metodo lança uma excessao caso dê algum erro
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean firstLine = true;

            int count = 0;

            while ((line = br.readLine()) != null) {

                if (firstLine) {
                    firstLine = false;
                    count++;
                    continue; // como está dentro do loop irá pular a primeira linha que é o header
                }

                if (line.isBlank()) {
                    count++;
                    continue; // Se alguma linha estiver em branco, irá pular esta linha
                }

                validaLinha(line).ifPresent(transactions::add);

                count++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }


    public Optional<Transaction> validaLinha (String line) {

        try {

            String[] parts = line.split(",", -1);
            // divide o arquivo limitado por virgula
            // e o -1 não permite obter colunas vazias no final da linha

            // step,type,amount,nameOrig,oldbalanceOrg,newbalanceOrig,nameDest,oldbalanceDest,newbalanceDest,isFraud,isFlaggedFraud


            if (parts[3].trim().isEmpty()) {
                throw new IllegalArgumentException("nameOrig do Cliente Origem, não pode ser vazio.");
            }
            String nameOrig = parts[3].trim();


            if (parts[4].trim() == null || Double.parseDouble(parts[4].trim()) < 0) {
                throw new IllegalArgumentException("oldBalanceOrg do Cliente Origem, deve ser positivo.");
            }
            Double oldBalanceOrg = Double.parseDouble(parts[4].trim());

            Double newBalanceOrig = Double.parseDouble(parts[5].trim());
            if (parts[5].trim() == null || Double.parseDouble(parts[5].trim()) < 0) {
                throw new IllegalArgumentException("newBalanceOrig do Cliente Origem, deve ser positivo.");
            }

            if (parts[6].trim().isEmpty()) {
                throw new IllegalArgumentException("nameDest do Cliente Destino, não pode ser vazio.");
            }
            String nameDest = parts[6].trim();

            if (parts[7].trim() == null || Double.parseDouble(parts[7].trim()) < 0) {
                throw new IllegalArgumentException("oldBalanceDest do Cliente Destino, deve ser positivo.");
            }
            Double oldBalanceDest = Double.parseDouble(parts[7].trim());

            if (parts[8].trim() == null || Double.parseDouble(parts[8].trim()) < 0) {
                throw new IllegalArgumentException("newBalanceDest do Cliente Destino, deve ser positivo.");
            }
            Double newBalanceDest = Double.parseDouble(parts[8].trim());

            if (parts[0].trim().isEmpty() || Integer.parseInt(parts[0].trim()) < 1) {
                throw new IllegalArgumentException("step, deve ser maior ou igual a 1.");
            }
            int step = Integer.parseInt(parts[0].trim());

            String typeValue = parts[1].trim();
            if (parts[1].trim().isEmpty()) {
                throw new IllegalArgumentException("type deve ser preenchido com valor válido.");
            }
            Transaction.TransactionType type;
            try {
                type = Transaction.TransactionType.valueOf(typeValue.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("type inválido: " + typeValue);
            }
            // Transaction.TransactionType type = Transaction.TransactionType.valueOf(parts[1].trim().toUpperCase());

            String amountVal = parts[2].trim();

            if (amountVal.isEmpty()) {
                throw new IllegalArgumentException("amount deve ser preenchido.");
            }
            BigDecimal amount = new BigDecimal(parts[2].trim());
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("amount não pode ser menor que zero.");
            }

            if (parts[9].trim().isEmpty()) {
                throw new IllegalArgumentException("isFraud não pode ser vazio.");
            }
            Boolean isFraud = Boolean.parseBoolean(parts[9].trim());

            if (parts[9].trim().isEmpty()) {
                throw new IllegalArgumentException("isFlaggedFraud não pode ser vazio.");
            }
            Boolean isFlaggedFraud = Boolean.parseBoolean(parts[10].trim());

            Cliente clienteOrigem = new Cliente(parts[3].trim(),Double.parseDouble(parts[4].trim()),Double.parseDouble(parts[5].trim()));
            Cliente clienteDestino = new Cliente(parts[6].trim(),Double.parseDouble(parts[7].trim()),Double.parseDouble(parts[8].trim()));

            Transaction transaction = new Transaction(
                    Integer.parseInt(parts[0].trim()),
                    Transaction.TransactionType.valueOf(parts[1].trim().toUpperCase()),
                    new BigDecimal(parts[2].trim()),
                    clienteOrigem,
                    clienteDestino,
                    Boolean.parseBoolean(parts[9].trim()),
                    Boolean.parseBoolean(parts[10].trim())
            );

            return Optional.of(new Transaction(step, type, amount, clienteOrigem, clienteDestino, isFraud, isFlaggedFraud));

        } catch (Exception e) {
            IO.println("Erro: " + line + " | " + e);
            return Optional.empty();
        }

    }



}
