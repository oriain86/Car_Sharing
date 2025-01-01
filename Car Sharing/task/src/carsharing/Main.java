package carsharing;

import java.util.List;
import java.util.Scanner;



public class Main {

    public static void main(String[] args) {
        String dbName = "default";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-databaseFileName") && i + 1 < args.length) {
                dbName = args[i + 1];
                break;
            }
        }

        String dbUrl = "jdbc:h2:./src/carsharing/db/" + dbName;

        // Initialize DAOs
        CompanyDAO companyDAO = new CompanyDAO(dbUrl);
        CarDAO carDAO = new CarDAO(dbUrl);
        CustomerDAO customerDAO = new CustomerDAO(dbUrl);

        // Create tables
        companyDAO.createTable();
        carDAO.createTable();
        customerDAO.createTable();

        // Proceed with the main menu
        CompanyService companyService = new CompanyService(companyDAO);
        CarService carService = new CarService(carDAO);
        CustomerService customerService = new CustomerService(customerDAO);

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> managerMenu(scanner, companyService, carService);
                case 2 -> customerLoginMenu(scanner, customerService, carService, companyService);
                case 3 -> customerService.createCustomer(scanner);
                case 0 -> exit = true;
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }



    private static void managerMenu(Scanner scanner, CompanyService companyService, CarService carService) {

        boolean back = false;

        while (!back) {
            System.out.println("\n1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> companyListMenu(scanner, companyService, carService);
                case 2 -> companyService.createCompany(scanner);
                case 0 -> back = true;
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void companyListMenu(Scanner scanner, CompanyService companyService, CarService carService) {

        List<Company> companies = companyService.getAllCompanies();

        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println("Choose a company:");
            for (int i = 0; i < companies.size(); i++) {
                System.out.println((i + 1) + ". " + companies.get(i).getName());
            }
            System.out.println("0. Back");

            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= companies.size()) {
                Company company = companies.get(choice - 1);
                companyMenu(scanner, company, carService);
            }
        }
    }

    private static void companyMenu(Scanner scanner, Company company, CarService carService) {

        boolean back = false;

        while (!back) {
            System.out.println("\n'" + company.getName() + "' company");
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> printCarList(carService, company.getId());
                case 2 -> carService.createCar(scanner, company.getId());
                case 0 -> back = true;
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void printCarList(CarService carService, int companyId) {

        List<Car> cars = carService.getCarsByCompany(companyId);

        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            for (int i = 0; i < cars.size(); i++) {
                System.out.println((i + 1) + ". " + cars.get(i).getName());
            }
        }
    }

    private static void customerLoginMenu(Scanner scanner, CustomerService customerService,
                                          CarService carService, CompanyService companyService) {

        List<Customer> customers = customerService.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
        } else {
            System.out.println("Choose a customer:");
            for (int i = 0; i < customers.size(); i++) {
                System.out.println((i + 1) + ". " + customers.get(i).getName());
            }
            System.out.println("0. Back");

            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= customers.size()) {
                Customer customer = customers.get(choice - 1);
                customerMenu(scanner, customer, customerService, carService, companyService);
            }
        }
    }

    private static void customerMenu(Scanner scanner, Customer customer,
                                     CustomerService customerService,
                                     CarService carService, CompanyService companyService) {

        boolean back = false;

        while (!back) {
            System.out.println("\n1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> rentCar(scanner, customer, customerService, carService, companyService);
                case 2 -> returnCar(customer, customerService);
                case 3 -> displayRentedCar(customer, carService, companyService);
                case 0 -> back = true;
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void rentCar(Scanner scanner, Customer customer,
                                CustomerService customerService,
                                CarService carService, CompanyService companyService) {

        if (customer.getRentedCarId() != null) {
            System.out.println("You've already rented a car!");
            return;
        }

        List<Company> companies = companyService.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }

        System.out.println("Choose a company:");
        for (int i = 0; i < companies.size(); i++) {
            System.out.println((i + 1) + ". " + companies.get(i).getName());
        }
        System.out.println("0. Back");

        int choice = Integer.parseInt(scanner.nextLine());
        if (choice > 0 && choice <= companies.size()) {
            Company company = companies.get(choice - 1);
            List<Car> availableCars = carService.getCarsByCompany(company.getId());
            availableCars.removeIf(car -> carService.isCarRented(car.getId()));

            if (availableCars.isEmpty()) {
                System.out.println("No available cars in the '" + company.getName() + "' company.");
                return;
            }

            System.out.println("Choose a car:");
            for (int i = 0; i < availableCars.size(); i++) {
                System.out.println((i + 1) + ". " + availableCars.get(i).getName());
            }
            System.out.println("0. Back");

            int carChoice = Integer.parseInt(scanner.nextLine());
            if (carChoice > 0 && carChoice <= availableCars.size()) {
                Car car = availableCars.get(carChoice - 1);
                customerService.updateRentedCar(customer.getId(), car.getId());
                customer.setRentedCarId(car.getId()); // Update the in-memory Customer object
                System.out.println("You rented '" + car.getName() + "'");
            }
        }
    }

    private static void returnCar(Customer customer, CustomerService customerService) {

        if (customer.getRentedCarId() == null) {
            System.out.println("You didn't rent a car!");
        } else {
            customerService.updateRentedCar(customer.getId(), null);
            System.out.println("You've returned a rented car!");
        }
    }

    private static void displayRentedCar(Customer customer,
                                         CarService carService,
                                         CompanyService companyService) {

        if (customer.getRentedCarId() == null) {
            System.out.println("You didn't rent a car!");
            return;
        }

        Car car = carService.getCarById(customer.getRentedCarId());
        if (car == null) {
            System.out.println("Car details not found!");
            return;
        }

        Company company = null;
        for (Company c : companyService.getAllCompanies()) {
            if (c.getId() == car.getCompanyId()) {
                company = c;
                break;
            }
        }

        if (company == null) {
            System.out.println("Company details not found!");
        } else {
            System.out.println("Your rented car:");
            System.out.println(car.getName());
            System.out.println("Company:");
            System.out.println(company.getName());
        }
    }
}
