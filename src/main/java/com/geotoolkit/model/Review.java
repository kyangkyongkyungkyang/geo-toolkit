package com.geotoolkit.model;

/**
 * 고객 리뷰.
 * Schema.org Review 타입에 대응.
 * AI는 제3자 리뷰를 강하게 신뢰하므로 GEO에서 중요한 역할을 한다.
 */
public class Review {

    private String author;
    private String datePublished;
    private int rating;
    private String body;

    public Review() {}

    public Review(String author, String datePublished, int rating, String body) {
        this.author = author;
        this.datePublished = datePublished;
        this.rating = rating;
        this.body = body;
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDatePublished() { return datePublished; }
    public void setDatePublished(String datePublished) { this.datePublished = datePublished; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}
