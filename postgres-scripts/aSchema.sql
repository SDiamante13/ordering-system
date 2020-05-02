create table customers
(
  customer_id bigserial not null
    constraint customers_pkey
    primary key,
  first_name varchar(255) not null,
  last_name varchar(255) not null,
  card_number varchar(255),
  expiration_date varchar(255),
  security_code varchar(3),
  zip_code varchar(5)
);

alter table customers owner to postgres;

create table orders
(
  order_id bigserial not null
    constraint orders_pkey
    primary key,
  order_date date not null,
  total_balance double precision not null
    constraint orders_total_balance_check
    check ((total_balance <= ('9223372036854775807'::bigint)::double precision) AND (total_balance >= (1)::double precision)),
  customer_fk bigint not null
    constraint fklctjwy900y7l1xmwulg4rkeb3
    references customers
);

alter table orders owner to postgres;

create table products
(
  product_id bigserial not null
    constraint products_pkey
    primary key,
  category varchar(255) not null,
  description varchar(80) not null,
  manufacturer varchar(255) not null,
  price double precision not null,
  product_image varchar(2000000),
  product_name varchar(255) not null,
  quantity integer not null
);

alter table products owner to postgres;

create table order_product
(
  order_id bigint not null
    constraint fkl5mnj9n0di7k1v90yxnthkc73
    references orders,
  product_id bigint not null
    constraint fko6helt0ucmegaeachjpx40xhe
    references products,
  constraint order_product_pkey
    primary key (order_id, product_id)
);

alter table order_product owner to postgres;

create table users
(
  id bigserial not null
    constraint users_pkey
    primary key,
  password varchar(255),
  username varchar(255)
);

alter table users owner to postgres;

create table user_roles
(
  user_id bigint not null
    constraint fkhfh9dx7w3ubf1co1vdev94g3f
    references users,
  roles varchar(255)
);

alter table user_roles owner to postgres;

