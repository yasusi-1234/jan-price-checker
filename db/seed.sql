-- Sample seed data for PostgreSQL schema

BEGIN;

INSERT INTO products (jan_code, name)
VALUES
  ('4901234567894', 'Sample Green Tea 500ml'),
  ('4512345678901', 'Sample Protein Bar Cocoa'),
  ('4987654321098', 'Sample Wireless Earbuds')
ON CONFLICT (jan_code) DO UPDATE
SET
  name = EXCLUDED.name,
  updated_at = NOW();

INSERT INTO shops (name)
VALUES
  ('Tokyo Recycle Mart'),
  ('Osaka Gadget Buyback'),
  ('Nagoya Book & Media Off')
ON CONFLICT (name) DO UPDATE
SET
  updated_at = NOW();

-- Full history records.
INSERT INTO purchase_price_histories (product_id, shop_id, price_yen, fetched_at)
SELECT p.id, s.id, x.price_yen, x.fetched_at
FROM (
  VALUES
    ('4901234567894', 'Tokyo Recycle Mart', 120, '2026-05-14 09:00:00+09'::timestamptz),
    ('4901234567894', 'Tokyo Recycle Mart', 130, '2026-05-16 09:00:00+09'::timestamptz),
    ('4901234567894', 'Osaka Gadget Buyback', 100, '2026-05-15 12:00:00+09'::timestamptz),
    ('4512345678901', 'Tokyo Recycle Mart', 80, '2026-05-15 10:30:00+09'::timestamptz),
    ('4512345678901', 'Nagoya Book & Media Off', 75, '2026-05-16 11:45:00+09'::timestamptz),
    ('4987654321098', 'Osaka Gadget Buyback', 2500, '2026-05-15 14:20:00+09'::timestamptz),
    ('4987654321098', 'Osaka Gadget Buyback', 2600, '2026-05-17 08:50:00+09'::timestamptz)
) AS x(jan_code, shop_name, price_yen, fetched_at)
JOIN products p ON p.jan_code = x.jan_code
JOIN shops s ON s.name = x.shop_name;

-- Latest per product/shop into purchase_prices.
INSERT INTO purchase_prices (product_id, shop_id, price_yen, fetched_at)
SELECT latest.product_id, latest.shop_id, latest.price_yen, latest.fetched_at
FROM (
  SELECT DISTINCT ON (h.product_id, h.shop_id)
    h.product_id,
    h.shop_id,
    h.price_yen,
    h.fetched_at
  FROM purchase_price_histories h
  ORDER BY h.product_id, h.shop_id, h.fetched_at DESC, h.id DESC
) AS latest
ON CONFLICT (product_id, shop_id) DO UPDATE
SET
  price_yen = EXCLUDED.price_yen,
  fetched_at = EXCLUDED.fetched_at,
  updated_at = NOW();

COMMIT;
