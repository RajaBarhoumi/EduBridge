package model;

import java.sql.Timestamp;

public class StudentTest {
    private int id;
    private int studentId;
    private int testId;
    private float score;
    private boolean passed;
    private boolean certificateGenerated;
    private Timestamp takenDate;

    // Constructors
    public StudentTest() {}

    public StudentTest(int id, int studentId, int testId, float score, boolean passed, boolean certificateGenerated, Timestamp takenDate) {
        this.id = id;
        this.studentId = studentId;
        this.testId = testId;
        this.score = score;
        this.passed = passed;
        this.certificateGenerated = certificateGenerated;
        this.takenDate = takenDate;
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

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public boolean isCertificateGenerated() {
        return certificateGenerated;
    }

    public void setCertificateGenerated(boolean certificateGenerated) {
        this.certificateGenerated = certificateGenerated;
    }

    public Timestamp getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(Timestamp takenDate) {
        this.takenDate = takenDate;
    }
}
