package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_database.db";
    private static final int DATABASE_VERSION = 2; // Increment the version for schema changes

    // Users table and columns
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";

    // Products table and columns
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_DESCRIPTION = "description";
    public static final String COLUMN_PRODUCT_IMAGE_URI = "imageUri"; // Added this for image URI

    // Cart table and columns
    public static final String TABLE_CART = "cart";
    public static final String COLUMN_CART_ID = "id";
    public static final String COLUMN_CART_PRODUCT_ID = "product_id";
    public static final String COLUMN_CART_QUANTITY = "quantity";

    // Orders table and columns
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_ID = "id";
    public static final String COLUMN_ORDER_CUSTOMER_NAME = "customer_name";
    public static final String COLUMN_ORDER_DATE = "order_date";
    public static final String COLUMN_ORDER_TOTAL = "total_price";
    public static final String COLUMN_ORDER_STATUS = "status";

    // SQL to create the users table
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT" +
                    ");";

    // SQL to create the products table
    private static final String CREATE_TABLE_PRODUCTS =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                    COLUMN_PRODUCT_PRICE + " REAL NOT NULL, " +
                    COLUMN_PRODUCT_DESCRIPTION + " TEXT, " +
                    COLUMN_PRODUCT_IMAGE_URI + " TEXT" + // Added column for image URI
                    ");";

    // SQL to create the cart table
    private static final String CREATE_TABLE_CART =
            "CREATE TABLE " + TABLE_CART + " (" +
                    COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CART_PRODUCT_ID + " INTEGER NOT NULL, " +
                    COLUMN_CART_QUANTITY + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_CART_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ")" +
                    ");";
    // SQL to create the orders table
    private static final String CREATE_TABLE_ORDERS =
            "CREATE TABLE " + TABLE_ORDERS + " (" +
                    COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDER_CUSTOMER_NAME + " TEXT NOT NULL, " +
                    COLUMN_ORDER_DATE + " TEXT NOT NULL, " +
                    COLUMN_ORDER_TOTAL + " REAL NOT NULL, " +
                    COLUMN_ORDER_STATUS + " TEXT NOT NULL" + // E.g., Pending, Confirmed
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_CART); // Create the cart table
        db.execSQL(CREATE_TABLE_ORDERS);

        // Pre-fill default admin and customer accounts
        preFillAccounts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables and recreate them if database version is upgraded
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // Pre-fill default accounts (users) for testing
    private void preFillAccounts(SQLiteDatabase db) {
        // Admin account (numeric username)
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS +
                " (" + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + ", " + COLUMN_EMAIL + ") VALUES " +
                "('12345', '12345', NULL)");

        // Customer accounts
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS +
                " (" + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + ", " + COLUMN_EMAIL + ") VALUES " +
                "('hawra', '12345', 'hawra@gmail.com')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS +
                " (" + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + ", " + COLUMN_EMAIL + ") VALUES " +
                "('claudia', '12345', 'claud@gmail.com')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS +
                " (" + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + ", " + COLUMN_EMAIL + ") VALUES " +
                "('gracie', '12345', 'cool@hotmail.com')");
    }

    // Get all customers (users)
    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_EMAIL},
                "NOT " + COLUMN_USERNAME + " GLOB '[0-9]*'", // Exclude numeric usernames
                null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                customerList.add(new Customer(id, username, email));
            }
            cursor.close();
        }
        return customerList;
    }

    // Delete a customer by ID
    public boolean deleteCustomer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_USERS, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }

    public String getUserEmail(String loggedInUsername) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT email FROM users WHERE username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{loggedInUsername});

        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("email"));
        }

        return null; // Return null if no email is found
    }

    // Get all products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESCRIPTION));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
                String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE_URI));

                products.add(new Product(id, name, description, price, imageUri));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return products;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_ORDERS, // Table to query
                null, null, null, null, null, null // No ordering
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID));
                String customerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_CUSTOMER_NAME));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE));
                double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS));

                orders.add(new Order(id, customerName, date, totalPrice, status));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return orders;
    }


    // Delete a product by ID
    public void deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("products", "id = ?", new String[]{String.valueOf(productId)});
        db.close();


    }

    // Insert a new product
    public boolean insertProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO " + TABLE_PRODUCTS +
                " (" + COLUMN_PRODUCT_NAME + ", " + COLUMN_PRODUCT_PRICE + ", " + COLUMN_PRODUCT_DESCRIPTION + ", " + COLUMN_PRODUCT_IMAGE_URI + ") VALUES " +
                "('" + product.getName() + "', " + product.getPrice() + ", '" + product.getDescription() + "', '" + product.getImageUri() + "')";
        db.execSQL(query);
        return true;
    }

    public void addToCart(int productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_CART,
                new String[]{COLUMN_CART_ID, COLUMN_CART_QUANTITY},
                COLUMN_CART_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(productId)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            // Update quantity if the product is already in the cart
            int existingQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_QUANTITY, existingQuantity + quantity);

            db.update(TABLE_CART, values, COLUMN_CART_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        } else {
            // Add a new product to the cart
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_PRODUCT_ID, productId);
            values.put(COLUMN_CART_QUANTITY, quantity);

            db.insert(TABLE_CART, null, values);
        }

        cursor.close();
        db.close();
    }
    public void deleteCartItemByName(String itemName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_CART_PRODUCT_ID + " IN (SELECT id FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_NAME + " = ?)", new String[]{itemName});
        db.close();
    }
    public void updateCartItemQuantity(String itemName, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_QUANTITY, newQuantity);

        db.update(TABLE_CART, values, COLUMN_CART_PRODUCT_ID + " IN (SELECT id FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_NAME + " = ?)", new String[]{itemName});
        db.close();
    }


    public List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT c." + COLUMN_CART_ID + ", p." + COLUMN_PRODUCT_NAME + ", p." + COLUMN_PRODUCT_PRICE + ", c." + COLUMN_CART_QUANTITY +
                " FROM " + TABLE_CART + " c " +
                " JOIN " + TABLE_PRODUCTS + " p ON c." + COLUMN_CART_PRODUCT_ID + " = p." + COLUMN_PRODUCT_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                //int cartId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                double productPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));

                // Create a new CartItem using the updated constructor
                cartItems.add(new CartItem( name, quantity, productPrice));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return cartItems;
    }
    public void addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_CUSTOMER_NAME, order.getCustomerName());
        values.put(COLUMN_ORDER_DATE, order.getDate());
        values.put(COLUMN_ORDER_TOTAL, order.getTotalPrice());
        values.put(COLUMN_ORDER_STATUS, order.getStatus());

        db.insert(TABLE_ORDERS, null, values);
        db.close();
    }
    public List<Order> searchOrders(String query) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_ORDERS,
                null,
                COLUMN_ORDER_CUSTOMER_NAME + " LIKE ?",
                new String[]{"%" + query + "%"},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID));
                String customerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_CUSTOMER_NAME));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE));
                double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS));

                orders.add(new Order(id, customerName, date, totalPrice, status));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orders;
    }
    public void cancelOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDERS, COLUMN_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)});
        db.close();
    }
}
