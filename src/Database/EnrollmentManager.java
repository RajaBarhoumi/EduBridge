package Database;

import java.sql.*;

public class EnrollmentManager {
    private final Connection con;
    private final Statement st;

    public EnrollmentManager(Connection con) throws SQLException {
        this.con = con;
        this.st = con.createStatement();
    }

    public int enrollStudent(int studentId, int courseId) {
        try {
            String query = "INSERT INTO enrollments (student_id, course_id) VALUES (" + studentId + ", " + courseId + ")";
            return st.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Enrollment failed: " + e.getMessage());
            return 0;
        }
    }

    public ResultSet getCoursesByStudent(int studentId) {
        try {
            return st.executeQuery("SELECT * FROM enrollments WHERE student_id = " + studentId);
        } catch (SQLException e) {
            System.out.println("Fetch enrollment failed: " + e.getMessage());
            return null;
        }
    }
}
