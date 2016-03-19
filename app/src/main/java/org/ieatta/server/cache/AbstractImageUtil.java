package org.ieatta.server.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;

import org.ieatta.database.models.DBPhoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import bolts.Task;

/// How to store a taken photo to decrease client storage.
///   1.  When taken a photo, save it as original and thumbnail formats.
///   2.1 When pushing to server, push original and thumbnail images to server.
///   2.2 When pushed successfully, delete offline original image.
///   3.  When pulling from server, just download a thumbnail image from server.
public abstract class AbstractImageUtil {
    protected abstract UnlimitedDiskCache getImageCache();

    /**
     * Query the disk cache's url path.
     * <p/>
     * - parameter UUID: Photo's tUUID
     * <p/>
     * - returns: Image Cache's url
     */
    public File getCacheImageUrl(String UUID) {
        return this.getImageCache().get(UUID);
    }

    public List<File> getImagesList(String usedRef){
        List<File> list = this.getImageCache().getList(usedRef);
        return list;
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
     * Save photo's InputStream as an offline file.
     * <p/>
     * - parameter image:           saved image's InputStream
     * - parameter model:           photo's instance
     */
    public Task<Void> saveTakenPhoto(InputStream inputStream, DBPhoto model) {

        // ** Important ** (Must store to Disk).
        boolean save = false;
        try {
            String uuid = model.getUUID();
            String usedRef = model.getUsedRef();
            String dateCreatedString = new SimpleDateFormat("yyyyMMddhhmm").format(model.getObjectCreatedDate());
            save = this.getImageCache().save(usedRef, uuid, dateCreatedString, inputStream, null);
        } catch (IOException e) {
            return Task.forError(e);
        }
        ///data/data/org.ieatta.alpha/thumbnail/828DB1D6-67AB-467D-8D98-76C1938C5306/20151123082215qebv8ze9helfca46q1su4ob

        if (save == false) {
            return Task.forError(new FileNotFoundException("Cache thumbnail image failed"));
        }

        return Task.forResult(null);
    }

    /**
     * Generate a special type image, then save it as the offline image.
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
