CREATE TABLE packages(
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    description TEXT,
    price DECIMAL(10, 2),
    quota INT,
    duration INT,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
)