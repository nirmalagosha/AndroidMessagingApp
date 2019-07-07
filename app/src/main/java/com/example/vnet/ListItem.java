package com.example.vnet;

public class ListItem {
    private String title;

    private String description;
    private String author;
    private String url;
    private String urltoNews;

    public ListItem(String title, String description, String author, String url, String urltoNews) {
        this.title = title;

        this.description = description;
        this.author = author;
        this.url = url;
        this.urltoNews = urltoNews;
    }

    public void setUrltoNews(String urltoNews) {
        this.urltoNews = urltoNews;
    }

    public String getUrltoNews() {
        return urltoNews;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

