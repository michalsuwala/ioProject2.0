public class Observer {
    private String name;
    private int id;

    public String getObserver(int id) {
        System.out.println("Getting observer with ID: " + id);
        return "";
    }

    public void addObserver(String name, int id) {
        System.out.println("Adding observer: " + name + " with ID: " + id);
    }
}
