package org.ieatta.activity.history;

import android.os.Parcel;
import android.os.Parcelable;

import org.ieatta.activity.PageTitle;

import java.util.Date;

public class HistoryEntry implements Parcelable {
    public static final int SOURCE_SEARCH = 1;
    public static final int SOURCE_INTERNAL_LINK = 2;
    public static final int SOURCE_EXTERNAL_LINK = 3;
    public static final int SOURCE_HISTORY = 4;
    public static final int SOURCE_SAVED_PAGE = 5;
    public static final int SOURCE_LANGUAGE_LINK = 6;
    public static final int SOURCE_RANDOM = 7;
    public static final int SOURCE_MAIN_PAGE = 8;
    public static final int SOURCE_NEARBY = 9;
    public static final int SOURCE_DISAMBIG = 10;

    public static final int SOURCE_NEARBY_RESTAURANTS = 11;
    public static final int SOURCE_RESTAURANT_DETAIL = 12;
    public static final int SOURCE_EVENT_DETAIL = 13;
    public static final int SOURCE_ORDERED_RECIPES = 14;
    public static final int SOURCE_RECIPE_DETAIL = 15;

    private final Date timestamp;
    private final int source;

    private final String restaurantUUID;
    private final String eventUUID;
    private final String teamUUID;
    private final String recipeUUID;

    public HistoryEntry(int source, String restaurantUUID, String eventUUID, String teamUUID, String recipeUUID) {
        this.source = source;
        this.restaurantUUID = restaurantUUID;
        this.eventUUID = eventUUID;
        this.teamUUID = teamUUID;
        this.recipeUUID = recipeUUID;

        this.timestamp = new Date();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getSource() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HistoryEntry)) {
            return false;
        }
        HistoryEntry other = (HistoryEntry) o;
        return timestamp.equals(other.timestamp)
                && source == other.source;
    }

    @Override
    public int hashCode() {
        int result = 123;
        result = 31 * result + source;
        result = 31 * result + timestamp.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "HistoryEntry{"
                + "source=" + source
                + ", timestamp=" + timestamp.getTime()
                + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getTimestamp().getTime());
        dest.writeInt(getSource());

        dest.writeString(this.restaurantUUID);
        dest.writeString(this.eventUUID);
        dest.writeString(this.teamUUID);
        dest.writeString(this.recipeUUID);
    }

    private HistoryEntry(Parcel in) {
        this.timestamp = new Date(in.readLong());
        this.source = in.readInt();

        this.restaurantUUID = in.readString();
        this.eventUUID = in.readString();
        this.teamUUID = in.readString();
        this.recipeUUID = in.readString();
    }

    public static final Creator<HistoryEntry> CREATOR
            = new Creator<HistoryEntry>() {
        public HistoryEntry createFromParcel(Parcel in) {
            return new HistoryEntry(in);
        }

        public HistoryEntry[] newArray(int size) {
            return new HistoryEntry[size];
        }
    };
}
