package com.recoveryrecord.surveyandroid.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_VALUE_BOOT_UP;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_VALUE_RESTART;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.TEST_USER_COLLECTION;

public class BootUpReceiver extends BroadcastReceiver {
    private SharedPreferences pref;
//    private appDatabase db;
//    UserDataRecord userRecord;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
//        pref = context.getSharedPreferences("test", MODE_PRIVATE);
//        db = appDatabase.getDatabase(context);
//        userRecord = db.userDataRecordDao().getLastRecord();
        String intent_action = intent.getAction();
//        CSVHelper.storeToCSV("AlarmCreate.csv", "intent_action: " + intent_action);
        if (intent_action.equals("android.intent.action.BOOT_COMPLETED")) {
            /* 收到廣播後要做的事 */
            Calendar cal = Calendar.getInstance();
//            int day = cal.get(Calendar.DAY_OF_MONTH); //開機日期
//            int ShutDown_Day = pref.getInt("ShutDown_Day", 0); //關機日期
            Log.d("BootOrShutDown", "restart service");
            context.startService(new Intent(context, NewsNotificationService.class));

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> log_service = new HashMap<>();
            log_service.put(NEWS_SERVICE_TIME, Timestamp.now());
            log_service.put(NEWS_SERVICE_STATUS_KEY, NEWS_SERVICE_STATUS_VALUE_RESTART);
            log_service.put(NEWS_SERVICE_CYCLE_KEY, NEWS_SERVICE_CYCLE_VALUE_BOOT_UP);
            db.collection(TEST_USER_COLLECTION)
                    .document(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID))
                    .collection(NEWS_SERVICE_COLLECTION)
                    .document(String.valueOf(Timestamp.now().toDate()))
                    .set(log_service);
        }
    }
}