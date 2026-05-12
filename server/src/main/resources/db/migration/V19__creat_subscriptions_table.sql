CREATE TABLE subscriptions(
    id SERIAL PRIMARY KEY,
    user_id TEXT REFERENCES users(id),
    package_id INT REFERENCES packages(id),
    quota INT,
    used INT,
    expired_time TIMESTAMPTZ,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
)