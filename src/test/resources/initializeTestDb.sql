DROP TABLE carts;
DROP TABLE items;
DROP TABLE users;

CREATE TABLE IF NOT EXISTS ITEMS
(
    id        BIGINT      NOT NULL AUTO_INCREMENT,
    name      VARCHAR(50) NOT NULL,
    image_url TEXT,
    price     INT         NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS USERS
(
    id       BIGINT      NOT NULL AUTO_INCREMENT,
    email    VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS CARTS
(
    id      BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES ITEMS (id) ON DELETE CASCADE
);

INSERT INTO items (name, image_url, price)
values ('위키드', 'https://image.yes24.com/themusical/upFiles/Themusical/Play/post_2013wicked.jpg',
        150000);
INSERT INTO items (name, image_url, price)
values ('마틸다', 'https://ticketimage.interpark.com/Play/image/large/22/22009226_p.gif', 100000);
INSERT INTO items (name, image_url, price)
values ('빌리 엘리어트', 'https://t1.daumcdn.net/cfile/226F4D4C544F42CF34', 200000);

INSERT INTO users (email, password)
values ('test1@gmail.com', 'test1pw1234');
INSERT INTO users (email, password)
values ('test2@naver.com', 'test2pw5678');
INSERT INTO users (email, password)
values ('test3@gmail.com', 'test3pw9090');


INSERT INTO carts (user_id, item_id)
values (1, 1);
INSERT INTO carts (user_id, item_id)
values (1, 2);
INSERT INTO carts (user_id, item_id)
values (2, 3);
