package com.tehran.traffic;

/**
 * Created by Mohsen on 8/14/15.
 */

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app, such as
 * the {@link Tracker}.
 */
@ReportsCrashes(
        reportType = HttpSender.Type.JSON,
        httpMethod = HttpSender.Method.PUT,
//        formUri = "http://apps.karinaco.com:5984/acra-karina/_design/acra-storage/_update/report",
//        formUriBasicAuthLogin = "karina", // optional
//        formUriBasicAuthPassword = "jasdjioweijsdnb2234", // optional
//        formKey = "", // This is required for backward compatibility but not used
        formUri = "https://collector.tracepot.com/1bb22253",
        mode = ReportingInteractionMode.DIALOG,
        resToastText = R.string.crash_toast_text, // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds
        resDialogText = R.string.crash_dialog_text,
        resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
        resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. When defined, adds a user text field input with this text resource as a label
        resDialogEmailPrompt = R.string.crash_user_email_label, // optional. When defined, adds a user email text entry with this text resource as label. The email address will be populated from SharedPreferences and will be provided as an ACRA field if configured.
        resDialogOkToast = R.string.crash_dialog_ok_toast // optional. displays a Toast message when the user accepts to send a report.
)
public class AnalyticsApplication extends Application {
    //Logging TAG
    private static final String TAG = "AnalyticsApplication";

    private Tracker mTracker;

    public AnalyticsApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // The following line triggers the initialization of ACRA
        ACRA.init(this);

    }

    public synchronized Tracker getTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.analytics_app_tracker);
        }
        return mTracker;
    }
}