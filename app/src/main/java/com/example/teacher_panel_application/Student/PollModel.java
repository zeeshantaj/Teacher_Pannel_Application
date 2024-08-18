package com.example.teacher_panel_application.Student;

public class PollModel {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String selectedOption;
    private String pollId;
    private String uid,key;

    public PollModel(String question, String option1, String option2, String option3, String option4, String selectedOption, String pollId, String uid, String key) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.selectedOption = selectedOption;
        this.pollId = pollId;
        this.uid = uid;
        this.key = key;
    }

    public PollModel(String question, String option1, String option2, String option3, String option4, String selectedOption, String pollId) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.selectedOption = selectedOption;
        this.pollId = pollId;
    }

    public PollModel() {

    }

    public PollModel(String question, String option1, String option2, String option3, String option4, String selectedOption) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.selectedOption = selectedOption;
    }

    public PollModel(String question, String option1, String option2, String option3, String option4) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }
    public String getSelectedOption() { return selectedOption; }

    public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }
}
