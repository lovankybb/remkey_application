CREATE TABLE orders_packages(
    order_id INT REFERENCES orders(id),
    package_id INT REFERENCES packages(id),
    PRIMARY KEY(order_id, package_id)
)