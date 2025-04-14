package Database;

import java.sql.*;

import static Database.Config.*;

public class TestManager {
    private Connection con;
    private Statement st;

    public TestManager() {
        try {
            Class.forName(DriverName);
            con = DriverManager.getConnection(databasUrl, USERNAME, PASSWORD);
            st = con.createStatement();
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    public int createTest(String title, int courseId, int professorId) {
        try {
            String query = "INSERT INTO tests (title, course_id, professor_id) VALUES ('" + title + "', " + courseId + ", " + professorId + ")";
            return st.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Create test failed: " + e.getMessage());
            return 0;
        }
    }

    public ResultSet getTestsByCourse(int courseId) {
        try {
            return st.executeQuery("SELECT * FROM tests WHERE course_id = " + courseId);
        } catch (SQLException e) {
            System.out.println("Fetch failed: " + e.getMessage());
            return null;
        }
    }

    public int deleteTest(int testId) {
        try {
            return st.executeUpdate("DELETE FROM tests WHERE id = " + testId);
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
            return 0;
        }
    }
}
