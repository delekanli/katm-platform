-- Client Service Schema
ALTER SESSION SET CURRENT_SCHEMA = katm_client;

CREATE SEQUENCE clients_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE clients (
    id               NUMBER(19) DEFAULT clients_seq.NEXTVAL PRIMARY KEY,
    inn              VARCHAR2(9)    NOT NULL,
    pinfl            VARCHAR2(14),
    first_name       VARCHAR2(100)  NOT NULL,
    last_name        VARCHAR2(100)  NOT NULL,
    middle_name      VARCHAR2(100),
    full_name        VARCHAR2(300)  NOT NULL,
    client_type      VARCHAR2(20)   NOT NULL CHECK (client_type IN ('INDIVIDUAL','LEGAL_ENTITY')),
    status           VARCHAR2(30)   NOT NULL DEFAULT 'PENDING_VERIFICATION'
                         CHECK (status IN ('ACTIVE','INACTIVE','BLOCKED','PENDING_VERIFICATION')),
    phone            VARCHAR2(20),
    email            VARCHAR2(255),
    address          VARCHAR2(500),
    birth_date       DATE,
    passport_series  VARCHAR2(2),
    passport_number  VARCHAR2(7),
    passport_issued_date DATE,
    passport_issued_by   VARCHAR2(500),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at       TIMESTAMP,
    created_by       VARCHAR2(100),
    updated_by       VARCHAR2(100),
    is_deleted       NUMBER(1) DEFAULT 0 NOT NULL
);

CREATE UNIQUE INDEX idx_clients_inn ON clients(inn) WHERE is_deleted = 0;
CREATE INDEX idx_clients_pinfl ON clients(pinfl);
CREATE INDEX idx_clients_status ON clients(status);
CREATE INDEX idx_clients_created_at ON clients(created_at DESC);
