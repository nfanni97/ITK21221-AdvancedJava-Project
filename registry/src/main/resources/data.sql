INSERT INTO roles(id,name) VALUES(1,'ROLE_USER');
INSERT INTO roles(id,name) VALUES(2,'ROLE_ADMIN');

INSERT INTO categories(id,name) VALUES(2,'decoration');
INSERT INTO categories(id,name) VALUES(3,'bathroom');

INSERT INTO products(id,name,price_huf) VALUES(1,'China vase',135000);
INSERT INTO products(id,name,price_huf) VALUES(3,'soap saver', 500);

INSERT INTO product_category(product_id,category_id) VALUES(1,2);
INSERT INTO product_category(product_id,category_id) VALUES(3,3);
