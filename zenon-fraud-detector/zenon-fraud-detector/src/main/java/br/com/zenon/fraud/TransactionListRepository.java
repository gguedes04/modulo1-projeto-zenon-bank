package br.com.zenon.fraud;

import java.util.Comparator;
import java.util.List;

public class TransactionListRepository implements TransactionRepository{

    @Override
    public List<Transaction> buscarTransacaoPorNome(List<Transaction> transactions, String nomeCliente) {

        //try {

            // List<Transaction> listaComFraude = transactions.stream().filter(Transaction::isFraud).toList();
            List<Transaction> listaCliente = transactions.stream()
                    .filter(transaction -> transaction.clienteOrigem().name().equalsIgnoreCase(nomeCliente))
                    .toList();
            listaCliente.forEach(System.out::println);

            if (listaCliente.isEmpty()) {
                throw new IllegalArgumentException("Nenhuma transação encontrada para o cliente: " + nomeCliente);
            }

            return listaCliente;

        //} catch (IllegalArgumentException e) {
         //   System.out.println(e.getMessage());
         //   throw e;
        //}

    }

    @Override
    public List<Transaction> buscarTransacaoPorNomeMedindoTempo(List<Transaction> transactions, String nomeCliente) {

        //try {

        long inicio = System.nanoTime();

        // List<Transaction> listaComFraude = transactions.stream().filter(Transaction::isFraud).toList();
        // por conta do max, o listaCliente não pode ser uma list e sim do tipo Transaction
        Transaction listaCliente = transactions.stream()
                .filter(transaction -> transaction.clienteOrigem().name().equalsIgnoreCase(nomeCliente))
                .max(Comparator.comparing(Transaction::amount))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nenhuma transação encontrada para o cliente: " + nomeCliente));

        long fim = System.nanoTime();

        System.out.println("Inicio da busca pelo nome do Cliente: " + inicio);
        System.out.println("Fim da busca pelo nome do Cliente: " + fim);
        System.out.println("Tempo Total da busca pelo nome do Cliente: " + (fim-inicio) / 1_000_000.0);

        // return listaCliente;

        return List.of(listaCliente); // Retorna a os campos da Transaction obtidos na busca e contidos na listaCliente

        //} catch (IllegalArgumentException e) {
        //   System.out.println(e.getMessage());
        //   throw e;
        //}

    }

}
