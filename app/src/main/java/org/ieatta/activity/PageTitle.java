package org.ieatta.activity;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.wikipedia.Site;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;

import static org.wikipedia.util.StringUtil.capitalizeFirstChar;
import static org.wikipedia.util.StringUtil.md5string;
import static org.wikipedia.util.UriUtil.decodeURL;

public class PageTitle implements Parcelable{

    @Nullable private final String namespace;
    private final String text;
    private final String fragment;
    @Nullable private String thumbUrl;
    private String description = null;
    private String uuid;
    private Date createdAt;

    public PageTitle(@Nullable final String namespace, final String text, final String fragment, @Nullable final String thumbUrl) {
        this.namespace = namespace;
        this.text = text;
        this.fragment = fragment;
        this.thumbUrl = thumbUrl;
    }

    public PageTitle(final String text, final Site site, @Nullable final String thumbUrl, final String description) {
        this(text, site, thumbUrl);
        this.description = description;
    }

    public PageTitle(String uuid,String thumbUrl) {
        this.uuid = uuid;
        this.thumbUrl = thumbUrl;

        this.namespace = "";
        this.text ="";
        this.fragment = "";
    }
    public PageTitle(String uuid) {
        this.uuid = uuid;

        this.namespace = "";
        this.text ="";
        this.fragment = "";
    }

    public PageTitle(String text, final Site site, @Nullable String thumbUrl) {
        this.namespace = "";
        this.fragment = "";
        this.text = text;
        this.thumbUrl = thumbUrl;
    }

    public PageTitle(String text, final Site site) {
        this(text, site, null);
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

    public void setDescription(@Nullable String description) {
        this.description = description != null ? capitalizeFirstChar(description) : null;
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
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(namespace);
        parcel.writeString(text);
        parcel.writeString(fragment);
        parcel.writeString(thumbUrl);
        parcel.writeString(description);
        parcel.writeString(uuid);
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
}
