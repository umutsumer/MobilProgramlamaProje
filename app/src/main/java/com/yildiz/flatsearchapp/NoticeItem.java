package com.yildiz.flatsearchapp;

public class NoticeItem {
    String name;
    String timePassed;
    String content;

    public NoticeItem(String name, String timePassed, String content) {
        this.name = name;
        this.timePassed = timePassed;
        this.content = content;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimePassed() {
        return timePassed;
    }

    public void setTimePassed(String timePassed) {
        this.timePassed = timePassed;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
