package com.example.cryptoPipeline;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.cryptoPipeline.CoinGeckoPriceSource;
import com.example.cryptoPipeline.CryptoPrice;

public class CryptoPriceLogger {

    private static final Logger LOG = LoggerFactory.getLogger(CryptoPriceLogger.class);

    public static void main(String[] args) throws Exception {
        // Flink execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Add custom source for fetching Bitcoin prices
        // Only fetching USD price, other data options are disabled
        env.addSource(new CoinGeckoPriceSource("bitcoin", "usd", false, false, false, 10000L)) // Disabled market cap, volume, and 24hr change
           .addSink(new PriceLogSink());
        env.addSource(new CoinGeckoPriceSource("ethereum", "usd", false, false, false, 10000L)) // Disabled market cap, volume, and 24hr change
           .addSink(new PriceLogSink());

        // Execute the Flink job
        env.execute("Crypto Price Logger");
    }
}
