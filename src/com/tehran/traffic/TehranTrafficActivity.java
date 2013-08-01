package com.tehran.traffic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

public class TehranTrafficActivity extends Activity {
	private static DataLoader loader;
	private boolean doubleBackToExitPressedOnce;

	// // Whether or not the system UI should be auto-hidden after
	// // AUTO_HIDE_DELAY_MILLIS milliseconds.
	// private static final boolean AUTO_HIDE = true;
	//
	// // If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	// // user interaction before hiding the system UI.
	// private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	//
	// // If set, will toggle the system UI visibility upon interaction.
	// Otherwise,
	// // will show the system UI visibility upon interaction.
	// private static final boolean TOGGLE_ON_CLICK = true;
	//
	// // The flags to pass to SystemUiHider#getInstance.
	// private static final int HIDER_FLAGS =
	// SystemUiHider.FLAG_HIDE_NAVIGATION;
	//
	// // The instance of the {@link SystemUiHider} for this activity.
	// private SystemUiHider mSystemUiHider;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// final View controlsView =
		// findViewById(R.id.fullscreen_content_controls);
		// final View contentView = findViewById(R.id.fullscreen_content);
		//
		// // Set up an instance of SystemUiHider to control the system UI for
		// // this activity.
		// mSystemUiHider = SystemUiHider.getInstance(this, contentView,
		// HIDER_FLAGS);
		// mSystemUiHider.setup();
		// mSystemUiHider
		// .setOnVisibilityChangeListener(new
		// SystemUiHider.OnVisibilityChangeListener() {
		// // Cached values.
		// int mControlsHeight;
		// int mShortAnimTime;
		//
		// @Override
		// @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		// public void onVisibilityChange(boolean visible) {
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
		// // If the ViewPropertyAnimator API is available
		// // (Honeycomb MR2 and later), use it to animate the
		// // in-layout UI controls at the bottom of the
		// // screen.
		// if (mControlsHeight == 0) {
		// mControlsHeight = controlsView.getHeight();
		// }
		// if (mShortAnimTime == 0) {
		// mShortAnimTime = getResources().getInteger(
		// android.R.integer.config_shortAnimTime);
		// }
		// controlsView
		// .animate()
		// .translationY(visible ? 0 : mControlsHeight)
		// .setDuration(mShortAnimTime);
		// } else {
		// // If the ViewPropertyAnimator APIs aren't
		// // available, simply show or hide the in-layout UI
		// // controls.
		// controlsView.setVisibility(visible ? View.VISIBLE
		// : View.GONE);
		// }
		//
		// if (visible && AUTO_HIDE) {
		// // Schedule a hide().
		// delayedHide(AUTO_HIDE_DELAY_MILLIS);
		// }
		// }
		// });
		//
		// // Set up the user interaction to manually show or hide the system
		// UI.
		// contentView.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// if (TOGGLE_ON_CLICK) {
		// mSystemUiHider.toggle();
		// } else {
		// mSystemUiHider.show();
		// }
		// }
		// });
		//
		// // Upon interacting with UI controls, delay any scheduled hide()
		// // operations to prevent the jarring behavior of controls going away
		// // while interacting with the UI.
		// findViewById(R.id.fullscreen_content_controls).setOnTouchListener(
		// mDelayHideTouchListener);

		final WebView webViewMap = (WebView) findViewById(R.id.webViewMap);
		webViewMap.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		webViewMap.getSettings().setBuiltInZoomControls(true);

		loader = new DataLoader(TehranTrafficActivity.this,
				(WebView) findViewById(R.id.webViewMap),
				(TextView) findViewById(R.id.txtStatus));
		loader.loadFile("newMap");

		final Button btnLeft = (Button) findViewById(R.id.btnLeft);
		final Button btnRight = (Button) findViewById(R.id.btnRight);
		final Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
		final Button btnBack = (Button) findViewById(R.id.btnBack);

		if (loader.fileExist("oldMap")) {
			btnLeft.setVisibility(Button.VISIBLE);
		} else {
			btnLeft.setVisibility(Button.INVISIBLE);
		}
		btnRefresh.setVisibility(Button.VISIBLE);
		btnRight.setVisibility(Button.INVISIBLE);
		btnBack.setVisibility(Button.INVISIBLE);

		btnLeft.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				btnLeft.setVisibility(Button.INVISIBLE);
				btnRight.setVisibility(Button.VISIBLE);
				loader.loadPrev();
			}
		});

		btnRight.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				btnLeft.setVisibility(Button.VISIBLE);
				btnRight.setVisibility(Button.INVISIBLE);
				loader.loadNext();
			}
		});

		btnBack.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				onCreate(null);
			}
		});

		btnRefresh.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if (loader.getStatus() == Status.FINISHED) {
					loader = new DataLoader(TehranTrafficActivity.this,
							(WebView) findViewById(R.id.webViewMap),
							(TextView) findViewById(R.id.txtStatus));
				}
				loader.execute();
				// if (loader.fileExist("oldMap"))
				btnLeft.setVisibility(Button.VISIBLE);
			}
		});

		// Toast.makeText(this, c.getFilesDir().toString(), Toast.LENGTH_LONG);

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Yes button clicked
					if (loader.getStatus() == Status.FINISHED) {
						loader = new DataLoader(TehranTrafficActivity.this,
								(WebView) findViewById(R.id.webViewMap),
								(TextView) findViewById(R.id.txtStatus));
					}
					loader.execute();
					// if (loader.fileExist("oldMap"))
					btnLeft.setVisibility(Button.VISIBLE);
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					break;
				}
			}
		};

		// checking last update time
		try {
			SharedPreferences settings = getSharedPreferences(
					"TehranTrafficMap", 0);
			// Toast.makeText(this, "1. " + settings.getString("lastUpdate",
			// ""),
			// Toast.LENGTH_LONG).show();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.US);
			Date lastUpdate = new Date();
			lastUpdate = df.parse(settings.getString("lastUpdate", ""));

			// Toast.makeText(this, "2. " + lastUpdate.toString(),
			// Toast.LENGTH_LONG).show();

			Date now = Calendar.getInstance().getTime();

			// Toast.makeText(this, "3. " + now.toString(),
			// Toast.LENGTH_LONG).show();

			lastUpdate.setMinutes(lastUpdate.getMinutes() + 5);
			// Toast.makeText(this, "4. " + lastUpdate.toString(),
			// Toast.LENGTH_LONG).show();
			if ((long) lastUpdate.getTime() < (long) now.getTime()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(getString(R.string.msg_updatemap))
						.setPositiveButton(getString(R.string.msg_yes),
								dialogClickListener)
						.setNegativeButton(getString(R.string.msg_no),
								dialogClickListener).show();
			}
		} catch (Exception ex) {
			// Toast.makeText(this, "5. " + ex.getMessage(),
			// Toast.LENGTH_LONG).show();
		}
	}

	// @Override
	// protected void onPostCreate(Bundle savedInstanceState) {
	// super.onPostCreate(savedInstanceState);
	//
	// // Trigger the initial hide() shortly after the activity has been
	// // created, to briefly hint to the user that UI controls
	// // are available.
	// delayedHide(100);
	// }
	//
	// /**
	// * Touch listener to use for in-layout UI controls to delay hiding the
	// * system UI. This is to prevent the jarring behavior of controls going
	// away
	// * while interacting with activity UI.
	// */
	// View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener()
	// {
	// @Override
	// public boolean onTouch(View view, MotionEvent motionEvent) {
	// if (AUTO_HIDE) {
	// delayedHide(AUTO_HIDE_DELAY_MILLIS);
	// }
	// return false;
	// }
	// };
	//
	// Handler mHideHandler = new Handler();
	// Runnable mHideRunnable = new Runnable() {
	// @Override
	// public void run() {
	// mSystemUiHider.hide();
	// }
	// };
	//
	// // Schedules a call to hide() in [delay] milliseconds, canceling any
	// // previously scheduled calls.
	// private void delayedHide(int delayMillis) {
	// mHideHandler.removeCallbacks(mHideRunnable);
	// mHideHandler.postDelayed(mHideRunnable, delayMillis);
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itmVideo:
			Toast.makeText(this, getString(R.string.msg_highdensity),
					Toast.LENGTH_LONG).show();
			showVideo();
			break;
		case R.id.itmMetro:
			showMetroMap();
			break;
		case R.id.itmPlane:
			showTrafficPlane();
			break;
		case R.id.itmHelp:
			showHelpDialog();
			break;
		case R.id.itmAbout:
			showAboutDialog();
			break;
		case R.id.itmExit:
			finish();
			break;
		}
		return true;
	}

	private void showAboutDialog() {

		final Dialog dAbout = new Dialog(TehranTrafficActivity.this);
		dAbout.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dAbout.setContentView(R.layout.aboutdialog);
		dAbout.setTitle(getString(R.string.app_about_title));
		dAbout.setCancelable(true);
		dAbout.setCanceledOnTouchOutside(true);
		dAbout.show();

		dAbout.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.logo);
	}

	private void showHelpDialog() {

		final Dialog dHelp = new Dialog(TehranTrafficActivity.this);
		dHelp.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dHelp.setContentView(R.layout.helpdialog);
		dHelp.setTitle(getString(R.string.app_help_title));
		dHelp.setCancelable(true);
		dHelp.setCanceledOnTouchOutside(true);
		dHelp.show();

		dHelp.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.help);
	}

	private void showTrafficPlane() {
		final Button btnLeft = (Button) findViewById(R.id.btnLeft);
		final Button btnRight = (Button) findViewById(R.id.btnRight);
		final Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
		final Button btnBack = (Button) findViewById(R.id.btnBack);

		btnLeft.setVisibility(Button.INVISIBLE);
		btnRight.setVisibility(Button.INVISIBLE);
		btnRefresh.setVisibility(Button.INVISIBLE);
		btnBack.setVisibility(Button.VISIBLE);

		loader.loadPlane();
	}

	private void showVideo() {
		final Button btnLeft = (Button) findViewById(R.id.btnLeft);
		final Button btnRight = (Button) findViewById(R.id.btnRight);
		final Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
		final Button btnBack = (Button) findViewById(R.id.btnBack);

		btnLeft.setVisibility(Button.INVISIBLE);
		btnRight.setVisibility(Button.INVISIBLE);
		btnRefresh.setVisibility(Button.INVISIBLE);
		btnBack.setVisibility(Button.VISIBLE);

		loader.loadVideo();
	}

	private void showMetroMap() {
		final Button btnLeft = (Button) findViewById(R.id.btnLeft);
		final Button btnRight = (Button) findViewById(R.id.btnRight);
		final Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
		final Button btnBack = (Button) findViewById(R.id.btnBack);

		btnLeft.setVisibility(Button.INVISIBLE);
		btnRight.setVisibility(Button.INVISIBLE);
		btnRefresh.setVisibility(Button.INVISIBLE);
		btnBack.setVisibility(Button.VISIBLE);

		loader.loadMetro();
	}

	@Override
	protected void onResume() {
		super.onResume();
		doubleBackToExitPressedOnce = false;
	}

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}
		doubleBackToExitPressedOnce = true;
		Toast.makeText(this, R.string.msg_exit, Toast.LENGTH_SHORT).show();

		Timer t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2500);

	}

	@Override
	protected void onStart() {
		super.onStart();

		EasyTracker.getInstance().activityStart(this); // Google Analytic
	}

	@Override
	protected void onStop() {
		super.onStop();

		EasyTracker.getInstance().activityStop(this); // Google Analytic
	}

}