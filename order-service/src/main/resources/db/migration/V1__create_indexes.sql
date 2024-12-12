-- 订单表索引
CREATE INDEX idx_order_no ON orders(order_no);
CREATE INDEX idx_user_id ON orders(user_id);
CREATE INDEX idx_status ON orders(status);
CREATE INDEX idx_create_time ON orders(create_time);

-- 订单项表索引
CREATE INDEX idx_order_id ON order_items(order_id);
CREATE INDEX idx_product_id ON order_items(product_id);

-- 库存表索引
CREATE INDEX idx_product_id ON inventory(product_id);
CREATE INDEX idx_quantity ON inventory(quantity);

-- 物流表索引
CREATE INDEX idx_tracking_no ON logistics(tracking_no);
CREATE INDEX idx_order_no ON logistics(order_no);
CREATE INDEX idx_status ON logistics(status); 