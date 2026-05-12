CREATE TABLE card_reports
(
    id           SERIAL PRIMARY KEY,
    card_id      INT NOT NULL REFERENCES cards (id),
    message      TEXT NOT NULL,
    more_desc     TEXT,
    created_at TIMESTAMPTZ
)