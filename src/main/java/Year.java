import java.sql.*;

public class Year {
    private int year;
    private int year_id;

    private Species species;


    public String getYear(int number) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql-20e7b509-sliwinski-69d4.k.aivencloud.com:24502/database?ssl-mode=REQUIRED", "avnadmin", "AVNS_LVSPr5wWYAjnOI2XT_0")) {
            String query = "SELECT * FROM database.Year WHERE year = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, number);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int year_id = rs.getInt("year_id");
                    return "Year ID for year " + number + ": " + year_id;
                } else {
                    return "Year not found for number: " + number;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving year: " + e.getMessage();
        }
    }

    public void addYear(int number) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql-20e7b509-sliwinski-69d4.k.aivencloud.com:24502/database?ssl-mode=REQUIRED", "avnadmin", "AVNS_LVSPr5wWYAjnOI2XT_0");
             Statement stmt = conn.createStatement()) {
            // Check if the year already exists
            String checkIfYearExists = String.format("SELECT EXISTS(SELECT * FROM database.Year WHERE year = %d) AS \"EXISTS\"", number);
            ResultSet rsYear = stmt.executeQuery(checkIfYearExists);
            rsYear.next();
            boolean yearExists = rsYear.getBoolean("EXISTS");

            if (!yearExists) {
                int defaultSpeciesId = getDefaultSpeciesId(conn, stmt);
                if (defaultSpeciesId == -1) {
                    defaultSpeciesId = addDefaultSpecies(conn, stmt);
                }

                if (defaultSpeciesId != -1) {
                    String insertYearToDB = String.format("INSERT INTO database.Year (year, Species_species_id) VALUES (%d, %d)", number, defaultSpeciesId);
                    int affectedRows = stmt.executeUpdate(insertYearToDB);
                    if (affectedRows > 0) {
                        System.out.println("Year " + number + " has been added to the database.");
                    } else {
                        System.out.println("Failed to add year " + number + ".");
                    }
                } else {
                    System.out.println("Failed to add year " + number + " due to missing default species.");
                }
            } else {
                System.out.println("Year " + number + " already exists in the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getDefaultSpeciesId(Connection conn, Statement stmt) throws SQLException {
        // Check if a default species exists in the Species table
        String checkDefaultSpecies = "SELECT species_id FROM database.Species WHERE name = 'Unknown'";
        ResultSet rsSpecies = stmt.executeQuery(checkDefaultSpecies);
        if (rsSpecies.next()) {
            return rsSpecies.getInt("species_id");
        } else {
            return -1;
        }
    }

    private int addDefaultSpecies(Connection conn, Statement stmt) throws SQLException {
        // Add a new default species entry to the Species table
        String insertDefaultSpecies = "INSERT INTO database.Species (name, description) VALUES ('Unknown', 'Unknown')";
        int affectedRows = stmt.executeUpdate(insertDefaultSpecies, Statement.RETURN_GENERATED_KEYS);
        if (affectedRows > 0) {
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Return the ID of the newly added species
            }
        }
        return -1;
    }

    public void getYearsWithSpeciesCount(int species_id) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql-20e7b509-sliwinski-69d4.k.aivencloud.com:24502/database?ssl-mode=REQUIRED", "avnadmin", "AVNS_LVSPr5wWYAjnOI2XT_0");
             Statement stmt = conn.createStatement()) {
            // Query to retrieve years with the count of the specified species
            String query = String.format(
                    "SELECT year_number, COUNT(*) AS species_count FROM database.Year " +
                            "INNER JOIN database.Observation ON Year.year_id = Observation.year_id " +
                            "WHERE Observation.species_id = %d GROUP BY year_number", species_id);
            ResultSet rs = stmt.executeQuery(query);

            // Print the result
            System.out.println("Years with Count of Species " + species_id + ":");
            System.out.println("Year\tSpecies Count");
            while (rs.next()) {
                int year_number = rs.getInt("year_number");
                int species_count = rs.getInt("species_count");
                System.out.println(year_number + "\t" + species_count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}