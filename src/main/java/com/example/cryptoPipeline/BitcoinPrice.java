package com.example.cryptoPipeline;

public class BitcoinPrice {
    private double usdPrice;
    private double gbpPrice;
    private double eurPrice;

    // Default constructor
    public BitcoinPrice() {}

    // Parameterized constructor
    public BitcoinPrice(double usdPrice, double gbpPrice, double eurPrice) {
        this.usdPrice = usdPrice;
        this.gbpPrice = gbpPrice;
        this.eurPrice = eurPrice;
    }

    // Getters and setters
    public double getUsdPrice() {
        return usdPrice;
    }

    public void setUsdPrice(double usdPrice) {
        this.usdPrice = usdPrice;
    }

    public double getGbpPrice() {
        return gbpPrice;
    }

    public void setGbpPrice(double gbpPrice) {
        this.gbpPrice = gbpPrice;
    }

    public double getEurPrice() {
        return eurPrice;
    }

    public void setEurPrice(double eurPrice) {
        this.eurPrice = eurPrice;
    }

    @Override
    public String toString() {
        return "BitcoinPrice{" +
                "USD=" + usdPrice +
                ", GBP=" + gbpPrice +
                ", EUR=" + eurPrice +
                '}';
    }
} 