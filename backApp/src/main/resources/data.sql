-- Insertar la tabla categories
INSERT INTO "categories" ("id", "name", "created_at", "updated_at")
VALUES (1, 'categoria1', '2023-01-01', '2023-01-01'),
       (2, 'categoria2',  '2023-01-02', '2023-01-02'),
       (3, 'categoria3',  '2023-01-03', '2023-01-03')
;

-- Insertar la tabla products
INSERT INTO "products" ("id", "name", "price", "stock", "gluten", "created_at", "updated_at", "category_id")
VALUES (1, 'Producto1', 10.50, 100, true, '2023-01-01', '2023-01-01',1),
       (2, 'Producto2', 15.75, 50, false, '2023-01-02', '2023-01-02', 2),
       (3, 'Producto3', 20.00, 75, true, '2023-01-03', '2023-01-03', 3),
       (4, 'Producto4', 8.99, 120, false, '2023-01-04', '2023-01-04', 1),
       (5, 'Producto5', 25.50, 200, true, '2023-01-05', '2023-01-05', 2)
;
INSERT INTO "restaurants" ("id", "name", "address", phone, "created_at", "updated_at")
VALUES (1, 'Restaurante1','Calle 1', 111111111, '2023-01-01', '2023-01-01'),
       (2, 'Restaurante2','Calle 2', 999999999, '2023-01-01', '2023-01-01'),
       (3, 'Restaurante3','Calle 3', 999999999, '2023-01-01', '2023-01-01')
;
-- Insertar la tabla valoraciones
INSERT INTO "evaluation" ("id", "valoracion", "created_at", "updated_at","product_id")
VALUES (1, 3, '2023-01-01', '2023-01-01',1),
       (2, 2,  '2023-01-02', '2023-01-02',2),
       (3, 3,  '2023-01-03', '2023-01-03',3),
       (4, 3,  '2023-01-03', '2023-01-03',3),
       (5, 2,  '2023-01-03', '2023-01-03',3)
;

-- Insertar la tabla ofertas
INSERT INTO "offers" ("id", "descuento", "fecha_desde", "fecha_hasta", "created_at", "updated_at","product_id")
VALUES (1, 30.0, '2024-01-01', '2024-10-10', '2023-01-01', '2023-01-01',1),
       (2, 30.0, '2023-01-01', '2023-10-10', '2023-01-01', '2023-01-01',1)
;
-- Insert usuarios y roles
-- Contraseña: Admin1
INSERT INTO users (created_at, id, updated_at, surname, email, name, password, username)
VALUES ('2023-11-02 11:43:24.724871', '00000000-0000-0000-0000-000000000000', '2023-11-02 11:43:24.724871', 'Admin Admin', 'admin@prueba.net', 'Admin', '$2a$10$Fjs4lFtGRtCgOsLURykTW.IGYfdqsFVXZN1jGhl3PlZAqMTKHK7S6', 'admin');

-- Asignar roles al administrador
INSERT INTO user_roles (user_id, roles)
VALUES ('00000000-0000-0000-0000-000000000000', 'USER');
INSERT INTO user_roles (user_id, roles)
VALUES ('00000000-0000-0000-0000-000000000000', 'ADMIN');

-- Contraseña: User1
INSERT INTO users (created_at, id, updated_at, surname, email, name, password, username)
VALUES ('2023-11-02 11:43:24.730431', '00000000-0000-0000-0000-000000000001', '2023-11-02 11:43:24.730431', 'User User', 'user@prueba.net', 'User', '$2a$10$co8cRNxqcwJvCoOUQD9freA/b.FcKGdlI3khs3FxqniJyo3LcpeHe', 'user');

-- Asignar roles al usuario
INSERT INTO user_roles (user_id, roles)
VALUES ('00000000-0000-0000-0000-000000000001', 'USER');