CREATE TABLE card_images(
    id SERIAL PRIMARY KEY,
    url TEXT NOT NULL,
    public_id TEXT,
    card_id INT REFERENCES cards(id),
    card_user_id INT REFERENCES cards_users(id)
)