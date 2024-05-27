

import java.sql.*;
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
                System.out.println("4. Search");
                System.out.println("5. Exit");

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
                } else if (Objects.equals(input, "4")) {
                    System.out.println("[1] Species");
                    System.out.println("[2] Observers");
                    int pickedSearchOption = Integer.parseInt(scanner.nextLine());
                    search(pickedSearchOption);
                } else if (Objects.equals(input, "5")) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
            }

            conn.close();
            scanner.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void search(int option) {
        if (option == 1) {
            listSpecies();
        } else if (option == 2) {
            listObservers();
        }
    }

    public static void listSpecies() {
        Scanner scanner = new Scanner(System.in);
        String query = "SELECT species_id, name FROM Species ORDER BY species_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("List of all species:");
            while (rs.next()) {
                int id = rs.getInt("species_id");
                String name = rs.getString("name");
                System.out.println("ID: " + id + ", Name: " + name);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching species data: " + e.getMessage());
            return;
        }
        System.out.println("Choose an option:");
        System.out.println("1. Pick a species");
        System.out.println("2. Back to main menu");
        String searchinput = scanner.nextLine();
        if (Objects.equals(searchinput, "1")) {
            pickSpecies();
        } else if (Objects.equals(searchinput, "2")) {
            return;
        }
    }

    public static void pickSpecies() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the species ID:");
        try {
            int speciesId = Integer.parseInt(scanner.nextLine());
            Species s = Species.getSpecies(speciesId);
            if (s != null) {
                System.out.println("ID: " + s.id + ", Name: " + s.name + ", Description: " + s.description);
                while (true) {
                    System.out.println("Choose an option:");
                    System.out.println("1. List of individuals");
                    System.out.println("2. Back to main menu");
                    int inputInsideSpecies = Integer.parseInt(scanner.nextLine());
                    if (inputInsideSpecies == 1) {
                        listIndividuals(speciesId);
                    } else if (inputInsideSpecies == 2) {
                        return;
                    } else {
                        System.out.println("Invalid option. Please try again.");
                    }
                }
            } else {
                System.out.println("Species not found.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter a valid species ID.");
        }
    }

    public static void listIndividuals(int speciesId) {
        Scanner scanner = new Scanner(System.in);
        String individualQuery = "SELECT individual_id, description FROM Individual WHERE species_id = " + speciesId + " ORDER BY individual_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(individualQuery)) {
            System.out.println("List of all individuals:");
            while (rs.next()) {
                int individual_id = rs.getInt("individual_id");
                String desc = rs.getString("description");
                System.out.println("ID: " + individual_id + ", Desc: " + desc);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching individual data: " + e.getMessage());
        }
        System.out.println("Choose an option:");
        System.out.println("1. Back to previous menu");
        int input = Integer.parseInt(scanner.nextLine());
        if (input == 1) {
            return;
        } else if (input != 1) {
            System.out.println("Invalid option. Please try again.");
        }
    }

    public static void listObservers() {
        String query = "SELECT observer_id, name FROM Observer ORDER BY observer_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("List of all observers:");
            while (rs.next()) {
                int id = rs.getInt("observer_id");
                String name = rs.getString("name");
                System.out.println("ID: " + id + ", Name: " + name);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching observer data: " + e.getMessage());
        }
    }

}

