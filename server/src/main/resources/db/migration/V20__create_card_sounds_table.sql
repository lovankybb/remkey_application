CREATE TABLE sounds(
    id SERIAL PRIMARY KEY,
    url TEXT NOT NULL UNIQUE,
    card_id INT REFERENCES cards(id),
    card_user_id INT REFERENCES cards_users(id)
)