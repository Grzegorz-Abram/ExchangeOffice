package com.futureprocessing.webtask.exchangeoffice.model;

import java.io.Serializable;

public class WalletsId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String currency;

    public WalletsId() {
    }

    public WalletsId(String username, String currency) {
        this.username = username;
        this.currency = currency;
    }

    public String getUsername() {
        return username;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        WalletsId other = (WalletsId) obj;

        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }

        if (currency == null) {
            if (other.currency != null) {
                return false;
            }
        } else if (!currency.equals(other.currency)) {
            return false;
        }

        return true;
    }

}
