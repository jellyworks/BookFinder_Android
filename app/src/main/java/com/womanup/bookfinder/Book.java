package com.womanup.bookfinder;

import java.io.Serializable;

public class Book implements Serializable {
    String title;
    String author;
    String publisher;
    int price;
    int salePrice;
    String translator;
    String thumbnail;
    String url;

    public Book(String title, String author, String publisher, int price, int salePrice, String translator, String thumbnail, String url) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.salePrice = salePrice;
        this.translator = translator;
        this.thumbnail = thumbnail;
        this.url = url;
    }
}
