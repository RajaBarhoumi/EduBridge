package Database;

import java.sql.*;

import static Database.Config.*;

public class StudentTestManager {
    private Connection con;
    private Statement st;

    public StudentTestManager() {
        try {
            Class.forName(DriverName);
            con = DriverManager.getConnection(databasUrl, USERNAME, PASSWORD);
            st = con.createStatement();
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    public int recordTestResult(int studentId, int testId, float score) {
        boolean passed = score >= 80;
        try {
            String query = "INSERT INTO student_tests (student_id, test_id, score, passed, certificate_generated) " +
                    "VALUES (" + studentId + ", " + testId + ", " + score + ", " + passed + ", " + passed + ")";
            return st.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return 0;
        }
    }

    public ResultSet getResultsByStudent(int studentId) {
        try {
            return st.executeQuery("SELECT * FROM student_tests WHERE student_id = " + studentId);
        } catch (SQLException e) {
            System.out.println("Fetch failed: " + e.getMessage());
            return null;
        }
    }

    public int updateCertificateStatus(int studentTestId, boolean generated) {
        try {
            return st.executeUpdate("UPDATE student_tests SET certificate_generated = " + generated + " WHERE id = " + studentTestId);
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return 0;
        }
    }
}
