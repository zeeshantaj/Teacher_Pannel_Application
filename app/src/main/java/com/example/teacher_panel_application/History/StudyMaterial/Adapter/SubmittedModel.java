package com.example.teacher_panel_application.History.StudyMaterial.Adapter;

public class SubmittedModel {
    // we have two classes and need to use all the field in the another class what the easy way

     private String userName,uid,pdfIdentifier,PDFName,imgUrl,PDFUrl,dateTime,year,semester,purpose;

    public SubmittedModel() {
    }

    public SubmittedModel(String userName, String uid, String pdfIdentifier, String PDFName, String imgUrl, String PDFUrl, String dateTime, String year, String semester, String purpose) {
        this.userName = userName;
        this.uid = uid;
        this.pdfIdentifier = pdfIdentifier;
        this.PDFName = PDFName;
        this.imgUrl = imgUrl;
        this.PDFUrl = PDFUrl;
        this.dateTime = dateTime;
        this.year = year;
        this.semester = semester;
        this.purpose = purpose;
    }

    public SubmittedModel(String userName, String uid, String PDFName, String imgUrl, String PDFUrl, String dateTime, String year, String semester, String purpose) {
        this.userName = userName;
        this.uid = uid;
        this.PDFName = PDFName;
        this.imgUrl = imgUrl;
        this.PDFUrl = PDFUrl;
        this.dateTime = dateTime;
        this.year = year;
        this.semester = semester;
        this.purpose = purpose;
    }

    public String getPdfIdentifier() {
        return pdfIdentifier;
    }

    public void setPdfIdentifier(String pdfIdentifier) {
        this.pdfIdentifier = pdfIdentifier;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPDFName() {
        return PDFName;
    }

    public void setPDFName(String PDFName) {
        this.PDFName = PDFName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
