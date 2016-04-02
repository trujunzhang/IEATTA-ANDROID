package org.ieatta.activity.history;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import org.ieatta.provide.MainSegueIdentifier;
import org.wikipedia.util.StringUtil;

import java.util.Date;

public class HistoryEntry implements Parcelable {
    public static final HistoryEntryDatabaseTable DATABASE_TABLE = new HistoryEntryDatabaseTable();

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

    private final Date timestamp;
    public MainSegueIdentifier identifier;
    private final int source;

    private final String hPara;
    private final String vPara;

    private Location location;

    private boolean isNewModel;

    public HistoryEntry(MainSegueIdentifier identifier, String hPara, boolean isNewModel) {
        this(identifier, hPara, "");
        this.isNewModel = isNewModel;
    }

    public HistoryEntry(MainSegueIdentifier identifier,Location location) {
        this(identifier, "", "");
        this.location = location;
    }

    public HistoryEntry(MainSegueIdentifier identifier, String hPara, String vPara) {
        this.identifier = identifier;
        this.source = identifier.getType();
        this.hPara = hPara;
        this.vPara = vPara;

        this.timestamp = new Date();
    }

    public HistoryEntry(MainSegueIdentifier identifier) {
        this(identifier, "", "");
    }

    public HistoryEntry(MainSegueIdentifier identifier, String restaurantUUID) {
        this(identifier, restaurantUUID, "");
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

        dest.writeString(this.hPara);
        dest.writeString(this.vPara);
    }

    private HistoryEntry(Parcel in) {
        this.timestamp = new Date(in.readLong());
        this.source = in.readInt();

        this.hPara = in.readString();
        this.vPara = in.readString();
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

    public String getHPara() {
        return hPara;
    }

    public String getVPara() {
        return vPara;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isNewModel() {
        return isNewModel;
    }

    public void setIsNewModel(boolean isNewModel) {
        this.isNewModel = isNewModel;
    }
}
