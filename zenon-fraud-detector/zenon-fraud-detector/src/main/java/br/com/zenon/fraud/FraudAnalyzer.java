package br.com.zenon.fraud;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    public List<Transaction> tamanhoLista(List<Transaction> transactions) {

        // List<Transaction> listaComFraude = transactions.stream().filter(Transaction::isFraud).toList();
        List<Transaction> listaComFraude = transactions.stream()
                                                       .filter(transaction -> transaction.isFraud() == true)
                                                       .toList();

        System.out.println("Tamanho da lista de registros com fraude: " + listaComFraude.size());

        return listaComFraude;
    }

    public List<Transaction> tresMaioresFraudes(List<Transaction> transactions) {

        // List<Transaction> listaComFraude = transactions.stream().filter(Transaction::isFraud).toList();
        List<Transaction> listaTresMaioresFraude = transactions.stream()
                // .filter(transaction -> transaction.isFraud() == true)
                .sorted((transaction1, transaction2) -> transaction2.amount().compareTo(transaction1.amount()))
                .limit(3)
                .toList();

        System.out.println("Lista com os 3 maiores valores de fraude:");

        listaTresMaioresFraude.forEach(System.out::println);

        return listaTresMaioresFraude;
    }

    public List<String> cincoMaioresClientes(List<Transaction> transactions) {

        // List<Transaction> listaComFraude = transactions.stream().filter(Transaction::isFraud).toList();
        List<String> listaCincoMaioresClientes = transactions.stream()
                .sorted((transaction1, transaction2) -> transaction2.amount().compareTo(transaction1.amount()))
                .map(transaction -> transaction.clienteOrigem().name())
                .distinct()
                .limit(5)
                .toList();

        System.out.println("Lista com os 5 maiores clientes de fraude:");

        listaCincoMaioresClientes.forEach(System.out::println);

        return listaCincoMaioresClientes;
    }

    public BigDecimal prejuizoTotal(List<Transaction> transactions) {

        // List<Transaction> listaComFraude = transactions.stream().filter(Transaction::isFraud).toList();
        BigDecimal listaPrejuizoTotal = transactions.stream()
                .filter(transaction -> transaction.isFraud() == true)
                .map(transaction -> transaction.amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("Lista Prejuizo Total:" + listaPrejuizoTotal);

        return listaPrejuizoTotal;
    }

    public Map<Transaction.TransactionType,Long> quantidadeFraudeTipoTransacao(List<Transaction> transactions) {

        Map<Transaction.TransactionType,Long> listaPorTipoTransacao = transactions.stream()
                        .collect(Collectors.groupingBy(Transaction::type,
                                Collectors.counting()
                        ));

        System.out.println("Lista quantidade de fraudes por tipo transacao:");

        listaPorTipoTransacao.forEach((tipo,quatidade) -> System.out.println(tipo + " : " + quatidade));

        return listaPorTipoTransacao;
    }

}
