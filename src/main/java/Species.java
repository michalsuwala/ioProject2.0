import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Species {
    int id;
    String name;
    String description;

    public static Species getSpecies(int id) {
        Species species = new Species();
        String getSpeciesSQL = "SELECT * FROM Species WHERE species_id = " + id ;
        try (Statement stmt = Main.conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(getSpeciesSQL);
            rs.next();
            int speciesId = rs.getInt("species_id");
            String speciesName = rs.getString("name");
            String description = rs.getString("description");
            species.id = speciesId;
            species.name = speciesName;
            species.description = description;
            return species;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return species;
    }

    public static void addSpecies(String name, String description) {
        String checkIfSpeciesExists = String
                .format("SELECT EXISTS(SELECT * FROM database.Species WHERE name = '%s') AS \"EXISTS\"", name);
        try (Statement stmt = Main.conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(checkIfSpeciesExists);
            rs.next();
            boolean speciesExists = rs.getBoolean("EXISTS");

            if(!speciesExists) {
                String insertSpeciesDataToDB = String
                        .format("INSERT INTO database.Species (species_id, name, description) SELECT MAX(species_id) + 1, '%s', '%s' FROM database.Species;", name, description);
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
