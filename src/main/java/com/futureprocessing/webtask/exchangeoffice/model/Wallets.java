package com.futureprocessing.webtask.exchangeoffice.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Transient;

@Entity
@IdClass(WalletsId.class)
public class Wallets {

    @Id
    private String username;
    @Id
    private String currency;
    private Integer amount;

    @Transient
    private Integer unit;
    @Transient
    private Double unitPrice;
    @Transient
    private Double value;

    public Wallets() {
    }

    public Wallets(String username, String currency, Integer amount) {
        this.username = username;
        this.currency = currency;
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

}
