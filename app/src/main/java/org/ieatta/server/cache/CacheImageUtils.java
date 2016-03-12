package org.ieatta.server.cache;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.ieatta.IEATTAApp;

import java.io.File;

public class CacheImageUtils extends AbstractImageUtils {

    public final static CacheImageUtils sharedInstance = new CacheImageUtils();

    /**
     * This SDImageCache constructor's namespace is "Cache".
     * Store images on the folder: "Cache/com.virtualbreak.IEATTA.cache"
     * <p/>
     * - returns: SDImageCache's instance
     */
    @Override
    protected UnlimitedDiskCache getImageCache() {
        File cacheDir = StorageUtils.getCacheDirectory(IEATTAApp.getInstance(), "cache");
        return new UnlimitedDiskCache(cacheDir);
    }

    public void clearCacheDisk() {
        this.getImageCache().clear();
    }

}
