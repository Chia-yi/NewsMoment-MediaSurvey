///*
// * Copyright 2019 Google LLC
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.recoveryrecord.surveyandroid.example;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//
//import java.util.Arrays;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
///**
// * Displays rationale for allowing the activity recognition permission and allows user to accept
// * the permission. After permission is accepted, finishes the activity so main activity can
// * show transitions.
// */
//public class ActivityRecognitionPermissionRationalActivity extends AppCompatActivity implements
//        ActivityCompat.OnRequestPermissionsResultCallback {
//
//    private static final String TAG = "ActivityRecognitionPermissionRationalActivity";
//
//    /* Id to identify Activity Recognition permission request. */
//    private static final int PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 45;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // If permissions granted, we start the main activity (shut this activity down).
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
//                == PackageManager.PERMISSION_GRANTED) {
//            finish();
//        }
//
//        setContentView(R.layout.activity_permission_rational);
//    }
//
//    @SuppressLint("LongLogTag")
//    @RequiresApi(api = Build.VERSION_CODES.Q)
//    public void onClickApprovePermissionRequest(View view) {
//        Log.d(TAG, "onClickApprovePermissionRequest()");
//
//        // TODO: Review permission request for activity recognition.
//        ActivityCompat.requestPermissions(
//                this,
//                new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
//                PERMISSION_REQUEST_ACTIVITY_RECOGNITION);
//    }
//
//    @SuppressLint("LongLogTag")
//    public void onClickDenyPermissionRequest(View view) {
//        Log.d(TAG, "onClickDenyPermissionRequest()");
//        finish();
//    }
//
//    /*
//     * Callback received when a permissions request has been completed.
//     */
//    @SuppressLint("LongLogTag")
//    @Override
//    public void onRequestPermissionsResult(
//            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        String permissionResult = "Request code: " + requestCode + ", Permissions: " +
//                Arrays.toString(permissions) + ", Results: " + Arrays.toString(grantResults);
//
//        Log.d(TAG, "onRequestPermissionsResult(): " + permissionResult);
//
//        if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
//            // Close activity regardless of user's decision (decision picked up in main activity).
//            finish();
//        }
//    }
//}
