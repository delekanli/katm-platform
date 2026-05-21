-- Claim Service Schema
ALTER SESSION SET CURRENT_SCHEMA = katm_claim;

CREATE SEQUENCE claims_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE claims (
    id               NUMBER(19) DEFAULT claims_seq.NEXTVAL PRIMARY KEY,
    claim_number     VARCHAR2(50)   NOT NULL UNIQUE,
    client_id        NUMBER(19)     NOT NULL,
    client_name      VARCHAR2(300),
    claim_type       VARCHAR2(50)   NOT NULL,
    status           VARCHAR2(30)   NOT NULL DEFAULT 'DRAFT'
                         CHECK (status IN ('DRAFT','SUBMITTED','UNDER_REVIEW','APPROVED','REJECTED','CANCELLED','PAID')),
    amount           NUMBER(18,2)   NOT NULL,
    currency         VARCHAR2(3)    DEFAULT 'UZS' NOT NULL,
    description      CLOB,
    documents        VARCHAR2(2000),
    incident_date    DATE,
    submission_date  DATE,
    resolution_date  DATE,
    assigned_to      VARCHAR2(100),
    rejection_reason VARCHAR2(2000),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at       TIMESTAMP,
    created_by       VARCHAR2(100),
    updated_by       VARCHAR2(100),
    is_deleted       NUMBER(1) DEFAULT 0 NOT NULL
);

CREATE INDEX idx_claims_client_id ON claims(client_id);
CREATE INDEX idx_claims_status ON claims(status);
CREATE INDEX idx_claims_created_at ON claims(created_at DESC);
CREATE INDEX idx_claims_claim_number ON claims(claim_number);

-- Claim documents table
CREATE SEQUENCE claim_docs_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE claim_documents (
    id           NUMBER(19) DEFAULT claim_docs_seq.NEXTVAL PRIMARY KEY,
    claim_id     NUMBER(19)    NOT NULL REFERENCES claims(id),
    file_name    VARCHAR2(255) NOT NULL,
    file_key     VARCHAR2(500) NOT NULL,
    file_size    NUMBER(19),
    content_type VARCHAR2(100),
    uploaded_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    uploaded_by  VARCHAR2(100)
);

CREATE INDEX idx_claim_docs_claim_id ON claim_documents(claim_id);
