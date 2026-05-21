-- Notification Service Schema
ALTER SESSION SET CURRENT_SCHEMA = katm_notif;

CREATE SEQUENCE notifications_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE notifications (
    id               NUMBER(19) DEFAULT notifications_seq.NEXTVAL PRIMARY KEY,
    notification_type VARCHAR2(20) NOT NULL CHECK (notification_type IN ('EMAIL','SMS','PUSH')),
    status           VARCHAR2(20) NOT NULL DEFAULT 'PENDING'
                         CHECK (status IN ('PENDING','SENT','FAILED','CANCELLED')),
    recipient        VARCHAR2(255) NOT NULL,
    subject          VARCHAR2(500),
    message          CLOB          NOT NULL,
    retry_count      NUMBER(3) DEFAULT 0,
    max_retries      NUMBER(3) DEFAULT 3,
    error_message    VARCHAR2(2000),
    sent_at          TIMESTAMP,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by       VARCHAR2(100),
    reference_id     VARCHAR2(100),
    reference_type   VARCHAR2(50)
);

CREATE INDEX idx_notif_status ON notifications(status);
CREATE INDEX idx_notif_created_at ON notifications(created_at DESC);
CREATE INDEX idx_notif_reference ON notifications(reference_id, reference_type);
