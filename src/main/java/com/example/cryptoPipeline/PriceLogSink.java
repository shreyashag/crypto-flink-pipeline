package com.example.cryptoPipeline; 

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.cryptoPipeline.CryptoPrice;
import java.util.Map;

public class PriceLogSink extends RichSinkFunction<Map<String, CryptoPrice>> {
    private static final Logger LOG = LoggerFactory.getLogger(PriceLogSink.class);

    @Override
    public void invoke(Map<String, CryptoPrice> value, Context context) {
        LOG.info("Received value: {}", value);
        for (Map.Entry<String, CryptoPrice> entry : value.entrySet()) {
            CryptoPrice priceData = entry.getValue();
            if (priceData != null) {
                LOG.info("Coin ID: {}, Price: {}, Market Cap: {}, Volume: {}, Change: {}", 
                    priceData.getCoinId(), priceData.getPrice(), priceData.getMarketCap(), priceData.getVolume(), priceData.getChange());
            } else {
                LOG.warn("Price data is null for coin ID: {}", entry.getKey());
            }
        }
    }
}
