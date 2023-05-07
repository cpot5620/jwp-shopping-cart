CREATE TABLE IF NOT EXISTS `product`
(
    `id`         BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`       VARCHAR(20) NOT NULL,
    `price`      INTEGER     NOT NULL,
    `img_url`    TEXT        NOT NULL,
    `created_at` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `users`
(
    `id`         BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `email`      VARCHAR(255) NOT NULL,
    `password`   TEXT         NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `cart`
(
    `id`         BIGINT   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`    BIGINT   NOT NULL,
    `product_id` BIGINT   NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
)
