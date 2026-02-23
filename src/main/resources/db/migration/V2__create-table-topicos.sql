CREATE TABLE IF NOT EXISTS topicos (
    id           BIGSERIAL    PRIMARY KEY,
    titulo       VARCHAR(255) NOT NULL,
    mensagem     TEXT         NOT NULL,
    data_criacao TIMESTAMP    NOT NULL,
    status       VARCHAR(50)  NOT NULL,
    autor        VARCHAR(255) NOT NULL,
    curso        VARCHAR(255) NOT NULL
);