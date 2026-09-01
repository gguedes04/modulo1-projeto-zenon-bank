package br.com.zenon;
import br.com.zenon.fraud.*;
import br.com.zenon.fraud.Cliente.TipoCliente;
import br.com.zenon.fraud.Transaction.TransactionType;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        List<Cliente> clientes = List.of(
                new Cliente(null, "C1231006815", 170136.0, 160296.36, TipoCliente.ORIGEM),
                new Cliente(null, "M1979787155", 0.0, 0.0, TipoCliente.DESTINO),
                new Cliente(null, "C1280323807", 850002.52, 0.0, TipoCliente.ORIGEM),
                new Cliente(null, "C873221189", 6510099.11, 7360101.63, TipoCliente.DESTINO)
        );

        List<Transaction> transactions = List.of(
                new Transaction(null, 1, TransactionType.PAYMENT, new BigDecimal("9839.64"),
                        clientes.stream()
                                .filter(cliente -> cliente.name().equals("C1231006815"))
                                .findFirst()
                                .orElse(null),
                        clientes.stream()
                                .filter(cliente -> cliente.name().equals("C1231006815"))
                                .findFirst()
                                .orElse(null), false, false),
                new Transaction(null, 743, TransactionType.CASH_OUT, new BigDecimal("850002.52"),
                        clientes.stream()
                                .filter(cliente -> cliente.name().equals("C1280323807"))
                                .findFirst()
                                .orElse(null),
                        clientes.stream()
                                .filter(cliente -> cliente.name().equals("C873221189"))
                                .findFirst()
                                .orElse(null), true, false)
        );

        for (Transaction transaction : transactions) {
/*
            Cliente clienteOrigem = clientes.stream()
                    .filter(cliente -> cliente.name().equals(transaction.clienteOrigem()))
                    .findFirst()
                    .orElse(null);

            Cliente clienteDestino = clientes.stream()
                    .filter(cliente -> cliente.name().equals(transaction.clienteDestino()))
                    .findFirst()
                    .orElse(null);
*/

            System.out.println("------------------");
            System.out.println("transactionId: " + transaction.transactionId());
            System.out.println("step: " + transaction.step());
            System.out.println("type: " + transaction.type());
            System.out.println("amount: " + transaction.amount());
            System.out.println("codClienteOrigem: " + transaction.clienteOrigem().codCliente());
            System.out.println("nameOrig: " + transaction.clienteOrigem().name());
            System.out.println("oldbalanceOrg: " + transaction.clienteOrigem().oldBalance());
            System.out.println("newbalanceOrig: " + transaction.clienteOrigem().newBalance());
            System.out.println("tipoClienteOrigem: " + transaction.clienteOrigem().tipoCliente());
            System.out.println("codClienteDestino: " + transaction.clienteDestino().codCliente());
            System.out.println("nameDest: " + transaction.clienteDestino().name());
            System.out.println("oldbalanceDest: " + transaction.clienteDestino().oldBalance());
            System.out.println("newbalanceDest: " + transaction.clienteDestino().newBalance());
            System.out.println("tipoClienteDestino: " + transaction.clienteDestino().tipoCliente());
            System.out.println("isFraud: " + transaction.isFraud());
            System.out.println("isFlaggedFraud: " + transaction.isFlaggedFraud());
            System.out.println("------------------");
        }

/*        // aula 03 - ingestao de dados
        TransactionIngestor transactionIngestor = new TransactionIngestor();
        List<Transaction> transactionsList;
        try {
           transactionsList = transactionIngestor.ingestor("src/data/PS_20174392719_1491204439457_log.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i <= 10; i++) {
            System.out.println(transactionsList.get(i));
        }*/

    // IMPRIME DADOS DE OUTRO ARQUIVO COM ERROS
        // aula 04 - Tratamento de erros
/*        List<Transaction> transactionsListErro;
        transactionsListErro = transactionIngestor.ingestorBadData("src/data/paysim_with_bad_data.csv");

        for (int i = 0; i < transactionsListErro.size(); i++) {
            System.out.println(transactionsListErro.get(i));
        }*/

        //for (Transaction transaction : transactionsListErro) {
        //    System.out.println(transaction);
        //}

        // aula 05 - streams
/*
        TransactionIngestor transactionIngestor = new TransactionIngestor();
        List<Transaction> transactionsList;
        try {
            transactionsList = transactionIngestor.ingestor("src/data/PS_20174392719_1491204439457_log.csv", 50000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FraudAnalyzer analyzer = new FraudAnalyzer();

        List<Transaction> listaComFraude = analyzer.tamanhoLista(transactionsList);

        List<Transaction> listaTresMaioresFraudes = analyzer.tresMaioresFraudes(transactionsList);

        List<String> listaCincoMaioresClientes = analyzer.cincoMaioresClientes(transactionsList);

        BigDecimal prejuizoTotal = analyzer.prejuizoTotal(transactionsList);

        Map<TransactionType,Long> listaQuantidadeFraudeTipoTransacao = analyzer.quantidadeFraudeTipoTransacao(transactionsList);
*/

        // aula 06 - benchmark
        TransactionIngestor transactionIngestor = new TransactionIngestor();
        List<Transaction> transactionsList;
        try {
            transactionsList = transactionIngestor.ingestor("src/data/PS_20174392719_1491204439457_log.csv", 1000000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TransactionRepository transactionListRepository = new TransactionListRepository();

        // aula 06 - benchmark - item 3

        //transactionListRepository.buscarTransacaoPorNome(transactionsList, "C1231006815");

        //transactionListRepository.buscarTransacaoPorNome(transactionsList, "C1280323807");

        //transactionListRepository.buscarTransacaoPorNome(transactionsList, "C12345");

        // aula 06 - benchmark - item 4
        transactionListRepository.buscarTransacaoPorNomeMedindoTempo(transactionsList, "C1231006815");
        transactionListRepository.buscarTransacaoPorNomeMedindoTempo(transactionsList, "C1868032458");
        //transactionListRepository.buscarTransacaoPorNomeMedindoTempo(transactionsList, "C1868");

        // aula 06 - benchmark - item 6: carregando em Map<String, Transaction>
        TransactionMapRepository transactionMapRepository = new TransactionMapRepository();

        Map<String, Transaction> transactionsMap = transactionsList.stream()
                .collect(Collectors.toMap(
                        transaction -> transaction.clienteOrigem().name(),
                        Function.identity(),
                        (transactionAtual, transactionNova) -> transactionNova
                ));

        System.out.println("Total de chaves no Map: " + transactionsMap.size());

        Transaction transactionEncontrada = transactionMapRepository
                .buscarTransacaoPorNomeMedindoTempo(transactionsMap, "C1868032458");

        System.out.println("Transação encontrada no Map: " + transactionEncontrada);

    }

}
