package com.example.lucas.imageloaddemo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Lucas on 11/04/2015.
 */
public class ImageViewFragment extends Fragment {

    public static final String FRAGMENT_TAG = "ImageLoadDemo";

    public ImageViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ImageView imageView = (ImageView)rootView.findViewById(R.id.imageView);
        //imageView.setImageBitmap(
                //decodeSampledBitmapFromResource(getResources(), R.drawable.big_image, 200, 200));

        loadBitmap(R.drawable.big_image,imageView);

        if(null != imageView) {
            Log.i(FRAGMENT_TAG, "Calling imageLoader method...");
            Log.i(FRAGMENT_TAG,"height "+imageView.getLayoutParams().height);
            Log.i(FRAGMENT_TAG,"width "+imageView.getWidth());

        }
        return rootView;
    }

    public void loadBitmap(int resId, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(resId);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,int reqWidth,int reqHeight) {
        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,resId,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,resId,options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        public BitmapWorkerTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            Log.i(FRAGMENT_TAG, "Inside Async Task");
            data = params[0];
            return ImageViewFragment.this.decodeSampledBitmapFromResource(getResources(), data, 100, 100);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if(imageView != null)
                    imageView.setImageBitmap(bitmap);
            }
        }
    }

}
