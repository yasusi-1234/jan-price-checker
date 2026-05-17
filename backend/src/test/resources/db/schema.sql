-- PostgreSQL schema for JAN price checker

CREATE TABLE IF NOT EXISTS products (
  id BIGSERIAL PRIMARY KEY,
  jan_code VARCHAR(13) NOT NULL UNIQUE,
  name TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT products_jan_code_digits_chk CHECK (jan_code ~ '^[0-9]{8}([0-9]{5})?$')
)^^

CREATE TABLE IF NOT EXISTS shops (
  id BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL UNIQUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
)^^

-- Holds only the latest purchase price per product/shop pair.
CREATE TABLE IF NOT EXISTS purchase_prices (
  id BIGSERIAL PRIMARY KEY,
  product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
  shop_id BIGINT NOT NULL REFERENCES shops(id) ON DELETE CASCADE,
  price_yen INTEGER NOT NULL,
  fetched_at TIMESTAMPTZ NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT purchase_prices_unique_product_shop UNIQUE (product_id, shop_id),
  CONSTRAINT purchase_prices_price_non_negative_chk CHECK (price_yen >= 0)
)^^

-- Keeps all fetched price history records.
CREATE TABLE IF NOT EXISTS purchase_price_histories (
  id BIGSERIAL PRIMARY KEY,
  product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
  shop_id BIGINT NOT NULL REFERENCES shops(id) ON DELETE CASCADE,
  price_yen INTEGER NOT NULL,
  fetched_at TIMESTAMPTZ NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT purchase_price_histories_price_non_negative_chk CHECK (price_yen >= 0)
)^^

CREATE INDEX IF NOT EXISTS idx_purchase_prices_product_shop
  ON purchase_prices (product_id, shop_id)^^

CREATE INDEX IF NOT EXISTS idx_purchase_price_histories_product_shop_fetched_at
  ON purchase_price_histories (product_id, shop_id, fetched_at DESC)^^

CREATE INDEX IF NOT EXISTS idx_purchase_price_histories_fetched_at
  ON purchase_price_histories (fetched_at DESC)^^

-- Trigger function to maintain updated_at automatically.
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql^^

DROP TRIGGER IF EXISTS trg_products_set_updated_at ON products^^
CREATE TRIGGER trg_products_set_updated_at
BEFORE UPDATE ON products
FOR EACH ROW
EXECUTE FUNCTION set_updated_at()^^

DROP TRIGGER IF EXISTS trg_shops_set_updated_at ON shops^^
CREATE TRIGGER trg_shops_set_updated_at
BEFORE UPDATE ON shops
FOR EACH ROW
EXECUTE FUNCTION set_updated_at()^^

DROP TRIGGER IF EXISTS trg_purchase_prices_set_updated_at ON purchase_prices^^
CREATE TRIGGER trg_purchase_prices_set_updated_at
BEFORE UPDATE ON purchase_prices
FOR EACH ROW
EXECUTE FUNCTION set_updated_at()^^

DROP TRIGGER IF EXISTS trg_purchase_price_histories_set_updated_at ON purchase_price_histories^^
CREATE TRIGGER trg_purchase_price_histories_set_updated_at
BEFORE UPDATE ON purchase_price_histories
FOR EACH ROW
EXECUTE FUNCTION set_updated_at()^^
