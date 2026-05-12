CREATE TABLE cards(
    id SERIAL PRIMARY KEY,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    language_id INT REFERENCES languages(id),
    topic_id INT REFERENCES topics(id),
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
)