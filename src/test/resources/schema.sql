DROP TABLE IF EXISTS CART;
DROP TABLE IF EXISTS PRODUCTS;
DROP TABLE IF EXISTS MEMBERS;

CREATE TABLE PRODUCTS
(
    id    INT     NOT NULL AUTO_INCREMENT,
    name  VARCHAR NOT NULL,
    price INT     NOT NULL,
    image VARCHAR NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE MEMBERS
(
    id          INT     NOT NULL AUTO_INCREMENT,
    email       VARCHAR NOT NULL UNIQUE,
    password    VARCHAR NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE CART
(
    id          INT NOT NULL AUTO_INCREMENT,
    member_id     INT NOT NULL,
    product_id  INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(member_id)  REFERENCES MEMBERS(id)  ON DELETE CASCADE,
    FOREIGN KEY(product_id) REFERENCES PRODUCTS(id) ON DELETE CASCADE
);
