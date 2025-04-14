package Database;

import java.sql.*;

import static Database.Config.*;

public class UserManager {

    private Connection con = null;
    private Statement st = null;

    public UserManager() {
        // Load Driver
        try {
            Class.forName(DriverName);
            System.out.println("Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found: " + e.getMessage());
        }

        // Connect to database
        try {
            this.con = DriverManager.getConnection(databasUrl, USERNAME, PASSWORD);
            this.st = con.createStatement();
            System.out.println("Connection successful");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Insert new user
    public int insertUser(String name, String email, String password, String role) {
        int rowsAffected = 0;
        try {
            String query = "INSERT INTO users (name, email, password, role) VALUES ('" + name + "', '" + email + "', '" + password + "', '" + role + "')";
            rowsAffected = st.executeUpdate(query);
            System.out.println("User inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
        return rowsAffected;
    }

    // Fetch users by role (optional)
    public ResultSet getUsersByRole(String role) {
        try {
            String query = "SELECT * FROM users WHERE role = '" + role + "'";
            return st.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return null;
    }

    // Get user by email and password (for login)
    public ResultSet login(String email, String password, String role) {

        try {
            String query = "SELECT * FROM users WHERE email = ? AND password = ? AND role = ?";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, role);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
        }


        return null;
    }

    // Update user's name or password
    public int updateUser(int id, String newName, String newPassword) {
        int rowsAffected = 0;
        try {
            String query = "UPDATE users SET name = '" + newName + "', password = '" + newPassword + "' WHERE id = " + id;
            rowsAffected = st.executeUpdate(query);
            System.out.println("User updated successfully.");
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
        return rowsAffected;
    }

    // Delete a user
    public int deleteUser(int id) {
        int rowsAffected = 0;
        try {
            String query = "DELETE FROM users WHERE id = " + id;
            rowsAffected = st.executeUpdate(query);
            System.out.println("User deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
        return rowsAffected;
    }

    // Utility to print result set (like your fetchStudents)
    public void printUsers(ResultSet rs) {
        try {
            if (rs == null) return;
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();

            // Print headers
            for (int i = 1; i <= cols; i++) {
                System.out.print(meta.getColumnName(i) + "\t");
            }
            System.out.println();

            // Print data
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    System.out.print(rs.getObject(i) + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error printing users: " + e.getMessage());
        }
    }
}

