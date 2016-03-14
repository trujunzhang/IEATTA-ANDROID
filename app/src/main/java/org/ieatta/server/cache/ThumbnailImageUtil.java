package org.ieatta.server.cache;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.ieatta.IEAApp;

import java.io.File;

public class ThumbnailImageUtil extends AbstractImageUtil {

    public final static ThumbnailImageUtil sharedInstance = new ThumbnailImageUtil();

    /**
     * This SDImageCache constructor's namespace is "Thumbnail".
     * Store images on the folder: "Thumbnail/com.virtualbreak.IEATTA.thumbnail"
     * <p/>
     * - returns: SDImageCache's instance
     */
    @Override
    public UnlimitedDiskCache getImageCache() {
        File cacheDir = StorageUtils.getCacheDirectory(IEAApp.getInstance(), "thumbnail");
        return new UnlimitedDiskCache(cacheDir);
    }

    /**
     * First of all, generate a thumbnail image from the original image, then store the thumbnail image as a offline cache file.
     * <p/>
     * - parameter image:           original image
     * - parameter model:           Photo's instance
     * - parameter completionBlock: callback variable
     */
//    @Override
//    public Task<Bitmap> generateTakenPhoto(Bitmap image, DBPhoto model) {
//        Bitmap thumbnailImage = ImageOptimizeUtils.generateThumbnailImage(image);
//
//        return super.saveTakenPhoto(thumbnailImage, model);
//    }
}
