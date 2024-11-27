package com.example.cryptoPipeline;

public class CryptoPrice {
    private String coinId;
    private double price;
    private double marketCap;
    private double volume;
    private double change;

    public CryptoPrice(String coinId, double price, double marketCap, double volume, double change) {
        this.coinId = coinId;
        this.price = price;
        this.marketCap = marketCap;
        this.volume = volume;
        this.change = change;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    @Override
    public String toString() {
        return "CryptoPrice{" +
                "coinId='" + coinId + '\'' +
                ", price=" + price +
                ", marketCap=" + marketCap +
                ", volume=" + volume +
                ", change=" + change +
                '}';
    }
}
