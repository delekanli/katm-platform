-- Report Service Schema
ALTER SESSION SET CURRENT_SCHEMA = katm_report;

CREATE SEQUENCE reports_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE reports (
    id              NUMBER(19) DEFAULT reports_seq.NEXTVAL PRIMARY KEY,
    report_number   VARCHAR2(50)  NOT NULL UNIQUE,
    report_type     VARCHAR2(50)  NOT NULL,
    status          VARCHAR2(20)  NOT NULL DEFAULT 'REQUESTED'
                        CHECK (status IN ('REQUESTED','IN_PROGRESS','COMPLETED','FAILED')),
    period_from     DATE,
    period_to       DATE,
    file_key        VARCHAR2(500),
    file_name       VARCHAR2(255),
    file_size       NUMBER(19),
    requested_by    VARCHAR2(100),
    requested_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    generated_at    TIMESTAMP,
    error_message   VARCHAR2(2000),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_reports_status ON reports(status);
CREATE INDEX idx_reports_created_at ON reports(created_at DESC);
