CREATE TABLE IF NOT EXISTS ted_talks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    author VARCHAR(255),
    date DATE,
    views BIGINT,
    likes BIGINT,
    link VARCHAR(512),
    influence_score BIGINT

);
