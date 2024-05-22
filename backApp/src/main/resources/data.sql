-- Insertar la tabla categories
INSERT INTO "categories" ("id", "name", "created_at", "updated_at")
VALUES (1, 'categoria1', '2023-01-01', '2023-01-01'),
       (2, 'categoria2',  '2023-01-02', '2023-01-02'),
       (3, 'categoria3',  '2023-01-03', '2023-01-03')
;

-- Insertar la tabla products
INSERT INTO "products" ("id", "nombre", "precio", "stock", "gluten", "created_at", "updated_at", "category_id")
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
       (4, 3,  '2023-01-03', '2023-01-03',4),
       (5, 2,  '2023-01-03', '2023-01-03',5)
;