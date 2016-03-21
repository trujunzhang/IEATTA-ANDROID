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

    public static final String data_format = "yyyyMMddhhmm";

    protected abstract UnlimitedDiskCache getImageCache();

    /**
     * Query the disk cache's url path.
     * <p/>
     * - parameter UUID: Photo's UUID
     * <p/>
     * - returns: Image Cache's url
     */
    public File getCacheImageUrl(String UUID) {
        return this.getImageCache().get(UUID);
    }

    public File getCacheImageUrl( DBPhoto model) {
        String uuid = model.getUUID();
        String usedRef = model.getUsedRef();
        String dateCreatedString = new SimpleDateFormat(data_format).format(model.getObjectCreatedDate());

        return this.getImageCache().getFile(usedRef, uuid, dateCreatedString);
    }

    public Task<List<File>> getImagesListTask(String usedRef) {
        List<File> list = getImageFiles(usedRef);
        if (list.size() == 0) {
            return Task.forError(new FileNotFoundException("Not found images by " + usedRef));
        }
        return Task.forResult(list);
    }

    /**
     * Query the disk cache's url path.
     * <p/>
     * - parameter usedRef: Photo's usedRef's UUID
     * <p/>
     * - returns: Images List
     */
    public List<File> getImageFiles(String usedRef) {
        return this.getImageCache().getList(usedRef);
    }

    public File getImageFile(String usedRef) {
        List<File> imageFiles = this.getImageFiles(usedRef);
        if(imageFiles.size() == 0)
            return null;

        return imageFiles.get(0);
    }

    public Task<List<File>> getImagesListTask() {
        List<File> list = this.getImageCache().getList();
        if (list.size() == 0) {
            return Task.forError(new FileNotFoundException("No images cached"));
        }
        return Task.forResult(list);
    }

//    public File getCacheImageUrl(DBPhoto model) {
//        return this.getCacheImageUrl(model.getUUID());
//    }

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
            String dateCreatedString = new SimpleDateFormat(data_format).format(model.getObjectCreatedDate());
            save = this.getImageCache().save(usedRef, uuid, dateCreatedString, inputStream, null);
        } catch (IOException e) {
            return Task.forError(e);
        }
        ///data/data/org.ieatta.alpha/thumbnail/828DB1D6-67AB-467D-8D98-76C1938C5306/201511230822_FD0CA37F-7ECF-443E-B69E-5FBBB8EEB771

        if (save == false) {
            return Task.forError(new FileNotFoundException("Cache thumbnail image failed"));
        }

        return Task.forResult(null);
    }

    public Task<Void> saveTakenPhoto(InputStream inputStream, String uuid) {

        // ** Important ** (Must store to Disk).
        boolean save = false;
        try {
            save = this.getImageCache().save(uuid, inputStream, null);
        } catch (IOException e) {
            return Task.forError(e);
        }
        ///data/data/org.ieatta.alpha/thumbnail/828DB1D6-67AB-467D-8D98-76C1938C5306/201511230822_FD0CA37F-7ECF-443E-B69E-5FBBB8EEB771

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
