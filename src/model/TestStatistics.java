package model;

import java.sql.Timestamp;

public class TestStatistics {
    private int id;
    private int testId;
    private int totalAttempts;
    private float averageScore;
    private float passingRate;
    private Timestamp lastUpdated;

    // Constructors
    public TestStatistics() {}

    public TestStatistics(int id, int testId, int totalAttempts, float averageScore, float passingRate, Timestamp lastUpdated) {
        this.id = id;
        this.testId = testId;
        this.totalAttempts = totalAttempts;
        this.averageScore = averageScore;
        this.passingRate = passingRate;
        this.lastUpdated = lastUpdated;
    }

    public TestStatistics(int testId, int totalAttempts, float averageScore, float passingRate) {
        this.testId = testId;
        this.totalAttempts = totalAttempts;
        this.averageScore = averageScore;
        this.passingRate = passingRate;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }

    public void setTotalAttempts(int totalAttempts) {
        this.totalAttempts = totalAttempts;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }

    public float getPassingRate() {
        return passingRate;
    }

    public void setPassingRate(float passingRate) {
        this.passingRate = passingRate;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
