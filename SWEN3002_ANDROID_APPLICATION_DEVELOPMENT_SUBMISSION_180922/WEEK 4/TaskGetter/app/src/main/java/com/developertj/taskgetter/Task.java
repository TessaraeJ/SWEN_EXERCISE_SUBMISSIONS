package com.developertj.taskgetter;

public class Task {
    private String title;
    private String description;
    private Boolean done;
    private int id;


    public Task(String description, Boolean done, int id, String title) {
        this.description = description;
        this.done = done;
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getDone() {
        return done;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public void setId(int id) {
        this.id = id;
    }
}
