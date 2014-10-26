package com.tehran.traffic.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.TextView;

import com.tehran.traffic.R;
import com.tehran.traffic.ui.MainActivity;
import com.tehran.traffic.ui.TouchImageView;

public class DataLoader extends AsyncTask<String, Void, Boolean> {
	Dialog progress;
	Context context;
	TouchImageView tivMap;
	TextView tvError;
	static Bitmap bmMetro, bmPlane, bmBrt;

	public DataLoader(Context context, TouchImageView tivMap, TextView tvError) {
		this.context = context;
		this.tivMap = tivMap;
		this.tvError = tvError;
	}

	@Override
	protected void onPreExecute() {
		progress = ProgressDialog.show(context,
				context.getString(R.string.app_progress_title),
				context.getString(R.string.app_progress), true, true,
				new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						cancel(true);
						((MainActivity) context).showTrafficMap();
					}
				});
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... data) {
		Boolean done = downloadImage(data[0], data[1], data[2]);
		return done;
	}

	private Boolean downloadImage(String u, String oldFile, String newFile) {
		// First create a new URL object
		URL url;
		try {
			url = new URL(u);

			// Next create a file, the example below will save to the SDCARD
			// using JPEG format

			FileOutputStream fOut = context.openFileOutput("temp.jpg",
					Context.MODE_PRIVATE);

			// Next create a Bitmap object and download the image to bitmap
			final Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());

			// Finally compress the bitmap, saving to the file previously
			// created
			bitmap.compress(CompressFormat.JPEG, 100, fOut);

			File fTemp = new File(context.getFilesDir() + "/temp.jpg");
			if (fTemp.exists()) {

				File fNew = new File(context.getFilesDir() + "/" + newFile
						+ ".jpg");
				File fOld = new File(context.getFilesDir() + "/" + oldFile
						+ ".jpg");
				if (fOld.exists()) {
					fOld.delete();
				}

				fNew.renameTo(new File(context.getFilesDir() + "/" + oldFile
						+ ".jpg"));

				fTemp.renameTo(new File(context.getFilesDir() + "/" + newFile
						+ ".jpg"));
			}

			try {
				// save last update date time
				SharedPreferences settings = context.getSharedPreferences(
						"TehranTrafficMap", 0);
				SharedPreferences.Editor editor = settings.edit();
				Calendar now = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String formattedDate = df.format(now.getTime());
				editor.putString(newFile, formattedDate);
				editor.commit();

				// Toast.makeText(context, "6. " + now.toString(),
				// Toast.LENGTH_LONG).show();
			} catch (Exception ex) {
				// Toast.makeText(context, "7. " + ex.getMessage(),
				// Toast.LENGTH_LONG).show();
			}

			if (!isCancelled()) {
				loadFile(newFile, false);
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

	public void loadFile(final String fName, boolean isForce) {
		try {
			File f = new File(context.getFilesDir() + "/" + fName + ".jpg");
			if (f.exists() && !isForce) {
				tivMap.post(new Runnable() {
					public void run() {
						Bitmap bm = BitmapFactory.decodeFile(context
								.getFilesDir() + "/" + fName + ".jpg");
						tivMap.setImageBitmap(bm);
					}
				});
			} else {
				// First data load
				execute(context.getString(R.string.imgURL), "oldMap", "newMap");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadTile(final int tile, boolean isForce) {
		try {
			File f = new File(context.getFilesDir() + "/newTile" + tile
					+ ".jpg");
			if (f.exists() && !isForce) {
				tivMap.post(new Runnable() {
					public void run() {
						Bitmap bm = BitmapFactory.decodeFile(context
								.getFilesDir() + "/newTile" + tile + ".jpg");
						tivMap.setImageBitmap(bm);
					}
				});
			} else {
				// First data load
				execute(String.format(
						context.getResources().getString(R.string.tileURL),
						tile), "oldTile" + tile, "newTile" + tile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadPrev() {
		loadFile("oldMap", false);
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
		AssetManager assetManager = context.getAssets();

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
		File fOld = new File(context.getFilesDir() + "/" + fName + ".jpg");
		return fOld.exists();
	}
}
