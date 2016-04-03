package org.ieatta.activity;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.ieatta.database.provide.PQueryModelType;
import org.wikipedia.Site;

import java.util.Date;

import static org.wikipedia.util.StringUtil.capitalizeFirstChar;

public class PageTitle implements Parcelable{

    @Nullable private final String namespace;
    private final String text;
    private final String fragment;
    @Nullable private String thumbUrl;
    private final String description ;
    private String uuid;
    private Date createdAt;
    private PQueryModelType pmType = PQueryModelType.unkown;

    public PageTitle(String uuid,@Nullable String thumbUrl,@Nullable String description) {
        this.uuid = uuid;
        this.thumbUrl = thumbUrl;

        this.description = description != null ? capitalizeFirstChar(description) : null;

        this.namespace = "";
        this.text ="";
        this.fragment = "";
    }


    @Nullable
    public String getNamespace() {
        return namespace;
    }

    public String getText() {
        return text.replace(" ", "_");
    }

    public String getFragment() {
        return fragment;
    }

    @Nullable public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(@Nullable String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public String getDisplayText() {
        return getPrefixedText().replace("_", " ");
    }

    public String getPrefixedText() {
        return namespace == null ? getText() : namespace + ":" + getText();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<PageTitle> CREATOR
            = new Parcelable.Creator<PageTitle>() {
        public PageTitle createFromParcel(Parcel in) {
            return new PageTitle(in);
        }

        public PageTitle[] newArray(int size) {
            return new PageTitle[size];
        }
    };

    private PageTitle(Parcel in) {
        namespace = in.readString();
        text = in.readString();
        fragment = in.readString();
        thumbUrl = in.readString();
        description = in.readString();
        uuid = in.readString();
        setPmType(PQueryModelType.getInstance(in.readInt()));
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(namespace);
        parcel.writeString(text);
        parcel.writeString(fragment);
        parcel.writeString(thumbUrl);
        parcel.writeString(description);
        parcel.writeString(uuid);
        parcel.writeInt(getPmType().getType());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PageTitle)) {
            return false;
        }

        PageTitle other = (PageTitle)o;
        // Not using namespace directly since that can be null
        return other.getPrefixedText().equals(getPrefixedText());
    }

    @Override
    public int hashCode() {
        int result = getPrefixedText().hashCode();
        result = 31 * result ;
        return result;
    }

    @Override
    public String toString() {
        return getPrefixedText();
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public PQueryModelType getPmType() {
        return pmType;
    }

    public void setPmType(PQueryModelType pmType) {
        this.pmType = pmType;
    }
}
