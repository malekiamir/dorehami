package ir.ac.kntu.patogh.Utils;

import java.util.Arrays;

public class Dorehami {
    private String id;
    private String name;
    private String creatorId;
    private String startTime;
    private String endTime;
    private int size;
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
    private String[] imagesIds;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getImagesIds() {
        return imagesIds;
    }

    public void setImagesIds(String[] imagesIds) {
        this.imagesIds = imagesIds;
    }

    public String getSummery() {
        return summery;
    }

    public void setSummery(String summery) {
        this.summery = summery;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(String thumbnailId) {
        this.thumbnailId = thumbnailId;
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

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPhysical() {
        return isPhysical;
    }

    public void setPhysical(boolean physical) {
        isPhysical = physical;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isJoined() {
        return isJoined;
    }

    public void setJoined(boolean joined) {
        isJoined = joined;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
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
                ", imagesIds=" + Arrays.toString(imagesIds) +
                '}';
    }
}
