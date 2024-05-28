import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Individual {
    public String getIndividual(int id) {
        System.out.println("Getting individual with ID: " + id);
        return "";
    }

    public static void addIndividual(int SpeciesId, String description) throws SQLException {
        String getMaxId = "SELECT MAX(individual_id) as \"MAX\" FROM database.Individual ";
        String checkIfSpeciesIdExists = String
                .format("SELECT EXISTS(SELECT * FROM database.Species WHERE species_id = %d) AS \"EXISTS\"", SpeciesId);
        try (Statement stmt = Main.conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(checkIfSpeciesIdExists);
            rs.next();
            boolean speciesExist = rs.getBoolean("EXISTS");
            if(speciesExist) {
                rs = stmt.executeQuery(getMaxId);
                rs.next();
                int maxIndividualId = rs.getInt("MAX");

                String insertIndividualDataToDB = String
                        .format("INSERT INTO database.Individual (individual_id, description, species_id) VALUES (%d, '%s', %d)", maxIndividualId + 1, description, SpeciesId);
                int affectedRows = stmt.executeUpdate(insertIndividualDataToDB);
                if (affectedRows > 0) {
                    System.out.println("Individual has been added to database");
                } else {
                    System.out.println("Failed to add individual to database");
                }
            } else {
                System.out.println("Spiecies with that ID doesn't exists");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
