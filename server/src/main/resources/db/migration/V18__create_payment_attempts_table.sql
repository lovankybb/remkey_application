CREATE TABLE payment_attempts(
    id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(id),
    payment_status TEXT,
    payment_method TEXT,
    gate_way_txn_id TEXT,
    response_code TEXT,
    response_message TEXT,
    redirect_url TEXT,
    return_url TEXT,
    client_ip_address TEXT,
    request_payload TEXT,
    response_payload TEXT,
    amount DECIMAL(10, 2),
    currency TEXT,
    paid_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ

)