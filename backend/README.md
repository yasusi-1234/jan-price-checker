# jan-price-checker

買取店価格チェック用のアプリケーションプロジェクトです。  
JANコード（バーコード）を読み取り、商品ごとの買取価格を店舗別に比較できる構成を目指します。

## 想定ユースケース
- iPhoneアプリでバーコード（JAN）を読み取り
- バックエンドAPI経由で商品情報・店舗別買取価格を取得
- 買取価格の降順ランキングで表示
- 将来的にAndroid対応
- 将来的にスクレイピングバッチで価格データを自動収集

## リポジトリ構成

```text
jan-price-checker/
├ frontend/   React Native + Expo
├ backend/    Spring Boot API
├ batch/      Python
├ db/         DDL / seed
├ docs/       設計メモ
└ README.md
```

## ディレクトリ責務
- `frontend/`: モバイルアプリ（バーコード読み取り・ランキング表示UI）
- `backend/`: JAN検索API、価格ランキングAPI
- `batch/`: 価格情報収集バッチ（1日1回実行想定）
- `db/`: テーブル定義、初期データ、マイグレーション方針
- `docs/`: 要件整理、画面/API/ER設計メモ

## 次の実装ステップ（提案）
1. `db/` に最小DDL（商品・店舗・買取価格履歴）を定義
2. `backend/` に `/products/{jan}/prices` API を作成
3. `frontend/` でカメラ読み取り＋API結果表示画面を作成
4. `batch/` でスクレイピングのモック収集処理を作成

## 確認したい事項
- DBは何を使う想定か（例: PostgreSQL / MySQL）
- バッチ実行基盤（cron / GitHub Actions / Cloud Scheduler など）
- スクレイピング対象サイトと利用規約の確認方針
- 初期対応範囲（PoCまでか、本番運用を見据えた構成までか）
