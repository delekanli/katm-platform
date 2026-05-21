-- Contract Service Schema
ALTER SESSION SET CURRENT_SCHEMA = katm_contract;

CREATE SEQUENCE contracts_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE contracts (
    id               NUMBER(19) DEFAULT contracts_seq.NEXTVAL PRIMARY KEY,
    contract_number  VARCHAR2(50)   NOT NULL UNIQUE,
    client_id        NUMBER(19)     NOT NULL,
    client_name      VARCHAR2(300),
    claim_id         NUMBER(19),
    contract_type    VARCHAR2(30)   NOT NULL
                         CHECK (contract_type IN ('LIFE','PROPERTY','HEALTH','AUTO','LIABILITY','TRAVEL')),
    status           VARCHAR2(30)   NOT NULL DEFAULT 'DRAFT'
                         CHECK (status IN ('DRAFT','PENDING_SIGNATURE','ACTIVE','EXPIRED','CANCELLED','SUSPENDED')),
    insured_amount   NUMBER(18,2)   NOT NULL,
    premium          NUMBER(18,2)   NOT NULL,
    currency         VARCHAR2(3)    DEFAULT 'UZS',
    start_date       DATE,
    end_date         DATE,
    document_key     VARCHAR2(500),
    signed_at        TIMESTAMP,
    signed_by        VARCHAR2(100),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at       TIMESTAMP,
    created_by       VARCHAR2(100),
    updated_by       VARCHAR2(100),
    is_deleted       NUMBER(1) DEFAULT 0 NOT NULL
);

CREATE INDEX idx_contracts_client_id ON contracts(client_id);
CREATE INDEX idx_contracts_status ON contracts(status);
CREATE INDEX idx_contracts_end_date ON contracts(end_date);
CREATE INDEX idx_contracts_created_at ON contracts(created_at DESC);
