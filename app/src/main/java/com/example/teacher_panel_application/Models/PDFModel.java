package com.example.teacher_panel_application.Models;

public class PDFModel {
    private String teacherName,PDFName,identifierForPDF,PDFUrl,FCMToken,dateTime,year,semester,purpose;

    public PDFModel() {
    }

    public PDFModel(String teacherName, String PDFName, String identifierForPDF, String PDFUrl, String FCMToken, String dateTime, String year, String semester, String purpose) {
        this.teacherName = teacherName;
        this.PDFName = PDFName;
        this.identifierForPDF = identifierForPDF;
        this.PDFUrl = PDFUrl;
        this.FCMToken = FCMToken;
        this.dateTime = dateTime;
        this.year = year;
        this.semester = semester;
        this.purpose = purpose;
    }

    public PDFModel(String PDFName, String PDFUrl, String FCMToken, String dateTime, String year, String semester, String purpose) {
        this.PDFName = PDFName;
        this.PDFUrl = PDFUrl;
        this.FCMToken = FCMToken;
        this.dateTime = dateTime;
        this.year = year;
        this.semester = semester;
        this.purpose = purpose;
    }

    public PDFModel(String PDFName, String PDFUrl, String dateTime, String year, String semester, String purpose) {
        this.PDFName = PDFName;
        this.PDFUrl = PDFUrl;
        this.dateTime = dateTime;
        this.year = year;
        this.semester = semester;
        this.purpose = purpose;
    }

    public PDFModel(String teacherName, String PDFName, String PDFUrl, String FCMToken, String dateTime, String year, String semester, String purpose) {
        this.teacherName = teacherName;
        this.PDFName = PDFName;
        this.PDFUrl = PDFUrl;
        this.FCMToken = FCMToken;
        this.dateTime = dateTime;
        this.year = year;
        this.semester = semester;
        this.purpose = purpose;
    }

    public String getIdentifierForPDF() {
        return identifierForPDF;
    }

    public void setIdentifierForPDF(String identifierForPDF) {
        this.identifierForPDF = identifierForPDF;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }

    public String getPDFName() {
        return PDFName;
    }

    public void setPDFName(String PDFName) {
        this.PDFName = PDFName;
    }

    public String getPDFUrl() {
        return PDFUrl;
    }

    public void setPDFUrl(String PDFUrl) {
        this.PDFUrl = PDFUrl;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
