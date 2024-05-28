import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Observer {
    private String name;
    private int id;

    public String getObserver(int id) {
        System.out.println("Getting observer with ID: " + id);
        return "";
    }

    public static void addObserver(String name) {
        String checkIfObserverExists = String.format("SELECT EXISTS(SELECT * FROM database.Observer WHERE name = '%s') AS \"EXISTS\"", name);
        try (Statement stmt = Main.conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkIfObserverExists)) {
                rs.next();
                boolean observerExists = rs.getBoolean("EXISTS");

                if(!observerExists) {
                    String insertObserverDataToDB = "INSERT INTO database.Observer (observer_id, name) SELECT MAX(observer_id) + 1, '"+ name +"' FROM database.Observer;";
                    int affectedRows = stmt.executeUpdate(insertObserverDataToDB);
                    if(affectedRows > 0) {
                        System.out.println("Observer has been added to database: " + name );
                    } else {
                        System.out.println("Failed to add observer: " + name);
                    }
                } else {
                    System.out.println("Observer already exists: " + name);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
