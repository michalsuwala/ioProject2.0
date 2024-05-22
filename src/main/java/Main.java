import java.sql.*;

public class Main {

    // Dane do połączenia z bazą danych
    private static final String URL = "jdbc:mysql://mysql-20e7b509-sliwinski-69d4.k.aivencloud.com:24502/database?ssl-mode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_LVSPr5wWYAjnOI2XT_0";


    static Connection conn;

    // Konstruktor klasy
    public Main() throws SQLException {
        // Inicjowanie połączenia w konstruktorze
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) throws SQLException {
        try {
            // Tworzenie instancji klasy Main
            Main main = new Main();

            String query = "SELECT observer_id, name FROM Observer";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    int id = rs.getInt("observer_id");
                    String name = rs.getString("name");
                    System.out.println("ID: " + id + ", Name: " + name);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            // Obsługa błędów związanych z połączeniem z bazą danych
            e.printStackTrace();
        }
        //Observer.addObserver("testowy1");
        Species.addSpecies("test", "testopistest");
    }

    public void search() {
        System.out.println("Searching...");
    }

    public void showPopulationChanges() {
        while (true) {
            System.out.println("Choose an option");
            System.out.println("1. Add observation");
            // Tutaj możesz dodać więcej opcji
        }
    }
}
