package com.tehran.traffic.network;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tehran.traffic.R;
import com.tehran.traffic.models.RoadData;
import com.tehran.traffic.ui.MainActivity;
import com.tehran.traffic.ui.TouchImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataLoader extends AsyncTask<String, Void, Boolean> {
    static Bitmap bmMetro, bmPlane, bmBrt;
    Dialog progress;
    Activity activity;
    TouchImageView tivMap;
    TextView tvError;
    long startTime;
    long endTime;
    private FirebaseAnalytics firebaseAnalytics;

    public DataLoader(Activity activity, TouchImageView tivMap, TextView tvError) {
        this.activity = activity;
        this.tivMap = tivMap;
        this.tvError = tvError;

        // Analytics instance
        firebaseAnalytics = FirebaseAnalytics.getInstance(activity);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(activity,
                activity.getString(R.string.app_progress_title),
                activity.getString(R.string.app_progress), true, true,
                new OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancel(true);
                        ((MainActivity) activity).showTrafficMap();
                    }
                });

        startTime = Calendar.getInstance().getTimeInMillis();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... data) {
        Boolean done = downloadImage(data[0], data[1], data[2], data[3]);
        return done;
    }

    private Boolean downloadImage(String u, String oldFile, String newFile, String extension) {
        // First create a new URL object
        URL url;
        try {
            url = new URL(u);

            // Next create a file, the example below will save to the SDCARD
            // using JPEG format

            FileOutputStream fOut = activity.openFileOutput("temp." + extension,
                    Context.MODE_PRIVATE);

            // Next create a Bitmap object and download the image to bitmap
            final Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());

            // Finally compress the bitmap, saving to the file previously
            // created
            if (extension.equals("jpg")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            } else if (extension.equals("png")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            }

            File fTemp = new File(activity.getFilesDir() + "/temp." + extension);
            if (fTemp.exists()) {

                File fNew = new File(activity.getFilesDir() + "/" + newFile
                        + "." + extension);
                File fOld = new File(activity.getFilesDir() + "/" + oldFile
                        + "." + extension);
                if (fOld.exists()) {
                    fOld.delete();
                }

                fNew.renameTo(new File(activity.getFilesDir() + "/" + oldFile
                        + "." + extension));

                fTemp.renameTo(new File(activity.getFilesDir() + "/" + newFile
                        + "." + extension));
            }

            try {
                // save last update date time
                SharedPreferences settings = activity.getSharedPreferences(
                        "TehranTrafficMap", 0);
                SharedPreferences.Editor editor = settings.edit();
                Calendar now = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(now.getTime());
                editor.putString(newFile, formattedDate);
                editor.commit();

                // Toast.makeText(activity, "6. " + now.toString(),
                // Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                // Toast.makeText(activity, "7. " + ex.getMessage(),
                // Toast.LENGTH_LONG).show();
            }

            if (!isCancelled()) {
                loadFile(newFile, extension, false);
            }
            return true;

        } catch (UnknownHostException e) {
            // txtStatus.setText(e.getMessage().toString());
            return false;
        } catch (Exception e) {
            // txtStatus.setText(e.getMessage().toString());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean done) {
        endTime = Calendar.getInstance().getTimeInMillis();
        Bundle bundle = new Bundle();
        bundle.putString("value", endTime - startTime+"");
        bundle.putString("action", "successful");
        firebaseAnalytics.logEvent("network", bundle);

        if (done) {
            tvError.post(new Runnable() {

                public void run() {
                    tvError.setVisibility(TextView.INVISIBLE);
                }
            });

        } else {
            tvError.post(new Runnable() {

                public void run() {
                    tvError.setVisibility(TextView.VISIBLE);
                }
            });
        }
        super.onPostExecute(done);
        if (progress != null)
            progress.dismiss();
    }

    public void loadFile(final String fName, final String extention, boolean isForce) {
        try {
            File f = new File(activity.getFilesDir() + "/" + fName + "." + extention);
            if (f.exists() && !isForce) {
                tivMap.post(new Runnable() {
                    public void run() {
                        Bitmap bm = BitmapFactory.decodeFile(activity
                                .getFilesDir() + "/" + fName + "." + extention);
                        tivMap.setImageBitmap(bm);
//                        tivMap.setBackgroundColor(activity.getResources().getColor(R.color.white));
                    }
                });
            } else {
                // First data load
                execute(activity.getString(R.string.imgURL), "oldMap", "newMap", "jpg");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTile(final int tile, boolean isForce) {
        try {
            File f = new File(activity.getFilesDir() + "/newTile" + tile
                    + ".jpg");
            if (f.exists() && !isForce) {
                tivMap.post(new Runnable() {
                    public void run() {
                        Bitmap bm = BitmapFactory.decodeFile(activity
                                .getFilesDir() + "/newTile" + tile + ".jpg");
                        tivMap.setImageBitmap(bm);
//                        tivMap.setBackgroundColor(activity.getResources().getColor(R.color.white));
                    }
                });
            } else {
                // First data load
                execute(String.format(
                        activity.getResources().getString(R.string.tileURL),
                        tile), "oldTile" + tile, "newTile" + tile, "jpg");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRoad(final int id, boolean isForce) {
        try {
            RoadData roadData = new RoadData(activity);

            File f = new File(activity.getFilesDir() + "/newRoad" + id
                    + ".png");
            if (f.exists() && !isForce) {
                tivMap.post(new Runnable() {
                    public void run() {
                        Bitmap bm = BitmapFactory.decodeFile(activity
                                .getFilesDir() + "/newRoad" + id + ".png");
                        tivMap.setImageBitmap(bm);
//                        tivMap.setBackgroundColor(activity.getResources().getColor(R.color.white));

//                        Drawable d = Drawable.createFromPath(activity
//                                .getFilesDir() + "/newRoad" + id + ".png");
//                        tivMap.setImageDrawable(d);
                    }
                });
            } else {
                // First data load
                execute(roadData.getImageUrl(id), "oldRoad" + id, "newRoad" + id, "png");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPrev() {
        loadFile("oldMap", "jpg", false);
    }

    // public void loadNext() {
    // loadFile("newMap");
    // }

    public void loadPlane() {
        try {
            if (bmPlane == null)
                bmPlane = getBitmapFromAsset("traffic_cam.jpg");
            tivMap.setImageBitmap(bmPlane);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void loadBrt() {
        try {
            if (bmBrt == null)
                bmBrt = getBitmapFromAsset("brt_map.jpg");
            tivMap.setImageBitmap(bmBrt);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapFromAsset(String strName) {
        AssetManager assetManager = activity.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(strName);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            return null;
        }

        return bitmap;
    }

    public void loadMetro() {
        try {
            if (bmMetro == null)
                bmMetro = getBitmapFromAsset("metro_map.jpg");
            tivMap.setImageBitmap(bmMetro);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Boolean fileExist(String fName) {
        File fOld = new File(activity.getFilesDir() + "/" + fName + ".jpg");
        return fOld.exists();
    }
}
