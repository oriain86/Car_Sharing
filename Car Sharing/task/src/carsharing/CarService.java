package carsharing;

import java.util.List;
import java.util.Scanner;



public class CarService {
    private final CarDAO carDAO;

    public CarService(CarDAO carDAO) {
        this.carDAO = carDAO;
    }

    public void createCar(Scanner scanner, int companyId) {
        System.out.println("Enter the car name:");
        String name = scanner.nextLine();

        if (name.trim().isEmpty()) {
            System.out.println("Car name cannot be empty!");
            return;
        }

        carDAO.addCar(name, companyId);
        System.out.println("The car was added!");
    }

    public Car getCarById(int carId) {
        return carDAO.getCarById(carId);
    }

    public List<Car> getCarsByCompany(int companyId) {
        return carDAO.getCarsByCompany(companyId);
    }

    public boolean isCarRented(int carId) {
        return carDAO.isCarRented(carId);
    }
}
