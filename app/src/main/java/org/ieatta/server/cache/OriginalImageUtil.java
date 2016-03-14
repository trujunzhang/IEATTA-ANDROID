package org.ieatta.server.cache;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.ieatta.IEAApp;
import org.ieatta.database.models.DBPhoto;

import java.io.File;

public class OriginalImageUtil extends AbstractImageUtil {

    public final static OriginalImageUtil sharedInstance = new OriginalImageUtil();

    /**
     * This SDImageCache constructor's namespace is "Original".
     * Store images on the folder: "Original/com.virtualbreak.IEATTA.original"
     * <p/>
     * - returns: SDImageCache's instance
     */
    @Override
    protected UnlimitedDiskCache getImageCache() {
        File cacheDir = StorageUtils.getCacheDirectory(IEAApp.getInstance(), "original");
        return new UnlimitedDiskCache(cacheDir);
    }

    /**
     * Remove offline original image.
     * <p/>
     * - parameter photo: photo's instance
     */
    public boolean removeOriginalImage(DBPhoto photo) {
        boolean imageExist = this.diskImageExistsWithKey(photo);
        if (imageExist == false) {
            return false;
        }
        boolean isRemove = this.getImageCache().remove(photo.getUUID());

        return isRemove;
    }

//    @Override
//    public Task<Bitmap> generateTakenPhoto(Bitmap image, DBPhoto model) {
//        Bitmap originalImage = ImageOptimizeUtils.generateOriginalImage(image);
//
//        return super.saveTakenPhoto(originalImage, model);
//    }
}
