package com.example.guoli.myandroid;

/**
 * Created by Guoli on 2020/6/13.
 */
public class Comment {
    private int id;
    private String title;
    private String author;
    private String detail;
    private String createTime;

    public Comment(int id, String title, String author, String detail, String createTime) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.detail = detail;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDetail() {
        return detail;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
