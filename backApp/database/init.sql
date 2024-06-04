SELECT 'CREATE DATABASE postgres'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'postgres');

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS "categories";
DROP TABLE IF EXISTS "evaluation";
DROP TABLE IF EXISTS "products";
DROP TABLE IF EXISTS "restaurants";
DROP TABLE IF EXISTS "offers";
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS "user_roles";

CREATE SEQUENCE products_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 21 CACHE 1;
CREATE SEQUENCE categories_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 9 CACHE 1;
CREATE SEQUENCE evaluation_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 6 CACHE 1;
CREATE SEQUENCE offers_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 3 CACHE 1;

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
       (2, 'Platos Principales', '2024-06-03', '2024-06-03'),
       (3, 'Postres', '2024-06-03', '2024-06-03'),
       (4, 'Bebidas', '2024-06-03', '2024-06-03'),
       (5, 'Aperitivos', '2024-06-03', '2024-06-03'),
       (6, 'Sopas y Ensaladas', '2024-06-03', '2024-06-03'),
       (7, 'Especialidades de la Casa', '2024-06-03', '2024-06-03'),
       (8, 'Platos Veganos', '2024-06-03', '2024-06-03')
;

-- Crear la tabla products
CREATE TABLE "public"."products"
(
    "id"              bigint                DEFAULT nextval('products_id_seq') NOT NULL,
    "name"            character varying(255),
    "price"           double precision,
    "stock"           integer,
    "gluten"          boolean,
    "image"           bytea                 Default NULL,
    "image_extension" character varying(10) default null,
    "created_at"      timestamp,
    "updated_at"      timestamp             default CURRENT_TIMESTAMP,
    "deleted_at"      timestamp             default null,
    "category_id"     bigint,
    CONSTRAINT "products_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "products_fk_categories" FOREIGN KEY ("category_id") REFERENCES "categories" ("id") NOT DEFERRABLE
) WITH (oids = false);
-- Insertar la tabla products
INSERT INTO "products" ("id", "name", "price", "stock", "gluten", "created_at", "updated_at", "category_id")
VALUES (1, 'Bruschetta Byte', 5.50, 100, true, '2024-06-03', '2024-06-03', 1),          -- Entrantes
       (2, 'Calamares Compilados', 8.75, 50, false, '2024-06-03', '2024-06-03', 1),     -- Entrantes
       (3, 'Sopa de Código', 6.00, 75, false, '2024-06-03', '2024-06-03', 6),           -- Sopas y Ensaladas
       (4, 'Ensalada César en C++', 7.99, 120, false, '2024-06-03', '2024-06-03', 6),   -- Sopas y Ensaladas
       (5, 'Hamburguesa MacJava', 10.50, 100, true, '2024-06-03', '2024-06-03', 2),     -- Platos Principales
       (6, 'Pizza Python', 12.00, 80, false, '2024-06-03', '2024-06-03', 2),            -- Platos Principales
       (7, 'Taco TensorFlow', 9.75, 60, true, '2024-06-03', '2024-06-03', 2),           -- Platos Principales
       (8, 'Sushi SQL', 14.00, 40, false, '2024-06-03', '2024-06-03', 2),               -- Platos Principales
       (9, 'Postre JSON', 5.00, 50, true, '2024-06-03', '2024-06-03', 3),               -- Postres
       (10, 'Tarta de Chocolate HTTP', 6.50, 30, false, '2024-06-03', '2024-06-03', 3), -- Postres
       (11, 'Batido Binary', 4.25, 70, true, '2024-06-03', '2024-06-03', 4),            -- Bebidas
       (12, 'Café Java', 2.50, 100, false, '2024-06-03', '2024-06-03', 4),              -- Bebidas
       (13, 'Aperitivo AJAX', 3.00, 90, true, '2024-06-03', '2024-06-03', 5),           -- Aperitivos
       (14, 'Tapas TCP/IP', 4.75, 80, false, '2024-06-03', '2024-06-03', 5),            -- Aperitivos
       (15, 'Falafel Frontend', 7.00, 60, true, '2024-06-03', '2024-06-03', 8),         -- Platos Vegetarianos
       (16, 'Burger Backend', 9.00, 50, false, '2024-06-03', '2024-06-03', 8),          -- Platos Vegetarianos
       (17, 'Wrap Web', 8.00, 70, true, '2024-06-03', '2024-06-03', 8),                 -- Platos Veganos
       (18, 'Curry Cloud', 11.00, 40, false, '2024-06-03', '2024-06-03', 8),            -- Platos Veganos
       (19, 'Paella PHP', 13.50, 30, true, '2024-06-03', '2024-06-03', 7) -- Especialidades de la Casa
;


CREATE SEQUENCE restaurants_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 4 CACHE 1;
-- Crear la tabla restaurants
CREATE TABLE "public"."restaurants"
(
    "id"         bigint    DEFAULT nextval('restaurants_id_seq') NOT NULL,
    "name"       character varying(255),
    "address"    character varying(255),
    "phone"      varchar(9),
    "created_at" timestamp,
    "updated_at" timestamp default CURRENT_TIMESTAMP,
    "deleted_at" timestamp default null,
    CONSTRAINT "restaurants_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

-- Insertar datos en la tabla restaurants
INSERT INTO "restaurants" ("id", "name", "address", "phone", "created_at", "updated_at")
VALUES (1, 'Restaurante1', 'Calle 1', 111111111, '2023-01-01', '2023-01-01'),
       (2, 'Restaurante2', 'Calle 2', 999999999, '2023-01-01', '2023-01-01'),
       (3, 'Restaurante3', 'Calle 3', 999999999, '2023-01-01', '2023-01-01')
;

-- Crear la tabla ofertas
CREATE TABLE "public"."offers"
(
    "id"          bigint    DEFAULT nextval('offers_id_seq') NOT NULL,
    "descuento"   double precision                           NOT NULL,
    "fecha_desde" timestamp                                  NOT NULL,
    "fecha_hasta" timestamp                                  NOT NULL,
    "created_at"  timestamp,
    "updated_at"  timestamp default CURRENT_TIMESTAMP,
    "deleted_at"  timestamp default null,
    "product_id"  bigint,
    CONSTRAINT "offers_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "offers_fk_products" FOREIGN KEY ("product_id") REFERENCES "products" ("id") NOT DEFERRABLE
) WITH (oids = false);

-- Insertar la tabla ofertas
INSERT INTO "offers" ("id", "descuento", "fecha_desde", "fecha_hasta", "created_at", "updated_at", "product_id")
VALUES (1, 30.0, '2024-01-01', '2024-10-10', '2023-01-01', '2023-01-01', 1),
       (2, 30.0, '2023-01-01', '2023-10-10', '2023-01-01', '2023-01-01', 1)
;

-- Creación de la tabla usuarios
CREATE TABLE "public".users
(
    "id"         uuid      DEFAULT uuid_generate_v4() NOT NULL,
    "email"      character varying(255)               NOT NULL,
    "name"       character varying(255)               NOT NULL,
    "surname"    character varying(255)               NOT NULL,
    "password"   character varying(255)               NOT NULL,
    "username"   character varying(255)               NOT NULL,
    "created_at" timestamp,
    "updated_at" timestamp default CURRENT_TIMESTAMP,
    "deleted_at" timestamp default null,
    CONSTRAINT "usuarios_email_unique_key" UNIQUE ("email"),
    CONSTRAINT "usuarios_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "usuarios_username_unique_key" UNIQUE ("username")
) WITH (oids = false);
-- Creación de la tabla user_roles
CREATE TABLE "public"."user_roles"
(
    "user_id" uuid NOT NULL,
    "roles"   character varying(255)
) WITH (oids = false);

-- Insert usuarios y roles
-- Contraseña: Admin1
INSERT INTO users (created_at, id, updated_at, surname, email, name, password, username)
VALUES ('2023-11-02 11:43:24.724871', 'a8fd9bb7-62e1-41dc-9f57-338b17c5bcc0', '2023-11-02 11:43:24.724871',
        'Admin Admin', 'admin@prueba.net', 'Admin', '$2a$10$Fjs4lFtGRtCgOsLURykTW.IGYfdqsFVXZN1jGhl3PlZAqMTKHK7S6',
        'admin');

-- Asignar roles al administrador
INSERT INTO user_roles (user_id, roles)
VALUES ('a8fd9bb7-62e1-41dc-9f57-338b17c5bcc0', 'USER');
INSERT INTO user_roles (user_id, roles)
VALUES ('a8fd9bb7-62e1-41dc-9f57-338b17c5bcc0', 'ADMIN');
INSERT INTO user_roles (user_id, roles)
VALUES ('a8fd9bb7-62e1-41dc-9f57-338b17c5bcc0', 'WORKER');

-- Contraseña: Worker1
INSERT INTO users (created_at, id, updated_at, surname, email, name, password, username)
VALUES ('2023-11-02 11:43:24.730431', '455e6e87-5c32-454b-b658-e39330ceefa2', '2023-11-02 11:43:24.730431',
        'Worker Worker', 'worker@prueba.net', 'Worker', '$2a$10$XYoJYUTLtLcQX9eytXQ/cuGk.OeUBNMWfqWHzoIFZJOMTgG11/zui',
        'worker');

-- Asignar roles al trabajador
INSERT INTO user_roles (user_id, roles)
VALUES ('455e6e87-5c32-454b-b658-e39330ceefa2', 'WORKER');


-- Contraseña: User1
INSERT INTO users (created_at, id, updated_at, surname, email, name, password, username)
VALUES ('2023-11-02 11:43:24.730431', '24bee18d-920c-4f25-971f-99e91d0aa331', '2023-11-02 11:43:24.730431',
        'User User', 'user@prueba.net', 'User', '$2a$10$co8cRNxqcwJvCoOUQD9freA/b.FcKGdlI3khs3FxqniJyo3LcpeHe', 'user');

-- Asignar roles al usuario
INSERT INTO user_roles (user_id, roles)
VALUES ('24bee18d-920c-4f25-971f-99e91d0aa331', 'USER');

-- Restricciones de clave externa
ALTER TABLE ONLY "public"."user_roles"
    ADD CONSTRAINT "user_roles_fk_user" FOREIGN KEY (user_id) REFERENCES users (id) NOT DEFERRABLE;


CREATE SEQUENCE addresses_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

-- Crear la tabla addresses
CREATE TABLE "public"."addresses"
(
    "id"          uuid                            DEFAULT uuid_generate_v4() NOT NULL,
    "country"     character varying(255) NOT NULL DEFAULT 'España',
    "province"    character varying(255) NOT NULL DEFAULT 'Madrid',
    "city"        character varying(255) NOT NULL,
    "street"      character varying(255) NOT NULL,
    "number"      character varying(255) NOT NULL,
    "apartment"   character varying(255),
    "postal_code" character varying(255) NOT NULL,
    "extra_info"  character varying(255),
    "name"        character varying(255) NOT NULL,
    "created_at"  timestamp,
    "updated_at"  timestamp                       default CURRENT_TIMESTAMP,
    "deleted_at"  timestamp                       default null,
    "user_id"     uuid,
    CONSTRAINT "ADDRESSES_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "FK_ADDRESSES_user_id" FOREIGN KEY ("user_id") REFERENCES "public"."users" ("id") ON DELETE CASCADE
) WITH (oids = false);

-- Insertar datos en la tabla addresses para casas en Leganés, Madrid
INSERT INTO "addresses" ("id", "country", "province", "city", "street", "number", "apartment", "postal_code",
                         "extra_info", "name",
                         "created_at", "updated_at", "user_id")
VALUES ('37bf14f0-db5d-4f12-bf4a-7b780cfac071', 'España', 'Madrid', 'Leganés', 'Calle de la Luna', '45', 'Bajo B',
        '28915',
        'Cuidado con el Perro', 'Casa de la familia Luna', '2023-01-03 00:00:00', '2023-01-03 00:00:00',
        '24bee18d-920c-4f25-971f-99e91d0aa331'),
       ('64873a5d-5336-45ba-8907-09a8b74da391', 'España', 'Madrid', 'Leganés', 'Avenida de la Universidad', '10',
        'Bajo C',
        '28916', NULL, 'Casa de la familia Universidad', '2023-01-04 00:00:00', '2023-01-04 00:00:00',
        '24bee18d-920c-4f25-971f-99e91d0aa331'),
       (default, 'España', 'Madrid', 'Leganés', 'Calle del Sol', '23', NULL, '28917',
        NULL,
        'Casa de la familia Sol', '2023-01-05 00:00:00', '2023-01-05 00:00:00', '24bee18d-920c-4f25-971f-99e91d0aa331'),
       (default, 'España', 'Madrid', 'Leganés', 'Calle de la Estrella', '78', NULL,
        '28918',
        NULL, 'Casa de la familia Estrella', '2023-01-06 00:00:00', '2023-01-06 00:00:00',
        '24bee18d-920c-4f25-971f-99e91d0aa331'),
       (default, 'España', 'Madrid', 'Leganés', 'Calle del Universo', '11', 'Primero B',
        '28919',
        NULL, 'Casa de la familia Universo', '2023-01-07 00:00:00', '2023-01-07 00:00:00',
        '24bee18d-920c-4f25-971f-99e91d0aa331')
;

-- Crear la tabla valoraciones
CREATE TABLE "public"."evaluation"
(
    "id"         bigint    DEFAULT nextval('evaluation_id_seq') NOT NULL,
    "value"      Integer                                        NOT NULL,
    "comment"    VARCHAR(255),
    "created_at" timestamp,
    "updated_at" timestamp default CURRENT_TIMESTAMP,
    "deleted_at" timestamp default null,
    "user_id"    uuid                                           not null,
    "product_id" bigint,
    CONSTRAINT "evaluation_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "evaluation_fk_users" FOREIGN KEY ("user_id") REFERENCES "users" ("id") NOT DEFERRABLE,
    CONSTRAINT "evaluation_fk_products" FOREIGN KEY ("product_id") REFERENCES "products" ("id") NOT DEFERRABLE
) WITH (oids = false);

-- Insertar la tabla valoraciones
INSERT INTO "evaluation" ("id", "value", "comment", "created_at", "updated_at", "user_id", "product_id")
VALUES (1, 3, 'Muy buen producto', '2023-01-01', '2023-01-01', '24bee18d-920c-4f25-971f-99e91d0aa331', 1),
       (2, 2, 'Muy buen producto', '2023-01-02', '2023-01-02', '24bee18d-920c-4f25-971f-99e91d0aa331', 2),
       (3, 3, 'Muy buen producto', '2023-01-03', '2023-01-03', '24bee18d-920c-4f25-971f-99e91d0aa331', 3),
       (4, 3, 'Muy buen producto', '2023-01-03', '2023-01-03', '24bee18d-920c-4f25-971f-99e91d0aa331', 3),
       (5, 2, 'Muy buen producto', '2023-01-03', '2023-01-03', '24bee18d-920c-4f25-971f-99e91d0aa331', 3)
;
