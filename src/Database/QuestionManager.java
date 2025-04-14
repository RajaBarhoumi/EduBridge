package Database;

import java.sql.*;

public class QuestionManager {
    private final Statement st;

    public QuestionManager(Connection con) throws SQLException {
        this.st = con.createStatement();
    }

    public int addQuestion(int testId, String questionText, int point) {
        try {
            String query = "INSERT INTO questions (test_id, question_text, point) VALUES (" +
                    testId + ", '" + questionText + "', " + point + ")";
            return st.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Add question failed: " + e.getMessage());
            return 0;
        }
    }

    public int setCorrectOption(int questionId, int optionId) {
        try {
            return st.executeUpdate("UPDATE questions SET correct_option_id = " + optionId + " WHERE id = " + questionId);
        } catch (SQLException e) {
            System.out.println("Set correct option failed: " + e.getMessage());
            return 0;
        }
    }
}
