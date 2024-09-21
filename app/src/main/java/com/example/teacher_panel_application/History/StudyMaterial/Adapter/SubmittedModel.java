package com.example.teacher_panel_application.History.StudyMaterial.Adapter;

public class SubmittedModel {
    // we have two classes and need to use all the field in the another class what the easy way

     private String userName,uid,pdfIdentifier,PDFName,imgUrl,PDFUrl,StudentFCMTOKEN,dateTime,year,semester,purpose,key,fromMark,outOfMark,remark;
     private boolean checked;
    public SubmittedModel() {
    }

    public SubmittedModel(String userName, String uid, String pdfIdentifier, String PDFName, String imgUrl, String PDFUrl,
                          String dateTime, String year, String semester, String purpose, String key, String fromMark, String outOfMark, String remark, boolean checked) {
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
        this.key = key;
        this.fromMark = fromMark;
        this.outOfMark = outOfMark;
        this.remark = remark;
        this.checked = checked;
    }

    public String getStudentFCMTOKEN() {
        return StudentFCMTOKEN;
    }

    public void setStudentFCMTOKEN(String studentFCMTOKEN) {
        StudentFCMTOKEN = studentFCMTOKEN;
    }

    public String getFromMark() {
        return fromMark;
    }

    public void setFromMark(String fromMark) {
        this.fromMark = fromMark;
    }

    public String getOutOfMark() {
        return outOfMark;
    }

    public void setOutOfMark(String outOfMark) {
        this.outOfMark = outOfMark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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
