

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.in;

public class Main {
    // Database connection instance
    public static Connection conn;

    public static void main(String[] args) {
        // Initialize the database connection
        try {
            final String URL = "jdbc:mysql://mysql-20e7b509-sliwinski-69d4.k.aivencloud.com:24502/database?ssl-mode=REQUIRED";
            final String USER = "avnadmin";
            final String PASSWORD = "AVNS_LVSPr5wWYAjnOI2XT_0";
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // Create an instance of the Observation class

            Observation observation = new Observation();
            Year year = new Year();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Choose an option");
                System.out.println("1. Add observation");
                System.out.println("2. Get observation");
                System.out.println("3. Get years with count of a species");
                System.out.println("4. Exit");

                String input = scanner.nextLine();
                if (Objects.equals(input, "1")) {
                    System.out.println("Enter observation id");
                    int observation_id = Integer.parseInt(scanner.nextLine());

                    System.out.println("Enter species id");
                    int species_id = Integer.parseInt(scanner.nextLine());

                    System.out.println("Enter individual id");
                    int individual_id = Integer.parseInt(scanner.nextLine());

                    System.out.println("Enter observer id");
                    int observer_id = Integer.parseInt(scanner.nextLine());

                    System.out.println("Enter year id");
                    int year_id = Integer.parseInt(scanner.nextLine());

                    System.out.println("Enter date (YYYY-MM-DD)");
                    Date date = Date.valueOf(scanner.nextLine());

                    observation.addObservation(observation_id, species_id, individual_id, observer_id, year_id, date);
                } else if (Objects.equals(input, "2")) {
                    System.out.println("Enter observation id");
                    int observation_id = Integer.parseInt(scanner.nextLine());

                    String result = observation.getObservation(observation_id);
                    System.out.println(result);
                } else if (Objects.equals(input, "3")) {
                    System.out.println("Enter species ID:");
                    int speciesId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    // Get years with count of the specified species
                    year.getYearsWithSpeciesCount(speciesId);
                }else if (Objects.equals(input, "4")) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
            }

            conn.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

