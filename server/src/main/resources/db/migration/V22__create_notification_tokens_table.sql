CREATE TABLE notification_tokens(
    id SERIAL PRIMARY KEY,
    user_id TEXT NOT NULL REFERENCES users(id),
    token TEXT UNIQUE ,
    last_updated TIMESTAMPTZ DEFAULT NOW()
)