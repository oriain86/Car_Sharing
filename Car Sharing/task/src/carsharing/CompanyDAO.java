package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class CompanyDAO {
    private final String dbUrl;

    public CompanyDAO(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    // Create the COMPANY table
    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS COMPANY (
                    ID INT PRIMARY KEY AUTO_INCREMENT,
                    NAME VARCHAR(255) UNIQUE NOT NULL
                );
                """;

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Error creating COMPANY table: " + e.getMessage());
        }
    }

    // Add a new company
    public void addCompany(String name) {
        String sql = "INSERT INTO COMPANY (NAME) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("The company was created!");
        } catch (SQLException e) {
            System.err.println("Error adding company: " + e.getMessage());
        }
    }

    // Retrieve all companies
    public List<Company> getAllCompanies() {
        String sql = "SELECT * FROM COMPANY ORDER BY ID";
        List<Company> companies = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                companies.add(new Company(id, name));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving companies: " + e.getMessage());
        }

        return companies;
    }

    // Retrieve company by ID
    public Company getCompanyById(int companyId) {
        String sql = "SELECT * FROM COMPANY WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                return new Company(id, name);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving company by ID: " + e.getMessage());
        }

        return null;
    }
}
