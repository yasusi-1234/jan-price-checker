# アーキテクチャメモ（初版）

## 全体構成
- Frontend（React Native + Expo）
- Backend（Spring Boot）
- Batch（Python）
- DB（商品・店舗・買取価格）

## データフロー
1. Batchが1日1回、店舗サイトから価格情報を取得
2. Backend APIがDBからJANコードに紐づく価格情報を取得
3. Frontendでバーコードスキャン後にAPI呼び出し
4. 店舗別価格を降順で表示

## APIイメージ
- `GET /products/{jan}/prices`
  - 入力: JANコード
  - 出力: 商品情報、店舗別買取価格（降順）

## 主要テーブルイメージ
- products（JAN, 商品名...）
- stores（店舗ID, 店舗名...）
- purchase_prices（商品ID, 店舗ID, 価格, 取得日時）
