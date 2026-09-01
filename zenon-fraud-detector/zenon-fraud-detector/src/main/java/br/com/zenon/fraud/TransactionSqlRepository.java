package br.com.zenon.fraud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TransactionSqlRepository implements TransactionRepository, AutoCloseable {

    private final Connection connection;

    // Metodo que irá realizar a conexão com o banco de dados
    public TransactionSqlRepository(String jdbcUrl, String username, String password) {
        try {
            this.connection = DriverManager.getConnection(jdbcUrl, username, password);
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao abrir conexão com o MySQL", e);
        }
    }

    @Override
    public List<Transaction> buscarTransacaoPorNome(List<Transaction> transactions, String nomeCliente) {
        return List.of();
    }

    @Override
    public List<Transaction> buscarTransacaoPorNomeMedindoTempo(List<Transaction> transactions, String nomeCliente) {
        return List.of();
    }

    @Override
    public Transaction save(Transaction transaction) {
        Objects.requireNonNull(transaction, "transaction não pode ser nula");

        String insertClienteSql = """
                insert into cliente (name, oldbalance, newbalance, tipo_cliente)
                values (?, ?, ?, ?)
                """;

        String insertTransactionSql = """
                insert into `transaction`
                (step, type, amount, cod_cliente_origem, cod_cliente_destino, isfraud, isflagged_fraud)
                values (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                PreparedStatement insertCliente = connection.prepareStatement(insertClienteSql, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement insertTransaction = connection.prepareStatement(insertTransactionSql, Statement.RETURN_GENERATED_KEYS)
        ) {
            Cliente origem = transaction.clienteOrigem();
            Cliente destino = transaction.clienteDestino();

            long origemId = insertCliente(insertCliente, origem);
            long destinoId = insertCliente(insertCliente, destino);

            insertTransaction.setInt(1, transaction.step());
            insertTransaction.setString(2, transaction.type().name());
            insertTransaction.setBigDecimal(3, transaction.amount());
            insertTransaction.setLong(4, origemId);
            insertTransaction.setLong(5, destinoId);
            insertTransaction.setBoolean(6, transaction.isFraud());
            insertTransaction.setBoolean(7, transaction.isFlaggedFraud());

            insertTransaction.executeUpdate();

            long transactionId;
            try (ResultSet rs = insertTransaction.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new SQLException("Não foi possível obter transactionid gerado");
                }
                transactionId = rs.getLong(1);
            }

            connection.commit();

            Cliente persistedOrigem = new Cliente(
                    origemId,
                    origem.name(),
                    origem.oldBalance(),
                    origem.newBalance(),
                    origem.tipoCliente()
            );

            Cliente persistedDestino = new Cliente(
                    destinoId,
                    destino.name(),
                    destino.oldBalance(),
                    destino.newBalance(),
                    destino.tipoCliente()
            );

            return new Transaction(
                    transactionId,
                    transaction.step(),
                    transaction.type(),
                    transaction.amount(),
                    persistedOrigem,
                    persistedDestino,
                    transaction.isFraud(),
                    transaction.isFlaggedFraud()
            );
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new RuntimeException("Erro ao fazer rollback", rollbackException);
            }
            throw new RuntimeException("Erro ao salvar transaction no MySQL", e);
        }
    }

    public Transaction findByOriginName(String nomeCliente) {
        String sql = """
                select
                    t.transactionid,
                    t.step,
                    t.type,
                    t.amount,
                    t.isfraud,
                    t.isflagged_fraud,
                    co.cod_cliente as origem_cod_cliente,
                    co.name as origem_name,
                    co.oldbalance as origem_oldbalance,
                    co.newbalance as origem_newbalance,
                    co.tipo_cliente as origem_tipo_cliente,
                    cd.cod_cliente as destino_cod_cliente,
                    cd.name as destino_name,
                    cd.oldbalance as destino_oldbalance,
                    cd.newbalance as destino_newbalance,
                    cd.tipo_cliente as destino_tipo_cliente
                from `transaction` t
                inner join cliente co on co.cod_cliente = t.cod_cliente_origem
                inner join cliente cd on cd.cod_cliente = t.cod_cliente_destino
                where co.name = ?
                order by t.transactionid desc
                limit 1
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomeCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("Nenhuma transação encontrada para o cliente: " + nomeCliente);
                }

                Cliente origem = new Cliente(
                        rs.getLong("origem_cod_cliente"),
                        rs.getString("origem_name"),
                        rs.getBigDecimal("origem_oldbalance"),
                        rs.getBigDecimal("origem_newbalance"),
                        Cliente.TipoCliente.valueOf(rs.getString("origem_tipo_cliente"))
                );

                Cliente destino = new Cliente(
                        rs.getLong("destino_cod_cliente"),
                        rs.getString("destino_name"),
                        rs.getBigDecimal("destino_oldbalance"),
                        rs.getBigDecimal("destino_newbalance"),
                        Cliente.TipoCliente.valueOf(rs.getString("destino_tipo_cliente"))
                );

                return new Transaction(
                        rs.getLong("transactionid"),
                        rs.getInt("step"),
                        Transaction.TransactionType.valueOf(rs.getString("type")),
                        rs.getBigDecimal("amount"),
                        origem,
                        destino,
                        rs.getBoolean("isfraud"),
                        rs.getBoolean("isflagged_fraud")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar transaction no MySQL", e);
        }
    }

    private long insertCliente(PreparedStatement stmt, Cliente cliente) throws SQLException {
        stmt.clearParameters();
        stmt.setString(1, cliente.name());
        stmt.setBigDecimal(2, cliente.oldBalance());
        stmt.setBigDecimal(3, cliente.newBalance());
        stmt.setString(4, cliente.tipoCliente().name());
        stmt.executeUpdate();

        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (!rs.next()) {
                throw new SQLException("Não foi possível obter cod_cliente gerado");
            }
            return rs.getLong(1);
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar conexão com o MySQL", e);
        }
    }
}