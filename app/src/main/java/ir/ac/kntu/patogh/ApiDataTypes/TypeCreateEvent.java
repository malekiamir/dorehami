package ir.ac.kntu.patogh.ApiDataTypes;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class TypeCreateEvent {
    String name;
    String startTime;
    String endTime;
    String summery;
    String category;
    int size;
    boolean isPhysical;
    String latitude;
    String longitude;
    String province;
    String thumbnailId;
    String address;
    String description;
    String[] imagesIds;
    String[] tags;

    public TypeCreateEvent(String name, String startTime, String endTime, String summery
            , String category, int size, boolean isPhysical, String latitude, String longitude
            , String province, String thumbnailId, String address, String description
            , String[] imagesIds, String[] tags) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.summery = summery;
        this.category = category;
        this.size = size;
        this.isPhysical = isPhysical;
        this.latitude = latitude;
        this.longitude = longitude;
        this.province = province;
        this.thumbnailId = thumbnailId;
        this.address = address;
        this.description = description;
        this.imagesIds = imagesIds;
        this.tags = tags;
    }

    @NonNull
    @Override
    public String toString() {
        return "TypeCreateEvent{" +
                "name='" + name + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", summery='" + summery + '\'' +
                ", category='" + category + '\'' +
                ", size=" + size +
                ", isPhysical=" + isPhysical +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", province='" + province + '\'' +
                ", thumbnailId='" + thumbnailId + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", imagesIds=" + Arrays.toString(imagesIds) +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }
}
