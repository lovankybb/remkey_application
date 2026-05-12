CREATE TABLE cards_users(
    id SERIAL PRIMARY KEY,
    user_id TEXT NOT NULL REFERENCES users(id),
    question TEXT,
    answer TEXT ,
    language_id INT REFERENCES languages(id),
    topic_id INT REFERENCES topics(id),
    stability DECIMAL(10, 6),
    difficulty DECIMAL(10, 6),
    retrievability DECIMAL(10, 6),
    last_review TIMESTAMPTZ,
    next_review TIMESTAMPTZ,
    notification_time TIMESTAMPTZ,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
)

