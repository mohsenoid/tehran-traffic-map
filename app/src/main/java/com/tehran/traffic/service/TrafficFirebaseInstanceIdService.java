package com.tehran.traffic.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mirhoseini.appsettings.AppSettings;

/**
 * Created by Mohsen on 04/12/2016.
 */

public class TrafficFirebaseInstanceIdService extends FirebaseInstanceIdService {
    public static final String TOKEN_ID = "token_id";
    public static final String TAG = "FirebaseService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        AppSettings.setValue(getApplicationContext(), TOKEN_ID, refreshedToken);

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        // TODO: Implement this method to send any registration to your app's servers.
//        sendRegistrationToServer(refreshedToken);
    }
}
