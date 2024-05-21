import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;


public class Main {
    private Species species;
    private Observation observation;
    private Year year;

    public Main() {}

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
