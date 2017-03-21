package com.whitebirdtechnology.treepr.volley;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.whitebirdtechnology.treepr.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by dell on 23/2/17.
 */

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private String stringImageUrl;
    private ImageView imageView;
    Activity activity;
    public ImageDownloaderTask(ImageView imageView, String ImageURL, Activity activity) {
        this.imageView = imageView;
        this.stringImageUrl = ImageURL;
        this.activity = activity;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Log.d("stringImageUrl",stringImageUrl);
        File pictureFile = getOutputMediaFile(stringImageUrl);
        try {


        final String stringUrl = activity.getString(R.string.serviceURL)+stringImageUrl;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity)
                // You can pass your own memory cache implementation
                .discCache(new UnlimitedDiskCache(pictureFile)) // You can pass your own disc cache implementation
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init(config);
        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.loading_image)
                .build();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageLoader.getInstance().displayImage(stringUrl, imageView, options); // Incoming options will be used
            }
        });

        }catch (Exception e){
            downloadBitmap(stringImageUrl);
        }
        //  Bitmap bitmap = downloadBitmap(stringImageUrl);
      //  storeImage(bitmap,stringImageUrl);
        return null;

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        /*
        if (isCancelled()) {
            bitmap = null;
        }

        if (bitmap != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

            imageView.setImageBitmap(decoded);

            //SaveImgSingltonCls.getInstance().hashMapImgDownload.put(stringImageUrl,bitmap);
        }/* else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                        imageView.setImageDrawable(placeholder);
                    }*/
    }

    private Bitmap downloadBitmap(String stringImageUrl) {
        URL url = null;
        Bitmap bmp;
        try {
            // URL uri = new URL(stringImageUrl);
            stringImageUrl = activity.getString(R.string.serviceURL)+stringImageUrl;
            if (stringImageUrl != null) {
                url = new URL(stringImageUrl);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                if (bmp != null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                    final Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(decoded);
                        }
                    });


                    //SaveImgSingltonCls.getInstance().hashMapImgDownload.put(stringImageUrl,bitmap);
                }
                return bmp;
            }
        } catch (Exception e) {
            //  urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        }
        return null;
    }


    private void storeImage(Bitmap image,String s) {



        String s2 = s;
        File pictureFile = getOutputMediaFile(s2);
        if (pictureFile == null||image==null) {
            Log.d("xb",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("hs", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("djb", "Error accessing file: " + e.getMessage());
        }
    }

    private  File getOutputMediaFile(String s2){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaFile = null;
        try {


            assert s2 != null;
            String fileName = s2.substring(s2.lastIndexOf("/") + 1);
            final String stringImageURL = s2.substring(0, fileName.length() - 1);
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                    + "/Android/data/"
                    + activity.getPackageName()
                    + "/Files/" + stringImageURL, fileName);

            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }
            // Create a media file name
            //String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());

            // String mImageName=s2;
            mediaFile = new File(mediaStorageDir.getPath(), fileName);
        }catch (Exception e){

        }
        return mediaFile;
    }

}


