CREATE TABLE call_history
(
    id                  serial PRIMARY KEY,
    method_name       VARCHAR(255) NOT NULL,
    request_method      VARCHAR(10) NOT NULL,
    request_path        VARCHAR(255) NOT NULL,
    response_status     INT NOT NULL,
    response_body       TEXT,
    creation_datetime   TIMESTAMP DEFAULT NOW()
);