package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "currency_data_history")
public class CurrencyDataHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "currency_id")
    private String currencyId;
    @Column(name = "price")
    private float price;
    @Column(name = "market_cap")
    private float marketCap;
    @Column(name = "timestamp")
    private Date timestamp;
    @Column(name = "source")
    private String source;
    @Column(name = "trust_factor")
    private float trustFactor;
    @Column(name = "created_at")
    private Date createdAt;

    public CurrencyDataHistory() {

    }

    public CurrencyDataHistory(String currencyId, float price, float marketCap, Date timestamp, String source, float trustFactor, Date createdAt) {
        this.currencyId = currencyId;
        this.price = price;
        this.marketCap = marketCap;
        this.timestamp = timestamp;
        this.source = source;
        this.trustFactor = trustFactor;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CurrencyDataHistory{" +
                "id=" + id +
                ", currencyId=" + currencyId +
                ", price=" + price +
                ", marketCap=" + marketCap +
                ", timestamp=" + timestamp +
                ", source='" + source + '\'' +
                ", trustFactor=" + trustFactor +
                ", createdAt=" + createdAt +
                '}';
    }
}
