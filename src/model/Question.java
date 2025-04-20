package model;

public class Question {
    private int id;
    private String content;
    private int points;
    private int testId;

    public Question() {}

    public Question(int id, String content, int points, int testId) {
        this.id = id;
        this.content = content;
        this.points = points;
        this.testId = testId;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }
}
