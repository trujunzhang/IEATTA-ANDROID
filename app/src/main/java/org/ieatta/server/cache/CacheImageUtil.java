package org.ieatta.server.cache;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.ieatta.IEAApp;

import java.io.File;

public class CacheImageUtil extends BaseImageUtil {

    public final static CacheImageUtil sharedInstance = new CacheImageUtil();

    /**
     * This SDImageCache constructor's namespace is "Cache".
     * Store images on the folder: "Cache/com.virtualbreak.IEATTA.cache"
     * <p/>
     * - returns: SDImageCache's instance
     */
    @Override
    protected UnlimitedDiskCache getImageCache() {
        File cacheDir = StorageUtils.getCacheDirectory(IEAApp.getInstance(), "cache");
        return new UnlimitedDiskCache(cacheDir);
    }

    public void clearCacheDisk() {
        this.getImageCache().clear();
    }

}
