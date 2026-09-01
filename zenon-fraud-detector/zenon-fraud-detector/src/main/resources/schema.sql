create table cliente (
    cod_cliente bigint not null auto_increment,
    name varchar(30) not null,
    oldbalance numeric(18,6),
    newbalance numeric(18,6),
    tipo_cliente enum('ORIGEM', 'DESTINO') not null,
    primary key (cod_cliente)
);

create table `transaction` (
    transactionid bigint not null auto_increment,
    step integer not null,
    type enum('CASH_IN', 'CASH_OUT', 'TRANSFER', 'DEBIT', 'PAYMENT') not null,
    amount numeric(18,6),
    cod_cliente_origem bigint not null,
    cod_cliente_destino bigint not null,
    isfraud boolean,
    isflagged_fraud boolean,
    primary key (transactionid),
    constraint fk_transaction_cliente_origem
        foreign key (cod_cliente_origem) references cliente (cod_cliente),
    constraint fk_transaction_cliente_destino
        foreign key (cod_cliente_destino) references cliente (cod_cliente)
);
