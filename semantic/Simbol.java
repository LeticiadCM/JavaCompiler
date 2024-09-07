package semantic;

public class Simbol {
  
    private String name;
    private String type;
    private boolean initialized;
    private boolean used;

    public Simbol(String name, String type) {
        this.name = name;
        this.type = type;
        this.initialized = false;
        this.used = false;
    }

    public String getType() {
        return type;
    }

    public boolean wasInitialized() {
        return initialized;
    }

    public boolean wasUsed() {
        return used;
    }

    public void markAsInicialized() {
        this.initialized = true;
    }

    public void markAsUsed() {
        this.used = true;
    }
}
