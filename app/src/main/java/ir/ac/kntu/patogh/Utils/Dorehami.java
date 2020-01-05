package ir.ac.kntu.patogh.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Dorehami {
    private String id;
    private String name;
    private String creatorId;
    private String startTime;
    private String endTime;
    private int size;
    private int remainingSize;
    private String description;
    private String summery;
    private boolean isPhysical;
    private String latitude;
    private String longitude;
    private String address;
    private String category;
    private String province;
    private String thumbnailId;
    private boolean isJoined;
    private boolean isFavorited;
    private String[] tags;
    private String[] imagesIds;

    public int getRemainingSize() {
        return remainingSize;
    }

    public void setRemainingSize(int remainingSize) {
        this.remainingSize = remainingSize;
    }

    public String[] getTags() {
        return tags;
    }

    public String getCategory() {
        return category;
    }

    public String[] getImagesIds() {
        return imagesIds;
    }

    public String getSummery() {
        return summery;
    }

    public String getProvince() {
        return province;
    }

    public String getThumbnailId() {
        return thumbnailId;
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

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getAddress() {
        return address;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPhysical() {
        return isPhysical;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public boolean isJoined() {
        return isJoined;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    @Override
    public String toString() {
        return "Dorehami{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", size=" + size +
                ", remainingSize=" + remainingSize +
                ", description='" + description + '\'' +
                ", summery='" + summery + '\'' +
                ", isPhysical=" + isPhysical +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", address='" + address + '\'' +
                ", category='" + category + '\'' +
                ", province='" + province + '\'' +
                ", thumbnailId='" + thumbnailId + '\'' +
                ", isJoined=" + isJoined +
                ", isFavorited=" + isFavorited +
                ", tags=" + Arrays.toString(tags) +
                ", imagesIds=" + Arrays.toString(imagesIds) +
                '}';
    }
}
