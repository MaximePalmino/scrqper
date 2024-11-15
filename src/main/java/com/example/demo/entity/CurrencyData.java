package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "currency_data")
public class CurrencyData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "currency_id")
    private String currencyId;

    @Column(name = "price")
    private float price;
    @Column(name = "market_cap")

    private float marketCap;
    @Column(name = "updated_at")

    private Date updatedAt;
    @Column(name = "source")

    private String source;
    @Column(name = "trust_factor")
    private float trustFactor;


    public CurrencyData() {

    }

    public CurrencyData(float price, float marketCap, Date updatedAt, String source, float trustFactor) {
        this.price = price;
        this.marketCap = marketCap;
        this.updatedAt = updatedAt;
        this.source = source;
        this.trustFactor = trustFactor;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(float marketCap) {
        this.marketCap = marketCap;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public float getTrustFactor() {
        return trustFactor;
    }

    public void setTrustFactor(float trustFactor) {
        this.trustFactor = trustFactor;
    }

    @Override
    public String toString() {
        return "CurrencyData{" +
                "currency_id=" + currencyId +
                ", price=" + price +
                ", marketCap=" + marketCap +
                ", updatedAt=" + updatedAt +
                ", source='" + source + '\'' +
                ", trustFactor=" + trustFactor +
                '}';
    }
}
