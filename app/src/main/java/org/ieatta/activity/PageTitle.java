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

import static org.wikipedia.util.StringUtil.capitalizeFirstChar;
import static org.wikipedia.util.StringUtil.md5string;
import static org.wikipedia.util.UriUtil.decodeURL;

public class PageTitle {

    private final String text;
    @Nullable private String thumbUrl;
    private String description = null;

    public PageTitle(final String text, @Nullable final String thumbUrl) {
        this.text = text;
        this.thumbUrl = thumbUrl;
    }
}
