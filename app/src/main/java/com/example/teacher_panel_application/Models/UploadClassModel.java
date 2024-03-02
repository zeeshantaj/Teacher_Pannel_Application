package com.example.teacher_panel_application.Models;

public class UploadClassModel {
    private String Name,department,location,subject,topic,key,minutes,endDateTime,currentTime,endTime;

    public UploadClassModel() {
    }

    public UploadClassModel(String name, String department, String location, String subject, String topic, String minutes, String endDateTime) {
        Name = name;
        this.department = department;
        this.location = location;
        this.subject = subject;
        this.topic = topic;
        this.minutes = minutes;
        this.endDateTime = endDateTime;
    }

    public UploadClassModel(String name, String department, String location, String subject, String topic, String key, String minutes, String endDateTime, String currentTime, String endTime) {

        Name = name;
        this.department = department;
        this.location = location;
        this.subject = subject;
        this.topic = topic;
        this.key = key;
        this.minutes = minutes;
        this.endDateTime = endDateTime;
        this.currentTime = currentTime;
        this.endTime = endTime;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
