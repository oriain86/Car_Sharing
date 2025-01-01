package carsharing;

import java.util.List;
import java.util.Scanner;



public class CompanyService {
    private final CompanyDAO companyDAO;

    public CompanyService(CompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }

    public void createCompany(Scanner scanner) {
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();

        if (name.trim().isEmpty()) {
            System.out.println("Company name cannot be empty!");
            return;
        }

        companyDAO.addCompany(name);
        System.out.println("The company was created!");
    }

    public List<Company> getAllCompanies() {
        return companyDAO.getAllCompanies();
    }
}
