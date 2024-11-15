package com.example.demo.entity;

import jakarta.persistence.*;
import com.example.demo.listener.CurrencyEventListener;
import org.springframework.beans.factory.annotation.Configurable;

@Entity
@EntityListeners(CurrencyEventListener.class)
@Table(name = "currencies")
@Configurable
public class Currencies {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "symbol")
    private String symbol;

    public Currencies() {
    }

    public Currencies(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "Currencies{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
