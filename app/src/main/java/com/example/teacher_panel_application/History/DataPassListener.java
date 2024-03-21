package com.example.teacher_panel_application.History;

public interface DataPassListener {
    void getTextData(String title,String des,String currentDate,String dueDate,String key);
    void getImageData(String imageUrl,String currentDate,String dueDate,String key);
}
