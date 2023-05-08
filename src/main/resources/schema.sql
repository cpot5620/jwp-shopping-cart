CREATE TABLE if not exists PRODUCT
(
    id        BIGINT UNSIGNED AUTO_INCREMENT NOT NULL,
    name      VARCHAR(100)                   NOT NULL,
    price     INT                            NOT NULL,
    image_url VARCHAR(255)                   NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE if not exists MEMBER
(
    id       BIGINT UNSIGNED AUTO_INCREMENT NOT NULL,
    email    VARCHAR(100) unique            NOT NULL,
    password VARCHAR(255)                   NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE if not exists CART_PRODUCT
(
    id         BIGINT UNSIGNED AUTO_INCREMENT NOT NULL,
    member_id  BIGINT UNSIGNED                NOT NULL,
    product_id BIGINT UNSIGNED                NOT NULL,
    FOREIGN KEY (member_id) REFERENCES MEMBER (id) on delete CASCADE,
    FOREIGN KEY (product_id) REFERENCES PRODUCT (id) on delete CASCADE
);
