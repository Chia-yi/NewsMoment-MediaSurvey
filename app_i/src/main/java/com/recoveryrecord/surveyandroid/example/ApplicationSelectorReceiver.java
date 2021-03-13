package com.recoveryrecord.surveyandroid.example;//package com.example.test;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class ApplicationSelectorReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        System.out.println(action);
        Log.d("log: hello", "action");
        String doc_time = "", doc_date = "", share_field = "";
        if (intent.getExtras() != null) {
            doc_time = intent.getExtras().getString("doc_time");
            doc_date = intent.getExtras().getString("doc_date");
            share_field = intent.getExtras().getString("share_field");
//            Log.d("log: doc_name", intent.getExtras().getString("doc_time"));
//            Log.d("log: doc_name", intent.getExtras().getString("doc_date"));
//            Log.d("log: index", intent.getExtras().getString("0"));
        }

        for (String key : Objects.requireNonNull(intent.getExtras()).keySet()) {
            try {
                ComponentName componentInfo = (ComponentName) intent.getExtras().get(key);
                PackageManager packageManager = context.getPackageManager();
                assert componentInfo != null;
                final String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(componentInfo.getPackageName(), PackageManager.GET_META_DATA));
                Log.d("log: Selected App Name", appName);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final DocumentReference rbRef = db.collection(Build.ID).document(doc_date).collection("reading_behaviors").document(doc_time);
//                rbRef.update("share", FieldValue.arrayRemove(share_field));
//                rbRef.update("share", FieldValue.arrayUnion(share_field + " " + appName));
                rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("log: firebase", "Success");
                                List<String> share_result = (List<String>) document.get("share");
                                share_result.remove(share_result.size() - 1);
                                share_result.add(appName);
//                                share_result.set(0,"none");
                                rbRef.update("share", share_result)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("log: firebase", "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("log: firebase", "Error updating document", e);
                                            }
                                        });
                            } else {
                                Log.d("log: firebase", "No such document");
                            }
                        } else {
                            Log.d("log: firebase", "get failed with ", task.getException());
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("log: hello", "456");
            }
        }
        Log.d("log: hello", "123");
    }
}