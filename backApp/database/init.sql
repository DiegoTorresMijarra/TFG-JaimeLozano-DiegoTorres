SELECT 'CREATE DATABASE postgres'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'postgres');

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS "categories";
DROP TABLE IF EXISTS "products";
DROP TABLE IF EXISTS "evaluation";

CREATE SEQUENCE products_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 6 CACHE 1;
CREATE SEQUENCE categories_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 4 CACHE 1;
CREATE SEQUENCE evaluation_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 6 CACHE 1;

-- Crear la tabla categories
CREATE TABLE "public"."categories"
(
    "id"         bigint    DEFAULT nextval('categories_id_seq') NOT NULL,
    "name"     character varying(255),
    "created_at" timestamp,
    "updated_at" timestamp default CURRENT_TIMESTAMP,
    "deleted_at"  timestamp default null,
    CONSTRAINT "categories_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

-- Insertar la tabla categories
INSERT INTO "categories" ("id", "name", "created_at", "updated_at")
VALUES (1, 'categoria1', '2023-01-01', '2023-01-01'),
       (2, 'categoria2',  '2023-01-02', '2023-01-02'),
       (3, 'categoria3',  '2023-01-03', '2023-01-03')
;

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
    "deleted_at"  timestamp default null,
    "category_id" bigint,
    CONSTRAINT "products_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "fk2fwq10nwymfv7fumctxt9vpgb" FOREIGN KEY ("category_id") REFERENCES "categories" ("id") NOT DEFERRABLE
) WITH (oids = false);
-- Insertar la tabla products
INSERT INTO "products" ("id", "nombre", "precio", "stock", "gluten", "created_at", "updated_at", "category_id")
VALUES (1, 'Producto1', 10.50, 100, true, '2023-01-01', '2023-01-01',1),
       (2, 'Producto2', 15.75, 50, false, '2023-01-02', '2023-01-02', 2),
       (3, 'Producto3', 20.00, 75, true, '2023-01-03', '2023-01-03', 3),
       (4, 'Producto4', 8.99, 120, false, '2023-01-04', '2023-01-04', 1),
       (5, 'Producto5', 25.50, 200, true, '2023-01-05', '2023-01-05', 2)
;

-- Crear la tabla valoraciones
CREATE TABLE "public"."evaluation"
(
    "id"         bigint    DEFAULT nextval('evaluation_id_seq') NOT NULL,
    "valoracion"     Integer NOT NULL,
    "created_at" timestamp,
    "updated_at" timestamp default CURRENT_TIMESTAMP,
    "deleted_at"  timestamp default null,
    "product_id" bigint,
    CONSTRAINT "evaluation_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "fk2fwq10nwymfv5fumctxt9fpgb" FOREIGN KEY ("product_id") REFERENCES "products" ("id") NOT DEFERRABLE
) WITH (oids = false);

-- Insertar la tabla valoraciones
INSERT INTO "evaluation" ("id", "valoracion", "created_at", "updated_at","product_id")
VALUES (1, 3, '2023-01-01', '2023-01-01',1),
       (2, 2,  '2023-01-02', '2023-01-02',2),
       (3, 3,  '2023-01-03', '2023-01-03',3),
       (4, 3,  '2023-01-03', '2023-01-03',4),
       (5, 2,  '2023-01-03', '2023-01-03',5)
;