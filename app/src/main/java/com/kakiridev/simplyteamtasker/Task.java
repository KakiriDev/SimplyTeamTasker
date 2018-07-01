package com.kakiridev.simplyteamtasker;

public class Task {
    private String id;
    private String author;
    private String name;
    private String description;
    private String owner;
    private String status;

    public Task(){

    }

    public Task(String author, String name, String description, String owner, String status) {
        this.author = author;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.status = status;
    }

    public Task(String id, String author, String name, String description, String owner, String status) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
