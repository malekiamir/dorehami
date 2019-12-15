package ir.ac.kntu.patogh.Utils;

public class Badge {

    private int imageId;
    private int id;

    public Badge(int imageId, int id) {
        this.imageId = imageId;
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
