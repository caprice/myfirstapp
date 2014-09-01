package com.mmm.mvideo.infrastructure.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mmm.mvideo.common.ApplicationCommon;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageLoader.
 * @author a37wczz
 */
public class ImageLoader {

    /** The pool. */
    public static final ExecutorService POOL = Executors.newFixedThreadPool(5);

    /**
     * This method will return immediately, if there is image in local cache,
     * the return value will not be null. If not, it will call the ImageCallback
     * object to set the image asynchronously.
     * 
     * @param imageURL
     *            the image url
     * @param widthExpect
     *            the width expect
     * @param heightExpect
     *            the height expect
     * @param callback
     *            the callback
     * @param ifCache
     *            if cache the image
     * @return the bitmap
     */
    public static Bitmap asynLoadImage(final String imageURL,
            final int widthExpect, final int heightExpect,
            final ImageCallback callback, final boolean ifCache) {
        Bitmap bitmap = null;
        if (ApplicationCommon.isSDCardAvailable()) {
            bitmap = getImageFromLocal(ApplicationCommon.IMAGE_CACHE_PATH,
                    unifyFileName(imageURL));
            // tries to get image from local cache, if exist, return it
            // immediately
            if (bitmap != null) {
                Log.v("", "Load image from local cache"
                        + ApplicationCommon.IMAGE_CACHE_PATH);
                return bitmap;
            }

            // if not in local cache
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(final Message msg) {
                    callback.imageLoaded((Bitmap) msg.obj, imageURL);
                }
            };

            Runnable task = new Runnable() {
                public void run() {
                    Bitmap drawable = loadImageFromURL(imageURL, widthExpect,
                            heightExpect, ifCache);
                    handler.sendMessage(handler.obtainMessage(0, drawable));
                };
            };

            POOL.execute(task);
        }
        return bitmap;
    }

    /**
     * Load image from url.
     * 
     * @param imageUrl
     *            the image url
     * @param width
     *            the width
     * @param height
     *            the height
     * @param ifCache
     *            the if cache
     * @return the bitmap
     */
    protected static Bitmap loadImageFromURL(final String imageUrl,
            final int width, final int height, final boolean ifCache) {
        Log.d("ImageLoader", "load image from url: " + imageUrl);
        Bitmap bitmap = null;
        Bitmap source = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.connect();
            InputStream inputStream = conn.getInputStream();

            source = BitmapFactory.decodeStream(inputStream);

            inputStream.close();
            conn.disconnect();

            if (source == null) {
                Log.d("ImageLoader", "image not found from url: " + imageUrl);
                return null;
            }

            Matrix m = new Matrix();
            Log.d("ImageLoader", "width: " + width + " height: " + height);
            Log.d("ImageLoader", "src width: " + source.getWidth()
                    + " src height: " + source.getHeight());
            m.postScale((float) width / source.getWidth(), (float) height
                    / source.getHeight());
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), m, true);

            source.recycle();
            source = null;

            if (bitmap != null && ifCache) {
                saveImage(bitmap, unifyFileName(imageUrl));
            }
        } catch (MalformedURLException e) {
            Log.e("ImageLoader: ", e.getMessage());
        } catch (IOException e) {
            Log.e("ImageLoader: ", e.getMessage());
        } catch (RuntimeException e) {
            Log.e("ImageLoader: ", e.getMessage());
            return null;
        } finally {
            // -
        }
        return bitmap;
    }

    /**
     * Gets the image from local file.
     * 
     * @param path
     *            the path
     * @param imageUrl
     *            the image url
     * @return the image from local
     */
    protected static Bitmap getImageFromLocal(String path, String imageUrl) {
        if (!"".equals(imageUrl) && imageUrl != null) {
            String filePath = path + imageUrl;
            Log.d("ImageLoader", "get image from local: " + filePath);
            File file = new File(filePath);
            // not exists
            if (!file.exists()) {
                return null;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * Save image.
     * 
     * @param bitmap
     *            the bitmap
     * @param imageUrl
     *            the image url
     */
    protected static void saveImage(Bitmap bitmap, String imageUrl) {
        if (ApplicationCommon.isSDCardAvailable() && imageUrl != null) {
            String path = ApplicationCommon.IMAGE_CACHE_PATH;
            File file = new File(path + imageUrl);
            Log.d("ImageLoader", "save image to: " + file.getName());
            FileOutputStream outputStream = null;
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            } catch (Exception e) {
                Log.e("ImageLoader: ", e.getMessage());
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
//                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Unify file name.
     * 
     * @param fileName
     *            the file name
     * @return the string
     */
    private static String unifyFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf("/") + 1).replaceAll(
                "[\\/:*?\"<>|]", "");
    }

    /**
     * The Interface ImageCallback.
     */
    public interface ImageCallback {

        /**
         * Image loaded.
         * 
         * @param imageDrawable
         *            the image drawable
         * @param imageUrl
         *            the image url
         */
        public void imageLoaded(Bitmap imageDrawable, String imageUrl);
    }

}
