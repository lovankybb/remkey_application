CREATE TABLE orders(
    id SERIAL PRIMARY KEY,
    user_id TEXT NOT NULL REFERENCES users(id),
    transaction_date TIMESTAMPTZ NOT NULL,
    payment_status TEXT NOT NULL,
    payment_method TEXT,
    total_amount DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
)