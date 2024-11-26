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

public class BitcoinPriceLogger {

    private static final Logger LOG = LoggerFactory.getLogger(BitcoinPriceLogger.class);

    public static void main(String[] args) throws Exception {
        // Flink execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Add custom source for fetching Bitcoin prices
        env.addSource(new BitcoinPriceSource())
           .addSink(new SinkFunction<BitcoinPrice>() {
                @Override
                public void invoke(BitcoinPrice value, Context context) {
                    LOG.info("Bitcoin price: {}", value);
                }
           });

        // Execute the Flink job
        env.execute("Bitcoin Price Logger");
    }

    // Custom SourceFunction to fetch Bitcoin prices
    public static class BitcoinPriceSource extends RichSourceFunction<BitcoinPrice> {
        private volatile boolean isRunning = true;
        private transient OkHttpClient client; // Mark the client as transient

        @Override
        public void open(org.apache.flink.configuration.Configuration parameters) {
            // Initialize OkHttpClient in open() method, executed on each task instance
            client = new OkHttpClient();
        }

        @Override
        public void run(SourceContext<BitcoinPrice> ctx) throws Exception {
            while (isRunning) {
                try {
                    // Fetch data from the API
                    String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful() && response.body() != null) {
                        // Extract and parse the JSON response
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        // Parse rates for USD, GBP, and EUR
                        double usdRate = jsonObject.getJSONObject("bpi").getJSONObject("USD").getDouble("rate_float");
                        double gbpRate = jsonObject.getJSONObject("bpi").getJSONObject("GBP").getDouble("rate_float");
                        double eurRate = jsonObject.getJSONObject("bpi").getJSONObject("EUR").getDouble("rate_float");

                        // Collect the parsed data into the Flink stream
                        ctx.collect(new BitcoinPrice(usdRate, gbpRate, eurRate));
                    }

                    // Wait for 2 seconds before the next fetch
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void cancel() {
            isRunning = false;
        }
    }
}
