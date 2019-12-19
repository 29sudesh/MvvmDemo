package com.sb.ubergodriver.fcm;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService  extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";


    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

//        Toast.makeText(MyFirebaseInstanceIDService.this,refreshedToken,Toast.LENGTH_LONG).show();
    }
}
