package model;

import java.util.Date;

public class Certificate {
    private int id;
    private int studentId;
    private int testId;
    private Date issueDate;
    private boolean isValid;
    private double score;

    // Constructors
    public Certificate() {}

    public Certificate(int id, int studentId, int testId, Date issueDate, boolean isValid, double score) {
        this.id = id;
        this.studentId = studentId;
        this.testId = testId;
        this.issueDate = issueDate;
        this.isValid = isValid;
        this.score = score;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
