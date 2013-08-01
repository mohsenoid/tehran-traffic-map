package com.tehran.traffic;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.widget.TextView;

public class DataLoader extends AsyncTask<String, Void, Boolean> {
	Dialog progress;
	Context context;
	WebView webview;
	TextView txtStatus;

	public DataLoader(Context c, WebView w, TextView t) {
		context = c;
		webview = w;
		txtStatus = t;
	}

	@Override
	protected void onPreExecute() {
		progress = ProgressDialog.show(context,
				context.getString(R.string.app_progress_title),
				context.getString(R.string.app_progress));
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		Boolean done = downloadImage();
		return done;
	}

	@Override
	protected void onPostExecute(Boolean done) {
		if (done) {
			txtStatus.post(new Runnable() {

				public void run() {
					txtStatus.setVisibility(TextView.INVISIBLE);
				}
			});

		} else {
			txtStatus.post(new Runnable() {

				public void run() {
					txtStatus.setVisibility(TextView.VISIBLE);
				}
			});
		}
		super.onPostExecute(done);
		progress.dismiss();
	}

	public void loadFile(final String fName) {
		try {
			File f = new File(context.getFilesDir() + "/" + fName + ".jpg");
			if (f.exists()) {
				webview.post(new Runnable() {
					public void run() {
						webview.loadUrl("file:" + context.getFilesDir() + "/"
								+ fName + ".jpg");
					}
				});
			} else {
				// First data load
				execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Boolean downloadImage() {
		// First create a new URL object
		URL url;
		try {
			url = new URL(context.getString(R.string.imgURL));

			// Next create a file, the example below will save to the SDCARD
			// using JPEG format

			FileOutputStream fOut = context.openFileOutput("tempMap.jpg",
					Context.MODE_PRIVATE);

			// Next create a Bitmap object and download the image to bitmap
			final Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());

			// Finally compress the bitmap, saving to the file previously
			// created
			bitmap.compress(CompressFormat.JPEG, 100, fOut);

			File fTemp = new File(context.getFilesDir() + "/tempMap.jpg");
			if (fTemp.exists()) {

				File fNew = new File(context.getFilesDir() + "/newMap.jpg");
				File fOld = new File(context.getFilesDir() + "/oldMap.jpg");
				if (fOld.exists()) {
					fOld.delete();
				}

				fNew.renameTo(new File(context.getFilesDir() + "/oldMap.jpg"));

				fTemp.renameTo(new File(context.getFilesDir() + "/newMap.jpg"));
			}
			loadFile("newMap");
			try {
				// save last update date time
				SharedPreferences settings = context.getSharedPreferences(
						"TehranTrafficMap", 0);
				SharedPreferences.Editor editor = settings.edit();
				Calendar now = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String formattedDate = df.format(now.getTime());
				editor.putString("lastUpdate", formattedDate);
				editor.commit();

				// Toast.makeText(context, "6. " + now.toString(),
				// Toast.LENGTH_LONG).show();
			} catch (Exception ex) {
				// Toast.makeText(context, "7. " + ex.getMessage(),
				// Toast.LENGTH_LONG).show();
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

	public void loadPrev() {
		loadFile("oldMap");
	}

	public void loadNext() {
		loadFile("newMap");
	}

	public void loadPlane() {
		// loadFile("traffic_cam");
		webview.loadUrl("file:" + "/android_asset" + "/traffic_cam.jpg");

	}

	public void loadVideo() {
		// loadFile("traffic_cam");
		webview.clearView();
		webview.loadUrl(context.getString(R.string.videoURL));

	}

	public void loadMetro() {
		// loadFile("traffic_cam");
		webview.loadUrl("file:" + "/android_asset" + "/metro_map.jpg");

	}

	public Boolean fileExist(String fName) {
		File fOld = new File(context.getFilesDir() + "/" + fName + ".jpg");
		return fOld.exists();
	}
}
