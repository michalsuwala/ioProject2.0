import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

public class Observation {
    private Individual individual;
    private int year;
    private Date date;
    private Observer observer;
    private Species species;

    public void addObservation(int observation_id, int species_id, int individual_id, int observer_id, int year_id, Date date) {
        try (Statement stmt = Main.conn.createStatement()) {
            // Check and add observer if not exists
            String checkIfObserverExists = String.format("SELECT EXISTS(SELECT * FROM database.Observer WHERE observer_id = %d) AS \"EXISTS\"", observer_id);
            ResultSet rsObserver = stmt.executeQuery(checkIfObserverExists);
            rsObserver.next();
            if (!rsObserver.getBoolean("EXISTS")) {
                String insertObserver = String.format("INSERT INTO database.Observer (observer_id, name) VALUES (%d, 'Unknown')", observer_id);
                stmt.executeUpdate(insertObserver);
                System.out.println("New observer added with ID: " + observer_id);
            }

            // Check and add species if not exists
            String checkIfSpeciesExists = String.format("SELECT EXISTS(SELECT * FROM database.Species WHERE species_id = %d) AS \"EXISTS\"", species_id);
            ResultSet rsSpecies = stmt.executeQuery(checkIfSpeciesExists);
            rsSpecies.next();
            if (!rsSpecies.getBoolean("EXISTS")) {
                String insertSpecies = String.format("INSERT INTO database.Species (species_id, name, description) VALUES (%d, 'Unknown', 'Unknown')", species_id);
                stmt.executeUpdate(insertSpecies);
                System.out.println("New species added with ID: " + species_id);
            }

            // Check and add individual if not exists
            String checkIfIndividualExists = String.format("SELECT EXISTS(SELECT * FROM database.Individual WHERE individual_id = %d) AS \"EXISTS\"", individual_id);
            ResultSet rsIndividual = stmt.executeQuery(checkIfIndividualExists);
            rsIndividual.next();
            if (!rsIndividual.getBoolean("EXISTS")) {
                String insertIndividual = String.format("INSERT INTO database.Individual (individual_id, description, species_id) VALUES (%d, 'Unknown', %d)", individual_id, species_id);
                stmt.executeUpdate(insertIndividual);
                System.out.println("New individual added with ID: " + individual_id);
            }

            // Check and add year if not exists
            String checkIfYearExists = String.format("SELECT EXISTS(SELECT * FROM database.Year WHERE year_id = %d) AS \"EXISTS\"", year_id);
            ResultSet rsYear = stmt.executeQuery(checkIfYearExists);
            rsYear.next();
            if (!rsYear.getBoolean("EXISTS")) {
                String insertYear = String.format("INSERT INTO database.Year (year_id, year_number, Species_species_id) VALUES (%d, 0, 1)", year_id);
                stmt.executeUpdate(insertYear);
                System.out.println("New year added with ID: " + year_id);
            }

            // Check if the observation already exists
            String checkIfObservationExists = String.format(
                    "SELECT EXISTS(SELECT * FROM database.Observation WHERE observation_id = %d) AS \"EXISTS\"", observation_id);
            ResultSet rsObservation = stmt.executeQuery(checkIfObservationExists);
            rsObservation.next();
            boolean observationExists = rsObservation.getBoolean("EXISTS");

            if (!observationExists) {
                // Insert the new observation into the database
                String insertObservationDataToDB = String.format(
                        "INSERT INTO database.Observation (observation_id, species_id, individual_id, observer_id, year_id, date) " +
                                "VALUES (%d, %d, %d, %d, %d, '%s')", observation_id, species_id, individual_id, observer_id, year_id, date);

                int affectedRows = stmt.executeUpdate(insertObservationDataToDB);
                if (affectedRows > 0) {
                    System.out.println("Observation has been added to the database.");
                } else {
                    System.out.println("Failed to add observation.");
                }
            } else {
                System.out.println("Observation already exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getObservation(int id) {
        String query = String.format("SELECT * FROM database.Observation WHERE observation_id = %d", id);
        try (Statement stmt = Main.conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                int observation_id = rs.getInt("observation_id");
                int species_id = rs.getInt("species_id");
                int individual_id = rs.getInt("individual_id");
                int observer_id = rs.getInt("observer_id");
                int year_id = rs.getInt("year_id");
                Date date = rs.getDate("date");

                // Fetch related details
                String observerName = getObserverName(observer_id);
                String speciesDetails = getSpeciesDetails(species_id);
                String individualDetails = getIndividualDetails(individual_id);

                return String.format("Observation ID: %d, Species: %s, Individual: %s, Observer: %s, Year ID: %d, Date: %s",
                        observation_id, speciesDetails, individualDetails, observerName, year_id, date.toString());
            } else {
                return "No observation found with ID: " + id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving observation: " + e.getMessage();
        }
    }

    // Helper methods to fetch related details
    private String getObserverName(int observer_id) {
        String query = String.format("SELECT name FROM database.Observer WHERE observer_id = %d", observer_id);
        try (Statement stmt = Main.conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("name");
            } else {
                return "Unknown Observer";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving observer: " + e.getMessage();
        }
    }

    private String getSpeciesDetails(int species_id) {
        String query = String.format("SELECT name, description FROM database.Species WHERE species_id = %d", species_id);
        try (Statement stmt = Main.conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("name") + " (" + rs.getString("description") + ")";
            } else {
                return "Unknown Species";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving species: " + e.getMessage();
        }
    }

    private String getIndividualDetails(int individual_id) {
        String query = String.format("SELECT description FROM database.Individual WHERE individual_id = %d", individual_id);
        try (Statement stmt = Main.conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("description");
            } else {
                return "Unknown Individual";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving individual: " + e.getMessage();
        }
    }
}

