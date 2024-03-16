package com.example.teacher_panel_application.Models;

import android.content.Intent;

import com.google.firebase.database.DatabaseReference;

public class UploadClassModel {
    private String Name,department,location,subject,topic,key,minutes,endDateTime,currentDateTime,dateTime;

    public UploadClassModel() {
    }

    public UploadClassModel(String name, String department, String location, String subject, String topic, String minutes, String dateTime) {
        Name = name;
        this.department = department;
        this.location = location;
        this.subject = subject;
        this.topic = topic;
        this.minutes = minutes;
        this.dateTime = dateTime;
    }

    public UploadClassModel(String name, String department, String location, String subject, String topic, String key, String minutes, String endDateTime, String currentDateTime) {

        Name = name;
        this.department = department;
        this.location = location;
        this.subject = subject;
        this.topic = topic;
        this.key = key;
        this.minutes = minutes;
        this.endDateTime = endDateTime;
        this.currentDateTime = currentDateTime;
    }

    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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
    public String minutesBuilder(String minutes){
        String[] parts = minutes.split("\\s+");
        int min = Integer.parseInt(parts[0]); // Assuming the first part is the number

        if (min < 60) {
            return min + " minute";
        } else {
            int hours = min / 60;
            int day = hours / 24;
            int remainingMinutes = min % 60;
            if (day > 0){
                return day + " day " + hours + " hour " + remainingMinutes + " min";
            }else {
                return hours + " hour " + remainingMinutes + " min";
            }

        }
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
}
