package ir.ac.kntu.patogh.Utils;

public class FavoriteEvent {

    private String name;
    private String date;
    private String capacity;
    private String id;

    public FavoriteEvent() {
    }

    public FavoriteEvent(String name,String date, String capacity, String id) {
        this.name = name;
        this.date = date;
        this.capacity = capacity;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

}
