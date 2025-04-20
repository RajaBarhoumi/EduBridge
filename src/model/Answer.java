package model;

public class Answer {
    private int id;
    private int studentId;
    private int questionId;
    private int selectedOptionId;

    public Answer() {}

    public Answer(int id, int studentId, int questionId, int selectedOptionId) {
        this.id = id;
        this.studentId = studentId;
        this.questionId = questionId;
        this.selectedOptionId = selectedOptionId;
    }

    // Getters and setters
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

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getSelectedOptionId() {
        return selectedOptionId;
    }

    public void setSelectedOptionId(int selectedOptionId) {
        this.selectedOptionId = selectedOptionId;
    }
}
