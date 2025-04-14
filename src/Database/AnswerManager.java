package Database;

import java.sql.*;

public class AnswerManager {
    private final Statement st;

    public AnswerManager(Connection con) throws SQLException {
        this.st = con.createStatement();
    }

    public int addAnswer(int studentTestId, int questionId, int selectedOptionId) {
        try {
            String query = "INSERT INTO answers (student_test_id, question_id, selected_option_id) VALUES (" +
                    studentTestId + ", " + questionId + ", " + selectedOptionId + ")";
            return st.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Add answer failed: " + e.getMessage());
            return 0;
        }
    }
}
