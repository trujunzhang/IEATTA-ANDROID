/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nostra13.universalimageloader.cache.disc.impl;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.utils.IoUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Base disk cache.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see FileNameGenerator
 * @since 1.0.0
 */
public abstract class BaseDiskCache implements DiskCache {
    /**
     * {@value
     */
    public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 Kb
    /**
     * {@value
     */
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    /**
     * {@value
     */
    public static final int DEFAULT_COMPRESS_QUALITY = 100;

    private static final String ERROR_ARG_NULL = " argument must be not null";
    private static final String TEMP_IMAGE_POSTFIX = ".tmp";

    protected final File cacheDir;
    protected final File reserveCacheDir;

    protected final Md5FileNameGenerator fileNameGenerator;

    protected int bufferSize = DEFAULT_BUFFER_SIZE;

    protected Bitmap.CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
    protected int compressQuality = DEFAULT_COMPRESS_QUALITY;

    /**
     * @param cacheDir Directory for file caching
     */
    public BaseDiskCache(File cacheDir) {
        this(cacheDir, null);
    }

    /**
     * @param cacheDir        Directory for file caching
     * @param reserveCacheDir null-ok; Reserve directory for file caching. It's used when the primary directory isn't available.
     */
    public BaseDiskCache(File cacheDir, File reserveCacheDir) {
        this(cacheDir, reserveCacheDir, new Md5FileNameGenerator());
    }

    /**
     * @param cacheDir          Directory for file caching
     * @param reserveCacheDir   null-ok; Reserve directory for file caching. It's used when the primary directory isn't available.
     * @param fileNameGenerator {@linkplain com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator
     *                          Name generator} for cached files
     */
    public BaseDiskCache(File cacheDir, File reserveCacheDir, Md5FileNameGenerator fileNameGenerator) {
        if (cacheDir == null) {
            throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
        }
        if (fileNameGenerator == null) {
            throw new IllegalArgumentException("fileNameGenerator" + ERROR_ARG_NULL);
        }

        this.cacheDir = cacheDir;
        this.reserveCacheDir = reserveCacheDir;
        this.fileNameGenerator = fileNameGenerator;
    }

    @Override
    public File getDirectory() {
        return cacheDir;
    }

    @Override
    public File get(String imageUri) {
        return getFile(imageUri);
    }

    @Override
    public List<File> getList(String imageDir) {
        File imageFolder = new File(cacheDir, imageDir);
        if (imageFolder.exists() == true) {
            final File[] files = imageFolder.listFiles();
            if (files.length > 0) {
                Arrays.sort(files, new FileComparator());
                return new LinkedList<>(Arrays.asList(files));
            }
        }
        return new LinkedList<>();
    }

    @Override
    public List<File> getList() {
        if (cacheDir.exists() == true) {
            final File[] files = cacheDir.listFiles();
            if (files.length > 0) {
                Arrays.sort(files, new FileComparator());
                return new LinkedList<>(Arrays.asList(files));
            }
        }
        return new LinkedList<>();
    }

    @Override
    public boolean save(String imageUri, InputStream imageStream, IoUtils.CopyListener listener) throws IOException {
        File imageFile = getFile(imageUri);
        File tmpFile = new File(imageFile.getAbsolutePath() + TEMP_IMAGE_POSTFIX);
        boolean loaded = false;
        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(tmpFile), bufferSize);
            try {
                loaded = IoUtils.copyStream(imageStream, os, listener, bufferSize);
            } finally {
                IoUtils.closeSilently(os);
            }
        } finally {
            if (loaded && !tmpFile.renameTo(imageFile)) {
                loaded = false;
            }
            if (!loaded) {
                tmpFile.delete();
            }
        }
        return loaded;
    }

    @Override
    public boolean save(String imageDir, String imageUri, String dateCreatedString, InputStream imageStream, IoUtils.CopyListener listener) throws IOException {
        File imageFile = getFile(imageDir, imageUri, dateCreatedString);
        File tmpFile = new File(imageFile.getAbsolutePath() + TEMP_IMAGE_POSTFIX);
        boolean loaded = false;
        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(tmpFile), bufferSize);
            try {
                loaded = IoUtils.copyStream(imageStream, os, listener, bufferSize);
            } finally {
                IoUtils.closeSilently(os);
            }
        } finally {
            if (loaded && !tmpFile.renameTo(imageFile)) {
                loaded = false;
            }
            if (!loaded) {
                tmpFile.delete();
            }
        }
        return loaded;
    }

    @Override
    public boolean save(String imageUri, Bitmap bitmap) throws IOException {
        File imageFile = getFile(imageUri);
        File tmpFile = new File(imageFile.getAbsolutePath() + TEMP_IMAGE_POSTFIX);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(tmpFile), bufferSize);
        boolean savedSuccessfully = false;
        try {
            savedSuccessfully = bitmap.compress(compressFormat, compressQuality, os);
        } finally {
            IoUtils.closeSilently(os);
            if (savedSuccessfully && !tmpFile.renameTo(imageFile)) {
                savedSuccessfully = false;
            }
            if (!savedSuccessfully) {
                tmpFile.delete();
            }
        }
        bitmap.recycle();
        return savedSuccessfully;
    }

    @Override
    public boolean remove(String imageUri) {
        return getFile(imageUri).delete();
    }

    @Override
    public void close() {
        // Nothing to do
    }

    @Override
    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }

    public List<File> getCacheFileList() {
        File[] files = cacheDir.listFiles();
        if (files != null) {
            new LinkedList<File>(Arrays.asList(files));
        }
        return new LinkedList<File>();
    }

    /**
     * Returns file object (not null) for incoming image URI. File object can reference to non-existing file.
     */
    protected File getFile(String imageUri) {
        String fileName = fileNameGenerator.generate(imageUri);
        File dir = cacheDir;
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            if (reserveCacheDir != null && (reserveCacheDir.exists() || reserveCacheDir.mkdirs())) {
                dir = reserveCacheDir;
            }
        }
        return new File(dir, fileName);
    }

    /**
     * @param imageDir          Special UUID type's folder
     * @param imageUri          Cached file's UUID
     * @param dateCreatedString Cached file's createdAt
     * @return
     */
    public File getFile(String imageDir, String imageUri, String dateCreatedString) {
        String fileName = String.format("%s_%s", dateCreatedString, imageUri);
        File dir = cacheDir;
        File imageFolder = new File(dir, imageDir);
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            // Check "checkDir" exist.
        }
        if (!imageFolder.exists() && !imageFolder.mkdirs()) {
            // Check "subFolder" exist.
        }
        File file = new File(imageFolder, fileName);
        if (file.exists() == true) {
            return null;
        }
        return file;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
    }

    public void setCompressQuality(int compressQuality) {
        this.compressQuality = compressQuality;
    }
}