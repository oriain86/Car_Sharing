package carsharing;



import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private final String dbUrl;

    public CustomerDAO(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    // Create CUSTOMER table
    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS CUSTOMER (
                    ID INT PRIMARY KEY AUTO_INCREMENT,
                    NAME VARCHAR(255) UNIQUE NOT NULL,
                    RENTED_CAR_ID INT,
                    FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)
                );
                """;

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Error creating CUSTOMER table: " + e.getMessage());
        }
    }

    // Add a new customer
    public void addCustomer(String name) {
        String sql = "INSERT INTO CUSTOMER (NAME) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("The customer was added!");
        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
        }
    }

    // Retrieve all customers
    public List<Customer> getAllCustomers() {
        String sql = "SELECT * FROM CUSTOMER ORDER BY ID";
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                Integer rentedCarId = rs.getObject("RENTED_CAR_ID", Integer.class);
                customers.add(new Customer(id, name, rentedCarId));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving customers: " + e.getMessage());
        }

        return customers;
    }

    // Update rented car for a customer
    public void updateRentedCar(int customerId, Integer carId) {
        String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            pstmt.setObject(1, carId);
            pstmt.setInt(2, customerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating rented car: " + e.getMessage());
        }
    }
}
