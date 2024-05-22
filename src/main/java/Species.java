import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Species {
    private String name;
    private String description;

    public String getSpecies(String name) {
        System.out.println("Getting species: " + name);
        return ""; // Placeholder return value
    }

    public static void addSpecies(String name, String description) {
        String checkIfSpeciesExists = String.format("SELECT EXISTS(SELECT * FROM database.Species WHERE name = '%s') AS \"EXISTS\"", name);
        try (Statement stmt = Main.conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(checkIfSpeciesExists);
            rs.next(); //przesunięcie na pierwszy wiersz
            boolean speciesExists = rs.getBoolean("EXISTS");

            if(!speciesExists) {
                String insertSpeciesDataToDB = String.format("INSERT INTO database.Species (species_id, name, description) SELECT MAX(species_id) + 1, '%s', '%s' FROM database.Species;", name, description);
                int affectedRows = stmt.executeUpdate(insertSpeciesDataToDB);
                if(affectedRows > 0) {
                    System.out.println("Species has been added to database: " + name );
                } else {
                    System.out.println("Failed to add species: " + name);
                }

            } else {
                System.out.println("Species already exists: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
