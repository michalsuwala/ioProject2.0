public class Observation {
    private Individual individual;
    private int year;
    private String date;
    private Observer observer;
    private Species species;

    public void addObservation(String observationData) {
        System.out.println("Adding observation: " + observationData);
    }
}
