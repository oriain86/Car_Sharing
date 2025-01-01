package carsharing;

import java.util.List;
import java.util.Scanner;



public class CustomerService {
    private final CustomerDAO customerDAO;

    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void createCustomer(Scanner scanner) {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();

        if (name.trim().isEmpty()) {
            System.out.println("Customer name cannot be empty!");
            return;
        }

        customerDAO.addCustomer(name);
        System.out.println("The customer was added!");
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public void updateRentedCar(int customerId, Integer carId) {
        customerDAO.updateRentedCar(customerId, carId);
    }
}
