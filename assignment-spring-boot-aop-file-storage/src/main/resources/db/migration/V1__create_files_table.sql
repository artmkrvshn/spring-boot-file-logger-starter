CREATE TABLE files
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    filename            VARCHAR(255) NOT NULL,
    extension           VARCHAR(10)  NOT NULL,
    content_type        VARCHAR(255) NOT NULL,
    content_disposition VARCHAR(255) NOT NULL,
    size                BIGINT       NOT NULL,
    content             BLOB         NOT NULL
);