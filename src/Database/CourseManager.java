package Database;

import java.sql.*;
import static Database.Config.*;

public class CourseManager {
    private Connection con;
    private Statement st;

    public CourseManager() {
        try {
            Class.forName(DriverName);
            con = DriverManager.getConnection(databasUrl, USERNAME, PASSWORD);
            st = con.createStatement();
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    public int insertCourse(String title, String description, String level, int professorId) {
        try {
            String query = "INSERT INTO courses (title, description, level, professor_id) " +
                    "VALUES ('" + title + "', '" + description + "', '" + level + "', " + professorId + ")";
            return st.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Insert course failed: " + e.getMessage());
            return 0;
        }
    }

    public ResultSet getAllCourses() {
        try {
            return st.executeQuery("SELECT * FROM courses");
        } catch (SQLException e) {
            System.out.println("Fetch courses failed: " + e.getMessage());
            return null;
        }
    }

    public int updateCourse(int id, String title, String description, String level) {
        try {
            String query = "UPDATE courses SET title='" + title + "', description='" + description +
                    "', level='" + level + "' WHERE id=" + id;
            return st.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Update course failed: " + e.getMessage());
            return 0;
        }
    }

    public int deleteCourse(int id) {
        try {
            String query = "DELETE FROM courses WHERE id=" + id;
            return st.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Delete course failed: " + e.getMessage());
            return 0;
        }
    }
}
