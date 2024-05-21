SELECT 'CREATE DATABASE postgres'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'postgres');

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS "products";

CREATE SEQUENCE products_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 6 CACHE 1;
-- Crear la tabla products
CREATE TABLE "public"."products"
(
    "id"         bigint    DEFAULT nextval('products_id_seq') NOT NULL,
    "nombre"     character varying(255),
    "precio"     double precision,
    "stock"      integer,
    "gluten"     boolean,
    "created_at" timestamp,
    "updated_at" timestamp default CURRENT_TIMESTAMP,
    "deleted_at" timestamp default null,
    CONSTRAINT "products_pkey" PRIMARY KEY ("id")
) WITH (oids = false);
-- Insertar la tabla products
INSERT INTO "products" ("id", "nombre", "precio", "stock", "gluten", "created_at", "updated_at")
VALUES (1, 'Producto1', 10.50, 100, true, '2023-01-01', '2023-01-01'),
       (2, 'Producto2', 15.75, 50, false, '2023-01-02', '2023-01-02'),
       (3, 'Producto3', 20.00, 75, true, '2023-01-03', '2023-01-03'),
       (4, 'Producto4', 8.99, 120, false, '2023-01-04', '2023-01-04'),
       (5, 'Producto5', 25.50, 200, true, '2023-01-05', '2023-01-05')
;

CREATE SEQUENCE restaurants_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 4 CACHE 1;
-- Crear la tabla restaurants
CREATE TABLE "public"."restaurants"
(
    "id"         bigint    DEFAULT nextval('restaurants_id_seq') NOT NULL,
    "name"       character varying(255),
    "address"    character varying(255),
    phone        varchar(9),
    "created_at" timestamp,
    "updated_at" timestamp default CURRENT_TIMESTAMP,
    "deleted_at" timestamp default null,
    CONSTRAINT "restaurants_pkey" PRIMARY KEY ("id")
) WITH (oids = false);
-- Insertar datos en la tabla restaurants
INSERT INTO "restaurants" ("id", "name", "address", phone, "created_at", "updated_at")
VALUES (1, 'Restaurante1','Calle 1', 111111111, '2023-01-01', '2023-01-01'),
       (2, 'Restaurante2','Calle 2', 999999999, '2023-01-01', '2023-01-01'),
       (3, 'Restaurante3','Calle 3', 999999999, '2023-01-01', '2023-01-01')
;