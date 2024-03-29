 package com.recoveryrecord.surveyandroid.example;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.messaging.FirebaseMessaging;
import com.recoveryrecord.surveyandroid.example.model.Pagers;
//import com.recoveryrecord.surveyandroid.example.receiever.ActivityRecognitionReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.BlueToothReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.NetworkChangeReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.ScreenStateReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import javax.annotation.Nullable;

//import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_PARCELABLE;


//import com.google.firebase.FirebaseApp;

public class TestNewsAllActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    //temp for notification
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private static final String TAG = "TestNewsAllActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.document("server_push_notifications/start");
    private CollectionReference noteRefqq = db.collection("server_push_notifications");
    boolean doubleBackToExitPressedOnce = false;

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private TextView user_phone, user_id, user_name;

    Intent mServiceIntent;
    private NewsNotificationService mYourService;


    private static final HashMap<String, String> media_hash = new HashMap<String, String>();
    static{
        media_hash.put("中時", "chinatimes");
        media_hash.put("中央社", "cna");
        media_hash.put("華視", "cts");
        media_hash.put("東森", "ebc");
        media_hash.put("自由時報", "ltn");
        media_hash.put("風傳媒", "storm");
        media_hash.put("聯合", "udn");
        media_hash.put("ettoday", "ettoday");
    }
    //sensor part
    BlueToothReceiver _BluetoothReceiver;
    //    RingModeReceiver _RingModeReceiver;
    private AudioManager myAudioManager;
    private boolean activityTrackingEnabled;
    private List<ActivityTransition> activityTransitionList;
    //DETECTED ACTIVITY
    public GoogleApiClient mApiClient;
    public ActivityRecognitionClient mActivityRecognitionClient;
    NetworkChangeReceiver _NetworkChangeReceiver;
    ScreenStateReceiver _ScreenStateReceiver;
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initial value for collection
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        DocumentReference docIdRef = db.collection("test_users").document(device_id);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                    } else {
                        Log.d(TAG, "Document does not exist!");
                        Map<String, Object> first = new HashMap<>();
                        first.put("test", Timestamp.now());
                        db.collection("test_users")
                                .document(device_id)
                                .set(first);
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
        //check preference
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = sharedPrefs.edit();
//        editor.clear();
//        editor.commit();
        //notification media_select
        Set<String> selections = sharedPrefs.getStringSet("media_select", null);
        if (selections==null){
            Toast.makeText(this, "趕快去設定選擇想要的媒體吧~", Toast.LENGTH_LONG).show();
        } else {
//            String[] selected = selections.toArray(new String[] {});
            Log.d("lognewsselect", Arrays.toString(new Set[]{selections}));
        }
        //some initial
        //FirebaseApp.initializeApp();
        setContentView(R.layout.activity_all_news);
        //navi
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        View header = navigationView.getHeaderView(0);
        user_phone = (TextView) header.findViewById(R.id.textView_user_phone);
        user_phone.setText(Build.MODEL);
        user_id = (TextView) header.findViewById(R.id.textView_user_id);
        user_id.setText(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        user_name = (TextView) header.findViewById(R.id.textView_user_name);
        String signature = sharedPrefs.getString("signature", null);
        if (signature==null){
//            Toast.makeText(this, "趕快去設定選擇想要的媒體吧~", Toast.LENGTH_LONG).show();
            user_name.setText("使用者名稱");
        } else {
            user_name.setText(signature);
        }

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("即時新聞");

        //check notification service running
        mYourService = new NewsNotificationService();
//        mServiceIntent = new Intent(this, mYourService.getClass());
        mServiceIntent = new Intent(this, NewsNotificationService.class);
        if (!isMyServiceRunning(mYourService.getClass())) {
            Toast.makeText(this, "service failed", Toast.LENGTH_LONG).show();
            startService(mServiceIntent);
        } else {
            Toast.makeText(this, "service running", Toast.LENGTH_LONG).show();
        }
        ArrayList<View> mPages = new ArrayList<>();
        //notification media_rank
        Set<String> ranking = sharedPrefs.getStringSet("media_rank", null);
        if (ranking==null){
            Set<String> set = new HashSet<String>();
            set.add("中時 1");
            set.add("中央社 2");
            set.add("華視 3");
            set.add("東森 4");
            set.add("自由時報 5");
            set.add("風傳媒 6");
            set.add("聯合 7");
            set.add("ettoday 8");
            SharedPreferences.Editor edit = sharedPrefs.edit();
            edit.clear();
            edit.putStringSet("media_rank", set);
            edit.apply();
            Toast.makeText(this, "趕快去設定調整首頁app排序八~", Toast.LENGTH_LONG).show();
            mPages.add(new Pagers(this, (1), "中時", "chinatimes", "生活"));
            mPages.add(new Pagers(this, (1), "中央社", "cna", "生活"));
            mPages.add(new Pagers(this, (1), "華視", "cts", "生活"));//cts
            mPages.add(new Pagers(this, (1), "東森", "ebc", "生活"));
            mPages.add(new Pagers(this, (1), "自由時報", "ltn", "生活"));
            mPages.add(new Pagers(this, (1), "風傳媒", "storm", "生活"));
            mPages.add(new Pagers(this, (1), "聯合", "udn", "生活"));
            mPages.add(new Pagers(this, (1), "ettoday", "ettoday", "生活"));
        } else {
//            String[] ranking_result = ranking.toArray(new String[] {});
            Log.d("lognewsselect", Arrays.toString(new Set[]{ranking}));
            for (int i = 1; i<=8; i++){
                for (String r : ranking){
                    List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
//            Log.d("lognewsselect", out.get(0));
                    if(Integer.parseInt(out.get(1))==i){
//                    Log.d("lognewsselect", out.get(0));
//                    Log.d("lognewsselect", String.valueOf(Integer.parseInt(out.get(1))));
//                i++;
                        mPages.add(new Pagers(this, (1), out.get(0), media_hash.get(out.get(0)), "生活"));
                        continue;
                    }
                }
            }
        }

        ViewPager viewPager = findViewById(R.id.mViewPager);
        TabLayout tab = findViewById(R.id.tab);
        NewsMediaPagerAdapter newsMediaPagerAdapter = new NewsMediaPagerAdapter(mPages, this);

        tab.setupWithViewPager(viewPager);
        viewPager.setAdapter(newsMediaPagerAdapter);
        viewPager.setCurrentItem(0);//指定跳到某頁，一定得設置在setAdapter後面
        //sensor
        //BLUETOOTH
        _BluetoothReceiver = new BlueToothReceiver();
        _BluetoothReceiver.registerBluetoothReceiver(this);
        //BLUETOOTH--要偵測附近裝置需要啟動位置權限
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //Android M Permission check
            if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    }
                });
                builder.show();
            }
        }
        //DETECTED ACTIVITY

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .build();
        mApiClient.connect();

        //Network
        _NetworkChangeReceiver = new NetworkChangeReceiver();
        _NetworkChangeReceiver.registerNetworkReceiver(this);

        //Screen
        _ScreenStateReceiver = new ScreenStateReceiver();
//        _ScreenStateReceiver.registerScreenStateReceiver(this);


    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_setting :
                Log.d("log: navigation", "nav_setting " + item.getItemId());
                Intent intent_setting = new Intent(TestNewsAllActivity.this, SettingsActivity.class);
                startActivity(intent_setting);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_progressing :
                Log.d("log: navigation", "nav_progressing " + item.getItemId());
                Intent intent_db = new Intent(TestNewsAllActivity.this, SurveyProgressActivity.class);
                startActivity(intent_db);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_history :
                Log.d("log: navigation", "nav_history " + item.getItemId());
                Intent intent_noti = new Intent(TestNewsAllActivity.this, ReadHistoryActivity.class);
                startActivity(intent_noti);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_reschedule :
                Log.d("log: navigation", "nav_reschedule " + item.getItemId());
//                scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 1000 );
//                scheduleNotification(getNotification("0143b739b1c33d46cd18b6af12b2d5b2", "ettoday", "wa_title" ), 5000 );
//                scheduleNotification_repeat(getNotification_esm("time"));
//                Toast.makeText(this, "開始每小時固定發送esm~", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "發送esm~", Toast.LENGTH_LONG).show();
//                Intent intent_base = new Intent(TestNewsAllActivity.this, TestBasicActivity.class);
//                startActivity(intent_base);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
//            case R.id.nav_contact :
//                Log.d("log: navigation", "nav_contact " + item.getItemId());
//                Toast.makeText(this, "目前什麼都沒有拉~", Toast.LENGTH_LONG).show();
//                Intent intent_ems = new Intent(TestNewsAllActivity.this, NewsHybridActivity.class);
//                startActivity(intent_ems);
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return true;
            default :
                return false;
        }

//        return false;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d ("Servicestatus", "Running");
                return true;
            }
        }
        Log.d ("Servicestatus", "Not running");
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }

    }

//
//    @Override
//    public boolean onCreateOptionsMenu (Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main_news, menu);
//        //getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public boolean onOptionsItemSelected (MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_5 :
//                scheduleNotification(getNotification("0143b739b1c33d46cd18b6af12b2d5b2", "ettoday", "wa_title" ), 5000 );
//                scheduleNotification_repeat(getNotification_esm("time"));
//                return true;
//            case R.id.action_10 :
//                Intent intent_ems = new Intent(TestNewsAllActivity.this, SurveyActivity.class);
//                startActivity(intent_ems);
//                return true;
//            case R.id.action_30 :
//                Intent intent_noti_db = new Intent(TestNewsAllActivity.this, NotificationDbViewActivity.class);
//                startActivity(intent_noti_db);
////                scheduleNotification(getNotification("news (30 second delay)" ), 30000 );
////                scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 30000 );
//                return true;
//            default :
//                return super.onOptionsItemSelected(item);
//        }
//    }
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void scheduleNotification (Notification notification, int delay) {
////        int nid = (int) System.currentTimeMillis();
////        Log.d("log: notification", "news id" + nid);
//        Intent notificationIntent = new Intent(this, NotificationListenerNews.class);
//        notificationIntent.putExtra(NotificationListenerNews.NOTIFICATION_ID, 1 ) ;
//        notificationIntent.putExtra(NotificationListenerNews.NOTIFICATION, notification) ;
//        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        long futureInMillis = SystemClock.elapsedRealtime() + delay;
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        assert alarmManager != null;
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
//    }
//    private Notification getNotification (String news_id, String media, String title) {
//        int nid = (int) System.currentTimeMillis();
//        Log.d("log: notification", "news id" + nid);
//        Intent intent_news = new Intent();
//        intent_news.setClass(TestNewsAllActivity.this, NewsModuleActivity.class);
//        intent_news.putExtra("trigger_from", "Notification");
//        intent_news.putExtra("news_id", news_id);
//        intent_news.putExtra("media", media);
////        intent_news.putExtra("media", "Notification");
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, nid, intent_news, 0);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
//        builder.setContentTitle(title);
//        builder.setContentText(media);
//        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
//        builder.setContentIntent(pendingIntent);
//        builder.setAutoCancel(true);
//        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
//        return builder.build() ;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void scheduleNotification_repeat (Notification notification) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 1);
//        int nid = (int) System.currentTimeMillis();
//        Log.d("logesm", "news id" + nid);
//        Intent notificationIntent = new Intent(this, NotificationListenerNews.class);
//        notificationIntent.putExtra(NotificationListenerESM.NOTIFICATION_ID, 1 ) ;
//        notificationIntent.putExtra(NotificationListenerESM.NOTIFICATION, notification) ;
////        notificationIntent.putExtra("noti_time", nid) ;
//        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////        long futureInMillis = SystemClock.elapsedRealtime() + delay;
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        assert alarmManager != null;
////        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 3600, pendingIntent);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60, pendingIntent);//60 min
////        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000 * 20, 1000 * 20, pendingIntent);
//    }
    private Notification getNotification_esm (String content) {
        //replace content with time
        Date date = new Date(System.currentTimeMillis());
        String esm_id = "";
        esm_id = String.valueOf(date);
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        String time_now = formatter.format(date);

        int nid = (int) System.currentTimeMillis();
        Log.d("logesm", "esm id " + nid + " " + Timestamp.now());
        Intent intent_esm = new Intent();
        intent_esm.setClass(TestNewsAllActivity.this, SurveyActivity.class);
        intent_esm.putExtra("trigger_from", "Notification");
        intent_esm.putExtra("esm_id", esm_id);
        intent_esm.putExtra("noti_timestamp", Timestamp.now());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, nid, intent_esm, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
        builder.setContentTitle("ESM");
        builder.setContentText("是時候填寫問卷咯~");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Map<String, Object> esm = new HashMap<>();
        esm.put("noti_time", time_now);
        esm.put("noti_timestamp", Timestamp.now());
        db.collection("test_users")
                .document(device_id)
                .collection("esms")
                .document(esm_id)
                .set(esm);
        return builder.build() ;
    }
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void scheduleNotification_esm (Notification notification, int delay) {
//        Intent notificationIntent = new Intent(this, NotificationListenerNews.class);
//        notificationIntent.putExtra(ESM_CHANNEL_ID, 1 ) ;
//        notificationIntent.putExtra(DEFAULT_ESM_PARCELABLE, notification) ;
//        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        long futureInMillis = SystemClock.elapsedRealtime() + delay;
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        assert alarmManager != null;
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
//    }
    //sensor
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "DetectedActivityConnected");
//        Intent intent = new Intent( this, ActivityRecognitionReceiver.class );
//        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
//        Task<Void> task = ActivityRecognition.getClient(this).requestActivityUpdates(30000, pendingIntent);
        //mApiClient.disconnect();
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    @Override
    public void onDestroy() {
        _BluetoothReceiver.unregisterBluetoothReceiver(this);
        _NetworkChangeReceiver.unregisterNetworkReceiver(this);
//        _ScreenStateReceiver.unregisterScreenStateReceiver(this);
        super.onDestroy();
    }

}
