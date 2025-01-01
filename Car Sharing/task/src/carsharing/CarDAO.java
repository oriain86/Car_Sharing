package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class CarDAO {
    private final String dbUrl;

    public CarDAO(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    // Create the CAR table
    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS CAR (
                    ID INT PRIMARY KEY AUTO_INCREMENT,
                    NAME VARCHAR(255) UNIQUE NOT NULL,
                    COMPANY_ID INT NOT NULL,
                    FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)
                );
                """;

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Error creating CAR table: " + e.getMessage());
        }
    }

    // Add a car
    public void addCar(String name, int companyId) {
        String sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            pstmt.setString(1, name);
            pstmt.setInt(2, companyId);
            pstmt.executeUpdate();
            System.out.println("The car was added!");
        } catch (SQLException e) {
            System.err.println("Error adding car: " + e.getMessage());
        }
    }

    // Retrieve all cars for a specific company
    public List<Car> getCarsByCompany(int companyId) {
        String sql = "SELECT * FROM CAR WHERE COMPANY_ID = ? ORDER BY ID";
        List<Car> cars = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                int company = rs.getInt("COMPANY_ID");
                cars.add(new Car(id, name, company));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving cars: " + e.getMessage());
        }

        return cars;
    }

    // Check if a car is rented
    public boolean isCarRented(int carId) {
        String sql = "SELECT COUNT(*) AS RENTED_COUNT FROM CUSTOMER WHERE RENTED_CAR_ID = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, carId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("RENTED_COUNT") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if car is rented: " + e.getMessage());
        }

        return false;
    }

    // Retrieve car by ID
    public Car getCarById(int carId) {
        String sql = "SELECT * FROM CAR WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, carId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                int companyId = rs.getInt("COMPANY_ID");
                return new Car(id, name, companyId);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving car by ID: " + e.getMessage());
        }

        return null;
    }
}
