package ir.ac.kntu.patogh.ApiDataTypes;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class TypeSearchEvent {
    private String name;
    private String category;
    private String province;
    private String[] tags;

    public TypeSearchEvent(String name, String category, String province, String[] tags) {
        this.name = name;
        this.category = category;
        this.province = province;
        this.tags = tags;
    }

    @NonNull
    @Override
    public String toString() {
        return "TypeCreateEvent{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", province='" + province + '\'' +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }
}
