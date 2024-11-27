package com.example.cryptoPipeline;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

// Custom SourceFunction to fetch cryptocurrency prices
public class CoinGeckoPriceSource extends RichSourceFunction<Map<String, CryptoPrice>> {
    private static final Logger LOG = LoggerFactory.getLogger(CoinGeckoPriceSource.class); // Logger instance
    private volatile boolean isRunning = true;
    private transient OkHttpClient client;
    private String ids;
    private String vsCurrencies;
    private boolean includeMarketCap;
    private boolean includeVolume;
    private boolean include24HrChange;
    private long fetchInterval; // New field for fetch interval

    public CoinGeckoPriceSource(String ids, String vsCurrencies, boolean includeMarketCap, boolean includeVolume, boolean include24HrChange, long fetchInterval) {        
        super();
        this.ids = ids;
        this.vsCurrencies = vsCurrencies;
        this.includeMarketCap = includeMarketCap;
        this.includeVolume = includeVolume;
        this.include24HrChange = include24HrChange;
        this.fetchInterval = fetchInterval; // Initialize fetch interval
    }

    @Override
    public void open(org.apache.flink.configuration.Configuration parameters) {
        // Initialize OkHttpClient
        client = new OkHttpClient();
        LOG.info("CoinGeckoPriceSource opened for ids: {}", ids); // Log when the source is opened
    }

    @Override
    public void run(SourceContext<Map<String, CryptoPrice>> ctx) throws Exception {
        while (isRunning) {
            try {
                // Define the API request parameters

                // Construct the API URL
                String url = String.format(
                    "https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=%s&include_market_cap=%b&include_24hr_vol=%b&include_24hr_change=%b",
                    this.ids, this.vsCurrencies, this.includeMarketCap, this.includeVolume, this.include24HrChange
                );

                // Make the API request
                Request request = new Request.Builder().url(url).build();
                try (Response response = client.newCall(request).execute()) { // Use try-with-resources to ensure response is closed
                    if (response.isSuccessful() && response.body() != null) {
                        // Parse the JSON response
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        // Create a map to store the prices
                        Map<String, CryptoPrice> prices = new HashMap<>();

                        for (String coin : jsonObject.keySet()) {
                            JSONObject coinData = jsonObject.getJSONObject(coin);
                            double price = coinData.getDouble("usd");
                            double marketCap = coinData.optDouble("usd_market_cap", 0.0);
                            double volume = coinData.optDouble("usd_24h_vol", 0.0);
                            double change = coinData.optDouble("usd_24h_change", 0.0);

                            // Add the price data to the map
                            prices.put(coin, new CryptoPrice(coin, price, marketCap, volume, change));
                        }

                        // Emit the prices to the Flink stream
                        ctx.collect(prices);
                        // LOG.info("Fetched prices: {}", prices); // Log fetched prices
                    }
                } // Response will be closed automatically here

                // Wait for some before the next fetch
                Thread.sleep(fetchInterval); // Use configurable fetch interval
            } catch (Exception e) {
                LOG.error("Error while fetching cryptocurrency prices: ", e);
            }
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
        LOG.info("CoinGeckoPriceSource cancelled"); // Log when the source is cancelled
    }
}

// POJO for cryptocurrency price data
