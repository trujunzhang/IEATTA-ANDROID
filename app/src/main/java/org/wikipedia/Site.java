package org.wikipedia;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;


import org.ieatta.IEATTAApp;

import java.util.Locale;

/**
 * Represents a particular wiki.
 */
public class Site implements Parcelable {
    private final String domain;

    private final String languageCode;

    public Site(String domain) {
        this(domain, urlToLanguage(domain));
    }

    public Site(String domain, String languageCode) {
        this.domain = urlToDesktopSite(domain);
        this.languageCode = languageCode;
    }

    public Site(Parcel in) {
        this(in.readString(), in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(domain);
        dest.writeString(languageCode);
    }

    public String getScriptPath(String script) {
        return "/w/" + script;
    }

    public boolean getUseSecure() {
        return true;
    }

    public String getDomain() {
        return domain;
    }

    public String getMobileDomain() {
        return urlToMobileSite(domain);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Site> CREATOR
            = new Parcelable.Creator<Site>() {
        public Site createFromParcel(Parcel in) {
            return new Site(in);
        }

        public Site[] newArray(int size) {
            return new Site[size];
        }
    };

    // Auto-generated
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Site site = (Site) o;

        if (domain != null ? !domain.equals(site.domain) : site.domain != null) {
            return false;
        }
        return !(languageCode != null ? !languageCode.equals(site.languageCode) : site.languageCode != null);

    }

    // Auto-generated
    @Override
    public int hashCode() {
        int result = domain != null ? domain.hashCode() : 0;
        result = 31 * result + (languageCode != null ? languageCode.hashCode() : 0);
        return result;
    }

    // Auto-generated
    @Override
    public String toString() {
        return "Site{"
                + "domain='" + domain + '\''
                + ", languageCode='" + languageCode + '\''
                + '}';
    }

    public String getFullUrl(String script) {
        return IEATTAApp.getInstance().getNetworkProtocol() + "://" + getDomain() + getScriptPath(script);
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public static Site forLanguage(String language) {
//        return new Site("http://localhost:8888/mediawiki", language);
        return new Site(languageToWikiSubdomain(language) + ".wikipedia.org", language);
    }

    /**
     * Returns if the site is supported
     * @param domain the site domain
     * @return boolean
     */
    public static boolean isSupportedSite(String domain) {
        return domain.matches("[a-z\\-]+\\.(m\\.)?wikipedia\\.org");
    }

    private static String urlToLanguage(String url) {
        return url.split("\\.")[0];
    }

    private String urlToDesktopSite(String url) {
        return url.replaceFirst("\\.m\\.", ".");
    }

    private String urlToMobileSite(String url) {
        return url.replaceFirst("\\.", ".m.");
    }

    private static String languageToWikiSubdomain(String language) {
        return language;
    }
}
