package br.com.zenon;
import br.com.zenon.fraud.Cliente;
import br.com.zenon.fraud.Transaction;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Cliente> clientes = List.of(
                new Cliente("C1231006815", 170136.0, 160296.36, "ORIGEM"),
                new Cliente("M1979787155", 0.0, 0.0, "DESTINO"),
                new Cliente("C1280323807", 850002.52, 0.0, "ORIGEM"),
                new Cliente("C873221189", 6510099.11, 7360101.63, "DESTINO")
        );

        List<Transaction> transactions = List.of(
                new Transaction(1, "PAYMENT", 9839.64, "C1231006815", "M1979787155", false, false),
                new Transaction(743,"CASH_OUT", 850002.52, "C1280323807", "C873221189", true, false)
        );

        for (Transaction transaction : transactions) {
            Cliente clienteOrigem = clientes.stream()
                    .filter(cliente -> cliente.name().equals(transaction.clienteOrigem()))
                    .findFirst()
                    .orElse(null);

            Cliente clienteDestino = clientes.stream()
                    .filter(cliente -> cliente.name().equals(transaction.clienteDestino()))
                    .findFirst()
                    .orElse(null);

            System.out.println("------------------");
            System.out.println("step: " + transaction.step());
            System.out.println("type: " + transaction.type());
            System.out.println("amount: " + transaction.amount());
            System.out.println("nameOrig: " + (clienteOrigem != null ? clienteOrigem.name() : "N/A"));
            System.out.println("oldbalanceOrg: " + (clienteOrigem != null ? clienteOrigem.oldbalance() : "N/A"));
            System.out.println("newbalanceOrig: " + (clienteOrigem != null ? clienteOrigem.newbalance() : "N/A"));
            System.out.println("nameDest: " + (clienteDestino != null ? clienteDestino.name() : "N/A"));
            System.out.println("oldbalanceDest: " + (clienteDestino != null ? clienteDestino.oldbalance() : "N/A"));
            System.out.println("newbalanceDest: " + (clienteDestino != null ? clienteDestino.newbalance() : "N/A"));
            System.out.println("isFraud: " + transaction.isFraud());
            System.out.println("isFlaggedFraud: " + transaction.isFlaggedFraud());
            System.out.println("------------------");
        }
    }
}
