db.createUser({
    user: 'admin',
    pwd: 'admin',
    roles: [
        {
            role: 'readWrite',
            db: 'mongo',
        },
    ],
});

db = db.getSiblingDB('mongo');

db.createCollection('order');

// Insertamos los datos de la coleccion pedidos
db.order.insertMany([
    {
        userId: BinData(3, "JU8Mko3hviQxowod6Zkflw=="),
        restaurantId: 1,
        addressesId: '37bf14f0-db5d-4f12-bf4a-7b780cfac071',
        orderedProducts: [
            {
                quantity: 2,
                productId: 1,
                productPrice: 10.50,
                totalPrice: 21.00,
            },
            {
                quantity: 3,
                productId: 3,
                productPrice: 20.00,
                totalPrice: 60.00,
            },
        ],
        totalPrice: 81.00,
        totalQuantityProducts: 5,
        isPaid: false,
        createdAt: '2023-10-23T12:57:17.3411925',
        updatedAt: '2023-10-23T12:57:17.3411925',
        deletedAt: null,
    },
    {
        userId: BinData(3, "JU8Mko3hviQxowod6Zkflw=="),
        restaurantId: 2,
        addressesId: '37bf14f0-db5d-4f12-bf4a-7b780cfac071',
        orderedProducts: [
            {
                quantity: 4,
                productId: 1,
                productPrice: 10.5,
                totalPrice: 42.0
            }
        ],
        totalPrice: 42.0,
        totalQuantityProducts: 4,
        isPaid: false,
        createdAt: '2022-10-23T12:57:17.3411925',
        updatedAt: '2022-10-23T12:57:17.3411925',
        deletedAt: null
    }
]);
