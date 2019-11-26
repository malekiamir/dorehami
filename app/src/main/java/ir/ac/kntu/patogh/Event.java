package ir.ac.kntu.patogh;

public class Event {
    private String name;
    private String desc;
    private String date;
    private String capacity;

    public Event() {
    }

    public Event(String name, String desc, String date, String capacity) {
        this.name = name;
        this.desc = desc;
        this.date = date;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
