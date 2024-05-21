import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Main {
    //database connection
    String url = "jdbc:mysql://localhost:3306/nazwa_bazy_danych";
    String user = "nazwa_użytkownika";
    String password = "hasło";

    try (Connection conn = DriverManager.getConnection(url, user, password)) {

    } catch (SQLException e) {
        // Obsługa błędów związanych z połączeniem z bazą danych
        e.printStackTrace();
    }

    private Species species;
    private Observation observation;
    private Year year;

    public Main() throws SQLException {}

    public void search() {
        System.out.println("Searching...");
    }

    public void showPopulationChanges() {
        while (true) {
            System.out.println("Choose an option");
            System.out.println("1. Add observation");
        }
    }
}
