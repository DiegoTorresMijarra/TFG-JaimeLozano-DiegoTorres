SELECT 'CREATE DATABASE postgres'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'postgres');

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS "categories";
DROP TABLE IF EXISTS "evaluation";
DROP TABLE IF EXISTS "products";
DROP TABLE IF EXISTS "restaurants";
DROP TABLE IF EXISTS "offers";
DROP TABLE IF EXISTS "usuarios";
DROP TABLE IF EXISTS "user_roles";

CREATE SEQUENCE products_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 6 CACHE 1;
CREATE SEQUENCE categories_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 4 CACHE 1;
CREATE SEQUENCE evaluation_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 6 CACHE 1;
CREATE SEQUENCE offers_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 2 CACHE 1;

-- Crear la tabla categories
CREATE TABLE "public"."categories"
(
    "id"         bigint    DEFAULT nextval('categories_id_seq') NOT NULL,
    "name"       character varying(255),
    "created_at" timestamp,
    "updated_at" timestamp default CURRENT_TIMESTAMP,
    "deleted_at" timestamp default null,
    CONSTRAINT "categories_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

-- Insertar la tabla categories
INSERT INTO "categories" ("id", "name", "created_at", "updated_at")
VALUES (1, 'Sin Categoria', '2023-01-01', '2023-01-01'),
       (2, 'categoria2', '2023-01-02', '2023-01-02'),
       (3, 'categoria3', '2023-01-03', '2023-01-03')
;

-- Crear la tabla products
CREATE TABLE "public"."products"
(
    "id"          bigint    DEFAULT nextval('products_id_seq') NOT NULL,
    "nombre"      character varying(255),
    "precio"      double precision,
    "stock"       integer,
    "gluten"      boolean,
    "created_at"  timestamp,
    "updated_at"  timestamp default CURRENT_TIMESTAMP,
    "deleted_at"  timestamp default null,
    "category_id" bigint,
    CONSTRAINT "products_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "products_fk_categories" FOREIGN KEY ("category_id") REFERENCES "categories" ("id") NOT DEFERRABLE
) WITH (oids = false);
-- Insertar la tabla products
INSERT INTO "products" ("id", "nombre", "precio", "stock", "gluten", "created_at", "updated_at", "category_id")
VALUES (1, 'Producto1', 10.50, 100, true, '2023-01-01', '2023-01-01', 1),
       (2, 'Producto2', 15.75, 50, false, '2023-01-02', '2023-01-02', 2),
       (3, 'Producto3', 20.00, 75, true, '2023-01-03', '2023-01-03', 3),
       (4, 'Producto4', 8.99, 120, false, '2023-01-04', '2023-01-04', 1),
       (5, 'Producto5', 25.50, 200, true, '2023-01-05', '2023-01-05', 2)
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
VALUES (1, 'Restaurante1', 'Calle 1', 111111111, '2023-01-01', '2023-01-01'),
       (2, 'Restaurante2', 'Calle 2', 999999999, '2023-01-01', '2023-01-01'),
       (3, 'Restaurante3', 'Calle 3', 999999999, '2023-01-01', '2023-01-01')
;

-- Crear la tabla valoraciones
CREATE TABLE "public"."evaluation"
(
    "id"         bigint    DEFAULT nextval('evaluation_id_seq') NOT NULL,
    "valoracion" Integer                                        NOT NULL,
    "created_at" timestamp,
    "updated_at" timestamp default CURRENT_TIMESTAMP,
    "deleted_at" timestamp default null,
    "product_id" bigint,
    CONSTRAINT "evaluation_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "evaluation_fk_products" FOREIGN KEY ("product_id") REFERENCES "products" ("id") NOT DEFERRABLE
) WITH (oids = false);

-- Insertar la tabla valoraciones
INSERT INTO "evaluation" ("id", "valoracion", "created_at", "updated_at","product_id")
VALUES (1, 3, '2023-01-01', '2023-01-01',1),
       (2, 2,  '2023-01-02', '2023-01-02',2),
       (3, 3,  '2023-01-03', '2023-01-03',3),
       (4, 3,  '2023-01-03', '2023-01-03',3),
       (5, 2,  '2023-01-03', '2023-01-03',3)
;

-- Crear la tabla ofertas
CREATE TABLE "public"."offers"
(
    "id"         bigint    DEFAULT nextval('offers_id_seq') NOT NULL,
    "descuento"     double precision NOT NULL,
    "fecha_desde" timestamp NOT NULL,
    "fecha_hasta" timestamp NOT NULL,
    "created_at" timestamp,
    "updated_at" timestamp default CURRENT_TIMESTAMP,
    "deleted_at"  timestamp default null,
    "product_id" bigint,
    CONSTRAINT "offers_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "fk2fwq10nwymfv3fumatxt9fpgb" FOREIGN KEY ("product_id") REFERENCES "products" ("id") NOT DEFERRABLE
) WITH (oids = false);

-- Insertar la tabla ofertas
INSERT INTO "offers" ("id", "descuento", "fecha_desde", "fecha_hasta", "created_at", "updated_at","product_id")
VALUES (1, 30.0, '2024-01-01', '2024-10-10', '2023-01-01', '2023-01-01',1),
       (2, 30.0, '2023-01-01', '2023-10-10', '2023-01-01', '2023-01-01',1)
;

-- Creación de la tabla usuarios
CREATE TABLE "public"."usuarios"
(
    "deleted_at" boolean DEFAULT false,
    "created_at" timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "id" uuid DEFAULT uuid_generate_v4() NOT NULL,
    "updated_at" timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "apellidos" character varying(255) NOT NULL,
    "email" character varying(255) NOT NULL,
    "nombre" character varying(255) NOT NULL,
    "password" character varying(255) NOT NULL,
    "username" character varying(255) NOT NULL,
    CONSTRAINT "usuarios_email_key" UNIQUE ("email"),
    CONSTRAINT "usuarios_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "usuarios_username_key" UNIQUE ("username")
) WITH (oids = false);
-- Creación de la tabla user_roles
CREATE TABLE "public"."user_roles"
(
    "user_id" uuid NOT NULL,
    "roles" character varying(255)
) WITH (oids = false);

-- Insert usuarios y roles
-- Contraseña: Admin1
INSERT INTO usuarios (deleted_at, created_at, id, updated_at, apellidos, email, nombre, password, username)
VALUES (false, '2023-11-02 11:43:24.724871', '00000000-0000-0000-0000-000000000000', '2023-11-02 11:43:24.724871', 'Admin Admin', 'admin@prueba.net', 'Admin', '$2a$10$vPaqZvZkz6jhb7U7k/V/v.5vprfNdOnh4sxi/qpPRkYTzPmFlI9p2', 'admin');

-- Asignar roles al administrador
INSERT INTO user_roles (user_id, roles)
VALUES ('00000000-0000-0000-0000-000000000000', 'USER');
INSERT INTO user_roles (user_id, roles)
VALUES ('00000000-0000-0000-0000-000000000000', 'ADMIN');

-- Contraseña: User1
INSERT INTO usuarios (deleted_at, created_at, id, updated_at, apellidos, email, nombre, password, username)
VALUES (false, '2023-11-02 11:43:24.730431', '00000000-0000-0000-0000-000000000001', '2023-11-02 11:43:24.730431', 'User User', 'user@prueba.net', 'User', '$2a$12$RUq2ScW1Kiizu5K4gKoK4OTz80.DWaruhdyfi2lZCB.KeuXTBh0S.', 'user');

-- Asignar roles al usuario
INSERT INTO user_roles (user_id, roles)
VALUES ('00000000-0000-0000-0000-000000000001', 'USER');

-- Restricciones de clave externa
ALTER TABLE ONLY "public"."user_roles"
    ADD CONSTRAINT "fk2chxp26bnpqjibydrikgq4t9e" FOREIGN KEY (user_id) REFERENCES usuarios (id) NOT DEFERRABLE;