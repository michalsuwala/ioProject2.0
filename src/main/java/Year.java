import java.util.ArrayList;
import java.util.List;

public class Year {
    private int number;
    private List<Observation> observations;
    private Species species;

    public String getYear(int number) {
        System.out.println("Getting year: " + number);
        return "";
    }

    public void addYear(int number, String speciesName) {
        System.out.println("Adding year: " + number + " with species: " + speciesName);
    }
}
