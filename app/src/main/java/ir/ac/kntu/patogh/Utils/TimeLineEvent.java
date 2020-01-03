package ir.ac.kntu.patogh.Utils;

import ir.ac.kntu.patogh.Interfaces.TimeLine;

public class TimeLineEvent implements TimeLine {

    private String eventName;
    private String date;
    private String thumbnailId;

    public TimeLineEvent(String eventName, String date, String id) {
        this.eventName = eventName;
        this.date = date;
        this.thumbnailId = id;
    }

    public String getThumbnailId() {
        return thumbnailId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int getType() {
        return TimeLine.TYPE_EVENT;
    }
}
