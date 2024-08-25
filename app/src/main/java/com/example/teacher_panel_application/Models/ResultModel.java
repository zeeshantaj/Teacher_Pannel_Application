package com.example.teacher_panel_application.Models;

public class ResultModel {
    private String question;
    private String option0;
    private String option1;
    private String option2;
    private String option3,pollId;
    private Long option1_count,option2_count,option3_count,option4_count;

    public ResultModel() {
    }

    public ResultModel(String question, String option0, String option1, String option2, String option3, String pollId, Long option1_count, Long option2_count, Long option3_count, Long option4_count) {
        this.question = question;
        this.option0 = option0;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.pollId = pollId;
        this.option1_count = option1_count;
        this.option2_count = option2_count;
        this.option3_count = option3_count;
        this.option4_count = option4_count;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption0() {
        return option0;
    }

    public void setOption0(String option0) {
        this.option0 = option0;
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

    public Long getOption1_count() {
        if (option1_count == null){
            option1_count = 0L;
        }
        return option1_count;
    }

    public void setOption1_count(Long option1_count) {
        this.option1_count = option1_count;
    }

    public Long getOption2_count() {
        if (option2_count == null){
            option2_count = 0L;
        }
        return option2_count;
    }

    public void setOption2_count(Long option2_count) {
        this.option2_count = option2_count;
    }

    public Long getOption3_count() {
        if (option3_count == null){
            option3_count = 0L;
        }
        return option3_count;
    }

    public void setOption3_count(Long option3_count) {
        this.option3_count = option3_count;
    }

    public Long getOption4_count() {
        if (option4_count == null){
            option4_count = 0L;
        }
        return option4_count;
    }

    public void setOption4_count(Long option4_count) {
        this.option4_count = option4_count;
    }
}
