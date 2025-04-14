package Database;

import java.sql.*;

public class OptionManager {
    private final Statement st;

    public OptionManager(Connection con) throws SQLException {
        this.st = con.createStatement();
    }

    public int addOption(int questionId, String text) {
        try {
            String query = "INSERT INTO options (question_id, option_text) VALUES (" + questionId + ", '" + text + "')";
            return st.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Add option failed: " + e.getMessage());
            return 0;
        }
    }

    public ResultSet getOptions(int questionId) {
        try {
            return st.executeQuery("SELECT * FROM options WHERE question_id = " + questionId);
        } catch (SQLException e) {
            System.out.println("Fetch options failed: " + e.getMessage());
            return null;
        }
    }
}
