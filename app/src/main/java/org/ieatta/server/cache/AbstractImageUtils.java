package org.ieatta.server.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import org.ieatta.database.models.DBPhoto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import bolts.Task;

/// How to store a taken photo to decrease client storage.
///   1.  When taken a photo, save it as original and thumbnail formats.
///   2.1 When pushing to server, push original and thumbnail images to server.
///   2.2 When pushed successfully, delete offline original image.
///   3.  When pulling from server, just download a thumbnail image from server.
public abstract class AbstractImageUtils {
    protected abstract UnlimitedDiskCache getImageCache();

    /**
     * Query the disk cache's url path.
     * <p/>
     * - parameter objectUUID: Photo's objectUUID
     * <p/>
     * - returns: Image Cache's url
     */
    public File getCacheImageUrl(String objectUUID) {
        return this.getImageCache().get(objectUUID);
    }

    public File getCacheImageUrl(DBPhoto model) {
        return this.getCacheImageUrl(model.getUUID());
    }

    public boolean diskImageExistsWithKey(DBPhoto model) {
        File file = this.getImageCache().get(model.getUUID());
        if (file == null || file.exists() == false) {
            return false;
        }
        return true;
    }

    public File getTakenPhotoFile(String UUID) {
        return this.getImageCache().get(UUID);
    }

    /**
     * Query the disk cache synchronously after checking the memory cache.
     * <p/>
     * - parameter UUID: photo's UUID
     * <p/>
     * - returns: Image Cache
     */
    public Bitmap getTakenPhoto(String UUID) {
        File imageFile = this.getTakenPhotoFile(UUID);
        if (imageFile == null || imageFile.exists() == false) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
    }

    /**
     * Query the disk cache synchronously after checking the memory cache.
     * <p/>
     * - parameter model: Photo's instance
     * <p/>
     * - returns: Image Cache
     */
    public Bitmap getTakenPhoto(DBPhoto model) {
        return this.getTakenPhoto(model.getUUID());
    }

    public List listCacheImageNames() {
        return this.getImageCache().getCacheFileList();
    }

    /**
     * Save photo's image as a offline file.
     * <p/>
     * - parameter image:           saved image
     * - parameter model:           photo's instance
     * - parameter completionBlock: callback variable
     */
    public Task<Bitmap> saveTakenPhoto(Bitmap image, DBPhoto model) {

        // ** Important ** Must store to Disk.
        try {
            this.getImageCache().save(model.getUUID(), image);
        } catch (IOException e) {
            return Task.forError(e);
        }

        return Task.forResult(image);
    }

    public Task<Void> saveTakenPhoto(InputStream inputStream, DBPhoto model) {

        // ** Important ** Must store to Disk.
        try {
            this.getImageCache().save(model.getUUID(), inputStream, null);
        } catch (IOException e) {
            return Task.forError(e);
        }

        return Task.forResult(null);
    }

    /**
     * Generate a specail type image, then save it as the offline image.
     * <p/>
     * - parameter image: taken photo
     * - parameter model: photo's instance
     * <p/>
     * - returns: task's instance
     */
    public Task<Bitmap> generateTakenPhoto(Bitmap image, DBPhoto model) {
        return Task.forResult(null);
    }

}
