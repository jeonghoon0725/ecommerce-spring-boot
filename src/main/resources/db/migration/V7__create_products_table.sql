DROP TABLE IF EXISTS products;


CREATE TABLE products (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          price DECIMAL(10, 2),
                          stock INT,
                          category_id INT,
                          created_at DATETIME,
                          updated_at DATETIME
);

