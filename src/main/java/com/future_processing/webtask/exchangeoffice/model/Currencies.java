package com.future_processing.webtask.exchangeoffice.model;

import java.util.Date;
import java.util.List;

public class Currencies {

    private Date publicationDate;
    private List<Currency> items;

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public List<Currency> getItems() {
        return items;
    }

    public void setItems(List<Currency> items) {
        this.items = items;
    }

}
