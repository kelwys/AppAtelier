# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table administrador (
  id                        bigint auto_increment not null,
  nome                      varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  constraint pk_administrador primary key (id))
;

create table categoria (
  id                        bigint auto_increment not null,
  descricao                 varchar(255),
  constraint pk_categoria primary key (id))
;

create table cliente (
  id                        bigint auto_increment not null,
  nome                      varchar(255),
  cpf                       varchar(255),
  telefone                  varchar(255),
  endereco                  varchar(255),
  numero                    integer,
  cep                       varchar(255),
  complemento               varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  datanascimento            date,
  sexo                      varchar(255),
  constraint pk_cliente primary key (id))
;

create table item_pedido (
  id                        bigint auto_increment not null,
  pedido_id                 bigint,
  produto_id                bigint,
  quantidade                integer,
  preco_unitario            double,
  preco_total               double,
  constraint pk_item_pedido primary key (id))
;

create table pedido (
  id                        bigint auto_increment not null,
  cliente_id                bigint,
  data_pedido               datetime(6),
  valor_total               double,
  status                    integer,
  constraint pk_pedido primary key (id))
;

create table produto (
  id                        bigint auto_increment not null,
  codigo_barras             varchar(255),
  nome                      varchar(255),
  descricao                 varchar(255),
  categoria_id              bigint,
  foto                      varchar(255),
  preco                     double,
  quantidade_estoque        bigint,
  constraint pk_produto primary key (id))
;

alter table item_pedido add constraint fk_item_pedido_pedido_1 foreign key (pedido_id) references pedido (id) on delete restrict on update restrict;
create index ix_item_pedido_pedido_1 on item_pedido (pedido_id);
alter table item_pedido add constraint fk_item_pedido_produto_2 foreign key (produto_id) references produto (id) on delete restrict on update restrict;
create index ix_item_pedido_produto_2 on item_pedido (produto_id);
alter table pedido add constraint fk_pedido_cliente_3 foreign key (cliente_id) references cliente (id) on delete restrict on update restrict;
create index ix_pedido_cliente_3 on pedido (cliente_id);
alter table produto add constraint fk_produto_categoria_4 foreign key (categoria_id) references categoria (id) on delete restrict on update restrict;
create index ix_produto_categoria_4 on produto (categoria_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table administrador;

drop table categoria;

drop table cliente;

drop table item_pedido;

drop table pedido;

drop table produto;

SET FOREIGN_KEY_CHECKS=1;

