package ir.ac.kntu.patogh.Utils;

public class DetailedBadge {

    private int imageId;
    private int id;
    private String badgeName;
    private int completionPercent;

    public DetailedBadge(){

    }
    public DetailedBadge( int imageId, String badgeName,int id) {
        this.imageId = imageId;
        this.id = id;
        this.badgeName = badgeName;
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

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public int getCompletionPercent() {
        return completionPercent;
    }

    public void setCompletionPercent(int completionPercent) {
        this.completionPercent = completionPercent;
    }
}
