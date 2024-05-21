public class Species {
    private String name;
    private String description;

    public String getSpecies(String name) {
        System.out.println("Getting species: " + name);
        return ""; // Placeholder return value
    }

    public void addSpecies(String name, String description) {
        System.out.println("Adding species: " + name + " with description: " + description);
    }
}
