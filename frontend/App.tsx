import React, { useState } from 'react';
import {
  ActivityIndicator,
  Button,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import { API_BASE_URL } from './src/config';

type Price = {
  shopName: string;
  priceYen: number;
  fetchedAt: string;
};

type ProductPricesResponse = {
  janCode: string;
  productName: string;
  prices: Price[];
};

export default function App() {
  const [janCode, setJanCode] = useState('');
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [result, setResult] = useState<ProductPricesResponse | null>(null);

  const handleSearch = async () => {
    if (!janCode.trim()) {
      setErrorMessage('JANコードを入力してください。');
      setResult(null);
      return;
    }

    setLoading(true);
    setErrorMessage(null);

    try {
      const response = await fetch(
        `${API_BASE_URL}/products/${encodeURIComponent(janCode.trim())}/prices`
      );

      if (!response.ok) {
        throw new Error(`APIエラー: ${response.status}`);
      }

      const data = (await response.json()) as ProductPricesResponse;
      setResult(data);
    } catch (error) {
      setResult(null);
      if (error instanceof Error) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage('不明なエラーが発生しました。');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.content}>
        <Text style={styles.title}>JAN価格検索</Text>

        <TextInput
          style={styles.input}
          placeholder="JANコードを入力"
          keyboardType="number-pad"
          value={janCode}
          onChangeText={setJanCode}
        />

        <Button title="検索" onPress={handleSearch} />

        {loading && <ActivityIndicator style={styles.loading} size="large" />}

        {errorMessage && <Text style={styles.error}>{errorMessage}</Text>}

        {result && (
          <View style={styles.resultArea}>
            <Text style={styles.productName}>{result.productName}</Text>
            <Text style={styles.subText}>JAN: {result.janCode}</Text>

            <Text style={styles.sectionTitle}>店舗ごとの価格</Text>
            {result.prices.length === 0 ? (
              <Text style={styles.subText}>価格情報がありません。</Text>
            ) : (
              result.prices.map((price, index) => (
                <View key={`${price.shopName}-${index}`} style={styles.priceCard}>
                  <Text>店舗名: {price.shopName}</Text>
                  <Text>価格: ¥{price.priceYen}</Text>
                  <Text>取得日時: {new Date(price.fetchedAt).toLocaleString()}</Text>
                </View>
              ))
            )}
          </View>
        )}
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  content: {
    padding: 16,
    gap: 12,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
  },
  input: {
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 10,
  },
  loading: {
    marginTop: 12,
  },
  error: {
    color: '#b00020',
    marginTop: 8,
  },
  resultArea: {
    marginTop: 16,
    gap: 8,
  },
  productName: {
    fontSize: 20,
    fontWeight: '600',
  },
  subText: {
    color: '#555',
  },
  sectionTitle: {
    marginTop: 8,
    fontSize: 16,
    fontWeight: '600',
  },
  priceCard: {
    borderWidth: 1,
    borderColor: '#e0e0e0',
    borderRadius: 8,
    padding: 10,
    gap: 4,
  },
});
