package model;

public class Test {
    private int id;
    private String title;
    private int courseId;
    private int professorId;
    private int timeLimit;
    private int maxAttempts;

    // Constructors
    public Test() {}

    public Test(int id, String title, int courseId, int professorId, int timeLimit, int maxAttempts) {
        this.id = id;
        this.title = title;
        this.courseId = courseId;
        this.professorId = professorId;
        this.timeLimit = timeLimit;
        this.maxAttempts = maxAttempts;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
}
