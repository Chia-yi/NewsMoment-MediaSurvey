package com.recoveryrecord.surveyandroid.example;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.recoveryrecord.surveyandroid.example.Utils.facebook;
import static com.recoveryrecord.surveyandroid.example.Utils.line;
import static com.recoveryrecord.surveyandroid.example.Utils.messenger;
import static com.recoveryrecord.surveyandroid.example.Utils.ptt;
import static com.recoveryrecord.surveyandroid.example.Utils.youtube;
import static com.recoveryrecord.surveyandroid.example.Utils.screen_on;

public class MyAccessibilityService extends AccessibilityService{
    final String TAG = "Accessibility service";

    private String MyEventText = "";
    private long[] vibrate_effect = {0, 200};
    private static String CHANNEL_ID = "AccessibilityService";
    public static final int Accessibility_ID = 6;

    private String name = "";
    private int log_counter = 0;
    Handler mMainThread = new Handler();
    private String[] NewsPack;
    private String[] NewsName;
    private String[] WebPack;
    private String[] WebEvent;
    private String[] HomePackage;
    private String[] AppPack;
    private String[] PttPack;
    private PowerManager mPowerManager;
    private boolean exit_room = true;
    private boolean home = false;
    private boolean random_first = true;
    private boolean watch_video = false;
    private boolean home_first = false;
    private boolean[] ScreenCaptureTransition = {false, false, false};
    private boolean line_interrupted_by_messenger = false;
    private int appear = 0;
    private SharedPreferences pref;
    private int googlebox_count = 0;
    private int youtube_count = 0;
    private int agree_interval;
    public static  Intent intent;
    private boolean[] state_array = {false, false, false, false, false, false, false, false, false, false};
    private String date;
    long lastid;
    private long messenger_duration = 0;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mMainThread.postDelayed(this, 5 * 1000);
        }
    };

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
//        AccessibilityEvent.eventTypeToString(event.getEventType()).contains("CLICK")
//        if(true){
//            AccessibilityNodeInfo nodeInfo = event.getSource();
//            dfs(nodeInfo);
//        }

        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.d(TAG, "TYPE_VIEW_CLICKED");
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                Log.d(TAG, "TYPE_VIEW_LONG_CLICKED");
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                Log.d(TAG, "TYPE_VIEW_FOCUSED");
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                Log.d(TAG, "TYPE_VIEW_SELECTED");
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                Log.d(TAG, "TYPE_VIEW_TEXT_CHANGED");
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.d(TAG, "TYPE_WINDOW_STATE_CHANGED");
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                Log.d(TAG, "TYPE_NOTIFICATION_STATE_CHANGED");
                break;
//            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
//                Log.d(TAG, "TYPE_TOUCH_EXPLORATION_GESTURE_START");
//                break;
//            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
//                Log.d(TAG, "TYPE_TOUCH_EXPLORATION_GESTURE_END");
//                break;
//            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
//                Log.d(TAG, "TYPE_VIEW_HOVER_ENTER");
//                break;
//            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
//                Log.d(TAG, "TYPE_VIEW_HOVER_EXIT");
//                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                Log.d(TAG, "TYPE_VIEW_SCROLLED");
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                Log.d(TAG, "TYPE_VIEW_TEXT_SELECTION_CHANGED");
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Log.d(TAG, "TYPE_WINDOW_CONTENT_CHANGED");
                break;
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                Log.d(TAG, "TYPE_ANNOUNCEMENT");
                break;
//            case AccessibilityEvent.TYPE_GESTURE_DETECTION_START:
//                Log.d(TAG, "TYPE_GESTURE_DETECTION_START");
//                break;
//            case AccessibilityEvent.TYPE_GESTURE_DETECTION_END:
//                Log.d(TAG, "TYPE_GESTURE_DETECTION_END");
//                break;
//            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START:
//                Log.d(TAG, "TYPE_TOUCH_INTERACTION_START");
//                break;
//            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END:
//                Log.d(TAG, "TYPE_TOUCH_INTERACTION_END");
//                break;
//            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
//                Log.d(TAG, "TYPE_VIEW_ACCESSIBILITY_FOCUSED");
//                break;
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                Log.d(TAG, "TYPE_WINDOWS_CHANGED");
                break;
//            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED:
//                Log.d(TAG, "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED");
//                break;
        }

        //pref.edit().putString("AccessibilityTime", ScheduleAndSampleManager.getCurrentTimeString()).apply(); // 12/14

        AccessibilityNodeInfo nodeInfo = event.getSource();
        if(nodeInfo != null)
        {
            Log.d(TAG, "NodeInfo: " + nodeInfo);
            Log.d(TAG, "NodeInfo: " + nodeInfo.getContentDescription());
            Log.w(TAG, "Start DFS");
            dfs(nodeInfo);
        }
        int EventType = event.getEventType();
        String EventText = getEventText(event);
        //String EventPackage = event.getPackageName().toString();
        //Log.d(TAG, "The package name is: " + EventPackage);
        Log.d(TAG, "The event message is: " + EventText);

//        String last_active = getSharedPreferences("test", MODE_PRIVATE)
//                .getString("ForegroundApp", "");
        //intent = new Intent(getApplicationContext(), ScreenCaptureActivity.class);

        String now_active = printForegroundTask();
        screen_on = getScreenStatus();

        //Log.d(TAG, "Screen Status:" + screen_on);
        //boolean screenshot = isMyServiceRunning(com.example.accessibility_detect.ScreenCapture.class);
        //String time = pref.getString("agree_dialog_time","0");

        /*if(time != null) {
            agree_interval = Integer.parseInt(time);
        }
        else{
            agree_interval = 0;
        }
        MyEventText = "NA";*/

        if(event.getPackageName() != null) {
            home = ((getEventText(event).contains("螢幕") && !getEventText(event).contains("全螢幕模式") && !getEventText(event).contains("全螢幕")) || getEventText(event).contains("主畫面") || getEventText(event).matches("第.*頁，共.*頁")
                    || getEventText(event).contains("預設頁面") || getEventText(event).contains("桌面") || !screen_on);
            if(event.getPackageName().equals("com.android.systemui"))home = false;
//            Log.d(TAG, "screen on: " + screen_on + " random first: " + random_first);
            if(!screen_on){
                home = true;
                if(random_first) {
                    /*Random_session_num = (int) (Math.random() * 3 + 1);
                    CSVHelper.storeToCSV("ESM_random_number.csv", "because screen is off, reset random number to: " + Random_session_num);
                    Random_session_counter = 0;*/
//                    pref.edit().putLong("Phone_SessionID", -1).apply();
//                    Log.d(TAG, "Phone session id: -1");
                    random_first = false;
                }
            }
            else{
                if(!random_first) {
//                    phone_sessionid = pref.getLong("Phone_SessionID", 1);
//                    phone_sessionid++;
//                    pref.edit().putLong("Phone_SessionID", phone_sessionid).apply();
//                    Log.d(TAG, "Phone session id: " + phone_sessionid);
                    random_first = true;
                }
            }

            //StopScreenshotDetecter(screenshot, EventType, EventText, EventPackage, now_active, nodeInfo);

            /*IsGoogleRunning(EventType, EventText, EventPackage, intent, screenshot, now_active);
            Log.d(TAG, "Google now: " + google);

            IsFacebookRunning(EventType, EventText, EventPackage, intent, screenshot);

            IsLineMesRunning(EventType, EventText, EventPackage, nodeInfo, screenshot, now_active);
            //Log.d(TAG, "Line is : " + String.valueOf(line));

            Log.d(TAG, "Chrome: " + chrome);
            IsWebBrowserRunning(EventType, EventText, EventPackage, intent, screenshot, now_active);

            IsInstagramRunning(EventType, EventText, EventPackage, intent, screenshot);
            //Log.d(TAG, "Instagram is : " + String.valueOf(instagram));

            IsYoutubeRunning(EventType, EventText, EventPackage, intent, screenshot);
            //Log.d(TAG, "Youtube is : " + String.valueOf(youtube));

            IsNewsAppRunning(EventType, EventText, EventPackage, now_active, intent, screenshot);
            //Log.d(TAG, "NewsApp is : " + String.valueOf(news));

            IsLineRunning(EventType, EventText, EventPackage, intent, screenshot, nodeInfo);
            //Log.d(TAG, "Today is : " + String.valueOf(line));

            IsPTTAppRunning(EventType, EventText, EventPackage, now_active, screenshot, event);
            //Log.d(TAG, "PTTApp is : " + String.valueOf(ptt));

            Log.d(TAG, "My Event is : " + MyEventText);

            IsMessengerRunning(EventType, EventText, EventPackage, nodeInfo, now_active, screenshot);

            Log.d(TAG, "Messenger is : " + String.valueOf(messenger));
            */

            String extra = "";
            if(nodeInfo != null)
            {
                if(nodeInfo.getContentDescription() != null)
                {
                    extra = nodeInfo.getContentDescription().toString();
                }
            }
            //DetectLastForeground(EventType, EventText, EventPackage, extra);

            //SaveLineLastSelected(EventType, EventText, EventPackage, now_active, nodeInfo);


            pref.edit()
                    .putString("ForegroundApp", now_active)
                    .apply();
        }
        if(nodeInfo != null) {
            nodeInfo.recycle();
        }
    }

    public void dfs(AccessibilityNodeInfo info){
        if(info == null)
            return;
        if(info.getText() != null && info.getText().length() > 0){
            if(info.getText().toString().contains("新聞") && info.getText().toString().length() < 30)
            {
                Log.d(TAG, "DFS news: " + info.getText().toString());
//                    URLtemp.add(EventText);
//                if(apptimesStreamGenerator!=null) {
//                    apptimesStreamGenerator.updateapptimes("PTTtitle");
//                }
                //WriteToFile(info.getText().toString(), "PTT");
                Log.d(TAG, "News In PTT: " + info.getText().toString());
            }
            Log.d(TAG, "dfs: " + info.getText() + " class: "+info.getClassName());
        }

        for(int i=0;i<info.getChildCount();i++){
            AccessibilityNodeInfo child = info.getChild(i);
            dfs(child);
            if(child != null){
                child.recycle();
            }
        }
    }

    int lastEvent = 0;
    boolean isTyping = false;
    int eventCounter = 0;
    String UserInput = "";
    int finishCount = 0;

    private void getUserInput(int EventType, String EventText, String EventPackage, AccessibilityEvent e){
//        UserInput = "";

        if(Arrays.asList(AppPack).contains(EventPackage) || Arrays.asList(WebPack).contains(EventPackage) || Arrays.asList(NewsPack).contains(EventPackage)) {
            Log.d(TAG, "get user input: " + isTyping + " " + eventCounter);

            if (EventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
                Log.d("Semantic", "Source: " + e.getSource());
                Log.d("Semantic", "Class Name: " + e.getClassName());
                Log.d("Semantic", "Event Time: " + e.getEventTime());
                Log.d("Semantic", "Text: " + e.getText());
                Log.d("Semantic", "Before Text: " + e.getBeforeText());
                Log.d("Semantic", "From Index: " + e.getFromIndex());
                Log.d("Semantic", "Add Count: " + e.getAddedCount());
                Log.d("Semantic", "Remove Count: " + e.getRemovedCount());
                StringBuilder sb = new StringBuilder();
                for (CharSequence s : e.getText()) {
                    sb.append(s);
                }
                EventText =  sb.toString();

                if(e.getBeforeText().equals("")){
                    Log.d("Semantic", "Store: " + UserInput);
                    if(!UserInput.equals("")){
//                        CSVHelper.storeToCSV("Semantic.csv", UserInput);
                        //prepareApi();
                        //analyzeSentiment(UserInput);
//                        analyzeEntities(UserInput);
//                        analyzeSyntax(UserInput);
                    }
                }
                if(e.getRemovedCount() == 0){
                    UserInput = EventText;
                    Log.d("Semantic", "This is input: " + UserInput);
                }
                Log.d("Semantic", "----------------------");
            }
            lastEvent = EventType;
        }
    }

    private void IsFacebookRunning(int EventType, String EventText, String EventPackage, Intent service_intent, boolean screenshot)
    {
        Log.d(TAG, "Cancel: " + Utils.cancel);
        Log.d(TAG, "Screen shot: " + screenshot);
        if (true) { // !screenshot 1/18
            if ((EventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || EventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
                    && EventPackage.equals("com.facebook.katana") && EventText.equals("Facebook") && !facebook && !Utils.cancel) {
                clean_prevApp(state_array, 1);
                state_array[1] = true;
                facebook = true;
                MyEventText = "Facebook is open";
                Log.d(TAG, "Facebook is open!!");
                //storeSession("Facebook", "Image");
                //NewsRelated(true);

//                CSVHelper.storeToCSV("Dialog.csv" ,"Dialog Time: " + System.currentTimeMillis() + " In accessibility service(facebook)");
//                service_intent.putExtra("Facebook", facebook);
                pref.edit().putString("Trigger", "Facebook").apply();
                service_intent.putExtra("FromNotification", false);
                service_intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//                CSVHelper.storeToCSV("AccessibilityDetect.csv", "facebook call startActivity time: " + (System.currentTimeMillis()));
//                try {
//                    Thread.sleep(200);
//                } catch (Exception e) {
//
//                }
                Log.d("ScreenCaptureActivity", "Facebook is open!!");
                startActivity(service_intent);

//                CSVHelper.storeToCSV("AccessibilityDetect.csv", "Send facebook screen shot notification");
            }
        }
        //optimization
//            else if (screenshot)
//            {
//                if(facebook) {
//                    //返回桌面
//                    if (EventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
//                            && home) {
//                        init_application();
//                        service_intent.putExtra("Facebook", facebook);
//                        stopService(new Intent(getApplicationContext(), ScreenCapture.class));
//                        MyEventText = "Facebook is close";
//                        Log.d(TAG, "Facebook is close!!");
//                        NewsRelated(false);
//                    }
//                    //案通知欄裡面的訊息，跳到別的app
//                    else if(EventType == AccessibilityEvent.TYPE_VIEW_CLICKED
//                            && (Arrays.asList(Utils.HomePackage).contains(EventPackage)
//                            || EventPackage.equals("com.android.systemui"))
//                            && !Arrays.asList(Utils.Permission_ui).contains(EventText))
//                    {
//                        init_application();
//                        service_intent.putExtra("Facebook", facebook);
//                        stopService(new Intent(getApplicationContext(), ScreenCapture.class));
//                        Log.d(TAG, "Facebook is close!!");
//                        MyEventText = "Facebook is close";
//                        NewsRelated(false);
//                    }
//                }
//            }
//            if(Utils.cancel && facebook)
//            {
//                facebook = true;
//                if (EventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
//                        && home) {
//                    Log.d(TAG, "facebook Initial");
//                    MyEventText = "Facebook is close";
//                    Utils.cancel = false;
//                    init_application();
//                    NewsRelated(false);
//                }
//                //案通知欄裡面的訊息，跳到別的app
//                else if(EventType == AccessibilityEvent.TYPE_VIEW_CLICKED
//                        && (Arrays.asList(Utils.HomePackage).contains(EventPackage)
//                        || EventPackage.equals("com.android.systemui"))
//                        && !Arrays.asList(Utils.Permission_ui).contains(EventText))
//                {
//                    Log.d(TAG, "facebook Initial");
//                    MyEventText = "Facebook is close";
//                    Utils.cancel = false;
//                    NewsRelated(false);
//                    init_application();
//                }
//            }
    }

    private void IsMessengerRunning(int EventType, String EventText, String EventPackage, AccessibilityNodeInfo nodeInfo, String now_active, boolean screenshot)
    {
//        boolean change_app = false;
        if(ScreenCaptureTransition[0] && !screenshot){
            //storeSession("Messenger", "Url");
            //NewsRelated(true);
            ScreenCaptureTransition[0] = false;
        }
//        if(!now_active.equals("com.facebook.orca") && EventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
//                && !now_active.equals("android") && !now_active.equals("com.android.systemui") && !now_active.equals("com.example.accessibility_detect")){
//            change_app = true;
//        }
        if (!messenger) {
            if ((EventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || EventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
                    && (EventText.equals("Messenger") || (EventPackage.equals("com.facebook.orca")))) {
//                try {
//                    Thread.sleep(500);
//                } catch (Exception e) {
//
//                }
                clean_prevApp(state_array, 9);
                state_array[9] = true;
                messenger = true;
                ScreenCaptureTransition[0] = true;
                MyEventText = "Messenger is open";
                messenger_duration = System.currentTimeMillis();
                Log.d(TAG, "Messenger is open!!");
//                CSVHelper.storeToCSV("AccessibilityDetect.csv", "messenger is open");
//                CSVHelper.storeToCSV("AccessibilityDetect.csv", "In messenger not running scope, messenger flag is " + messenger);
                if(screenshot){
                    //stopService(new Intent(getApplicationContext(), ScreenCapture.class));
                }
            }
        }
        else if (messenger)
        {
//                if(nodeInfo!=null) {
//                    if (nodeInfo.getContentDescription() != null) {
//                        if(nodeInfo.getContentDescription().equals("用戶")){
//                            if(!name.equals("")) {
//                                WriteToFile("Test","Messenger");
//                            }
//                            else{
//                                URLtemp.clear();
//                            }
//                        }
//                    }
//                }
//            if(false){
//                if(nodeInfo!=null) {
//                    if (nodeInfo.getContentDescription() == null) {
//                        messenger = false;
//                        lastid = pref.getLong("SessionID", 0);
//                        db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
//                        MyEventText = "Messenger is close";
////                            if(!name.equals("")) {
////                                WriteToFile("Test","Messenger");
////                            }
//                        NewsRelated(false);
//                        Log.d(TAG, "Messenger is close!!");
//                    }
//                    //take_next = false;
//                }
//            }
//                //返回桌面
//             else
            boolean change_app = false;
            if(!now_active.equals("com.facebook.orca") && EventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
                    && !now_active.equals("android") && !now_active.equals("com.android.systemui") && !now_active.equals("com.example.accessibility_detect")
                    && !EventPackage.equals("com.facebook.orca") && !EventPackage.equals("android") && EventPackage.equals(now_active)
                    && !EventPackage.equals("com.android.systemui") && !EventPackage.equals("com.example.accessibility_detect")){
                change_app = true;
            }
            boolean question_interrupt = pref.getBoolean("Question_interrupt",false);
            if (EventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
                    && (home || question_interrupt || change_app)) {
//                CSVHelper.storeToCSV("AccessibilityDetect.csv",  "Messenger is close(home condition)");
                pref.edit().putBoolean("Question_interrupt",false).apply();
                init_application();
                messenger = false;
                state_array[9] = false;
                MyEventText = "Messenger is close";
                lastid = pref.getLong("SessionID", 0);
                //db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
//                        if(!name.equals("")) {
//                            WriteToFile("Test","Messenger");
//                        }
                //NewsRelated(false);
//                    CSVHelper.storeToCSV("AccessibilityDetect.csv", "In messenger running scope, messenger flag is " + messenger + " ( " + home +", )");

                long now = System.currentTimeMillis();
                if(now - messenger_duration >= 10 *1000){
                    //ESMjump("Messenger");
                }

                Log.d(TAG, "Messenger is close!!");
            }
            //案通知欄裡面的訊息，跳到別的app
            else if(
                    false
//                        EventType == AccessibilityEvent.TYPE_VIEW_CLICKED
//                        && (Arrays.asList(HomePackage).contains(EventPackage)
//                        || EventPackage.equals("com.android.systemui"))
//                        && !Arrays.asList(Utils.Permission_ui).contains(EventText)
            )
            {
//                    CSVHelper.storeToCSV("AccessibilityDetect.csv",  "Messenger is close(noti condition)");

                init_application();
                messenger = false;
                state_array[9] = false;
                lastid = pref.getLong("SessionID", 0);
                //db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
                MyEventText = "Messenger is close";
//                        if(!name.equals("")) {
//                            WriteToFile("Test","Messenger");
//                        }
                //NewsRelated(false);
                Log.d(TAG, "Messenger is close!!");
            }
//                else if(EventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED  && !now_active.equals("com.facebook.orca")){
//                    if(Arrays.asList(AppPack).contains(now_active) || Arrays.asList(NewsPack).contains(now_active)
//                            || Arrays.asList(PTTPack).contains(now_active)) {
//                        init_application();
//                        messenger = false;
//                        MyEventText = "Messenger is close";
//                        lastid = pref.getLong("SessionID", 0);
//                        db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
//                        //                        if(!name.equals("")) {
//                        //                            WriteToFile("Test","Messenger");
//                        //                        }
//                        NewsRelated(false);
//                        Log.d(TAG, "Messenger is close!!");
//                    }
//                }
        }
        if(EventPackage.equals("com.facebook.orca") && messenger)
        {
            if(nodeInfo != null)
            {
                String content = "";
                if(nodeInfo.getContentDescription() != null){
                    content = nodeInfo.getContentDescription().toString();
                }
                Log.d(TAG, "nodeInfo.getContentDescription(): " + content);
                String url = "";
//                    if(name.equals(""))
//                    {
//                        getMessengerName(nodeInfo);//看有沒有抓到名字
//                    }
                if(content.contains("http:"))
                {
                    String[] split_str = content.split("http:");
                    url = "http:" + split_str[1];
                    SharedPreferences pref = getSharedPreferences("URL", MODE_PRIVATE);
                    Set<String> UrlSet = pref.getStringSet("UrlSet", new HashSet<String>());
                    List<String> TitleAndWeb = new ArrayList<String>(UrlSet);

                    if(!TitleAndWeb.contains(url)) {
//                            URLtemp.add(url);
                        Log.d(TAG, "MessengerURL: " + url); //網址，要傳到後端
                        MyEventText = "Get Messenger URL";
//                        if (apptimesStreamGenerator != null) {
//                            apptimesStreamGenerator.updateapptimes("MessengerURL");
//                        }
                        //WriteToFile(url, "Messenger");
                    }
                }
                else if(content.contains("https:"))
                {
                    String[] split_str = content.split("https:");
                    url = "https:" + split_str[1];
                    SharedPreferences pref = getSharedPreferences("URL", MODE_PRIVATE);
                    Set<String> UrlSet = pref.getStringSet("UrlSet", new HashSet<String>());
                    List<String> TitleAndWeb = new ArrayList<String>(UrlSet);

                    if(!TitleAndWeb.contains(url)) {
//                            URLtemp.add(url);
                        Log.d(TAG, "MessengerURL: " + url); //網址，要傳到後端
                        MyEventText = "Get Messenger URL";
//                        if (apptimesStreamGenerator != null) {
//                            apptimesStreamGenerator.updateapptimes("MessengerURL");
//                        }
                        //WriteToFile(url, "Messenger");
                    }
                }
            }
        }
            /*if(Utils.cancel && messenger)
            {
                messenger = true;
                if (event.getEventType() != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
                        && ((getEventText(event).indexOf("螢幕") >= 0 || getEventText(event).indexOf("主畫面") >= 0
                        || getEventText(event).indexOf("預設頁面") >= 0))) {
                    Log.d(TAG, "messenger Initial");
                    Utils.cancel = false;
                    init_application();
                }
                //案通知欄裡面的訊息，跳到別的app
                else if(event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED
                        && (Arrays.asList(Utils.HomePackage).contains(event.getPackageName().toString())
                        || event.getPackageName().toString().equals("com.android.systemui"))
                        && !Arrays.asList(Utils.Permission_ui).contains(getEventText(event)))
                {
                    Log.d(TAG, "messenger Initial");
                    Utils.cancel = false;
                    init_application();
                }
            }*/
    }

    private void IsYoutubeRunning(int EventType, String EventText, String EventPackage, Intent service_intent, boolean screenshot)
    {
        /*if(event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED
            && event.getPackageName().toString().equals("com.google.android.youtube")
            && now_active.equals("com.google.android.youtube")
            && getEventText(event).equals("前往頻道動作選單"))
        {
            if(nodeInfo != null && nodeInfo.getContentDescription() != null)
            {
                WriteToFile(nodeInfo.getContentDescription().toString());
                youtube_screen = true;
                //Log.d(TAG, "GG: " + nodeInfo.getContentDescription().toString()); //點了甚麼影片，要傳到後端
            }
        }*/
        Log.d(TAG, "is watch video: " + watch_video);
        Log.d(TAG, "youtube: " + youtube);
        if (EventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                && EventPackage.equals("com.google.android.youtube") && EventText.equals("YouTube")
                && !youtube && !Utils.cancel ) { // !watch_video 1/4
            clean_prevApp(state_array, 5);
            state_array[5] = true;
            youtube = true;
            Log.d(TAG, "Youtube is open!!");
            MyEventText = "Youtube is open";
            //storeSession("Youtube", "Image");
            //NewsRelated(true);

//            CSVHelper.storeToCSV("AccessibilityDetect.csv", "In youtube screen shot trigger, youtube flag is " + youtube);

//            service_intent.putExtra("Youtube", youtube);
            pref.edit().putString("Trigger", "Youtube").apply();
            service_intent.putExtra("FromNotification", false);
            service_intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//                    try {
//                        Thread.sleep(500);
//                    } catch (Exception e) {
//
//                    }
            startActivity(service_intent);

            //SendCaptureNoti("Youtube", youtube, service_intent, screenshot);

//            CSVHelper.storeToCSV("AccessibilityDetect.csv", "Send youtube screen shot notification");
        }
        else if (EventType == AccessibilityEvent.TYPE_VIEW_SCROLLED
                && EventPackage.equals("com.google.android.youtube")
                && !Utils.cancel && !youtube) { // watch_video 1/4
            youtube_count++;
            if(youtube_count >= 3) {
                watch_video = false;
                youtube_count = 0;
//                clean_prevApp(state_array, 5);
//                state_array[5] = true;
//                youtube = true;
                Log.d(TAG, "Youtube is open!!");
//                MyEventText = "Youtube is open";
//                storeSession("Youtube", "Image");
                //NewsRelated(true);

//                CSVHelper.storeToCSV("AccessibilityDetect.csv", "In youtube screen shot trigger, youtube flag is " + youtube);

//                service_intent.putExtra("Youtube", youtube);
                pref.edit().putString("Trigger", "Youtube").apply();

                service_intent.putExtra("FromNotification", false);
                service_intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//                    try {
//                        Thread.sleep(500);
//                    } catch (Exception e) {
//
//                    }
                startActivity(service_intent);

                //SendCaptureNoti("Youtube", youtube, service_intent, screenshot);

//                CSVHelper.storeToCSV("AccessibilityDetect.csv", "Send youtube screen shot notification");
            }
        }
        else if (EventPackage.equals("com.google.android.youtube")
                && EventType != AccessibilityEvent.TYPE_VIEW_SCROLLED
                && EventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            youtube_count = 0;
        }
            /*else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED
                    && event.getPackageName().toString().equals("com.google.android.youtube")
                    && !youtube && !Utils.cancel
                    && getEventText(event).contains("暫停影片")) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
                youtube = true;
                Log.d(TAG, "Youtube is open!!");
                service_intent.putExtra("Youtube", youtube);
                service_intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(service_intent);
            }*/
        //            else if (screenshot)
//            {
//                if(youtube) {
//                    //返回桌面
//                    if (EventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
//                            && home) {
//                        init_application();
//                        service_intent.putExtra("Youtube", youtube);
//                        stopService(new Intent(getApplicationContext(), ScreenCapture.class));
//                        Log.d(TAG, "Youtube is close!!");
//                        MyEventText = "Youtube is close";
//                        NewsRelated(false);
//                    }
//                    else if(EventType == AccessibilityEvent.TYPE_VIEW_CLICKED //案通知欄裡面的訊息，跳到別的app
//                            && (Arrays.asList(Utils.HomePackage).contains(EventPackage)
//                            || EventPackage.equals("com.android.systemui"))
//                            && !Arrays.asList(Utils.Permission_ui).contains(EventText))
//                    {
//                        init_application();
//                        service_intent.putExtra("Youtube", youtube);
//                        stopService(new Intent(getApplicationContext(), ScreenCapture.class));
//                        Log.d(TAG, "Youtube is close!!");
//                        MyEventText = "Youtube is close";
//                        NewsRelated(false);
//                    }
//                    else if(EventType == AccessibilityEvent.TYPE_VIEW_CLICKED
//                            && EventPackage.equals("com.google.android.youtube")
//                            && (EventText.equals("前往頻道動作選單") || EventText.equals("動作選單")))
//                    {
//                        try {
//                            Log.d(TAG, "delay one second");
//                            Thread.sleep(2000);
//                        } catch (Exception e) {
//
//                        }
//                        init_application();
//                        service_intent.putExtra("Youtube", youtube);
//                        stopService(new Intent(getApplicationContext(), ScreenCapture.class));
//                        Log.d(TAG, "Youtube is close!!");
//                        MyEventText = "Youtube is close";
//                        NewsRelated(false);
//                        /*if(nodeInfo != null && nodeInfo.getContentDescription() != null)
//                        {
//                            WriteToFile(nodeInfo.getContentDescription().toString());
//                            Log.d(TAG, "Title: " + nodeInfo.getContentDescription().toString()); //點了甚麼影片，要傳到後端
//                        }*/
//                    }
//                }
//            }
//            if(Utils.cancel && youtube)
//            {
//                youtube = true;
//                if (EventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
//                        && home) {
//                    Log.d(TAG, "youtube Initial");
//                    Utils.cancel = false;
//                    init_application();
//                    MyEventText = "Youtube is close";
//                    NewsRelated(false);
//                }
//                //案通知欄裡面的訊息，跳到別的app
//                else if(EventType == AccessibilityEvent.TYPE_VIEW_CLICKED
//                        && (Arrays.asList(Utils.HomePackage).contains(EventPackage)
//                        || EventPackage.equals("com.android.systemui"))
//                        && !Arrays.asList(Utils.Permission_ui).contains(EventText))
//                {
//                    Log.d(TAG, "youtube Initial");
//                    Utils.cancel = false;
//                    NewsRelated(false);
//                    MyEventText = "Youtube is close";
//                    init_application();
//                }
//            }
    }

    /*private void IsPTTAppRunning(int EventType, String EventText, String EventPackage, String now_active, boolean screenshot, AccessibilityEvent event)
    {//2 true
        boolean change_app = false;
//        if(true){
//            AccessibilityNodeInfo nodeInfo = event.getSource();
//            dfs(nodeInfo);
//        }
        if(ScreenCaptureTransition[1] && !screenshot){
            storeSession("PTT", "Title");
            NewsRelated(true);
            ScreenCaptureTransition[1] = false;
        }
        if(!ptt){
            if (EventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
                    && Arrays.asList(PttPack).contains(EventPackage)) {
//                CSVHelper.storeToCSV("AccessibilityDetect.csv",  "PTT is open");
//                try {
//                    Thread.sleep(500);
//                } catch (Exception e) {
//
//                }
                clean_prevApp(state_array, 8);
                state_array[8] = true;
                ptt = true;
                MyEventText = "PPT is open";
                ScreenCaptureTransition[1] = true;
                Log.d(TAG, "PPT is open!!");
//                CSVHelper.storeToCSV("AccessibilityDetect.csv", "In ptt not running scope, ptt flag is " + ptt);

//                storeSession("PTT", "Title");
//                NewsRelated(true);
            }
        }
        else if(ptt) {
            if(EventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED && !home
                    && EventPackage.equals("com.facebook.orca")){
//                CSVHelper.storeToCSV("AccessibilityDetect.csv",  "PTT interrupted by messenger");
                home = true;
            }
            if(!Arrays.asList(PttPack).contains(now_active) && EventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
                    && !now_active.equals("android") && !now_active.equals("com.android.systemui") && !now_active.equals("com.example.accessibility_detect")
                    && !Arrays.asList(PttPack).contains(EventPackage) && !EventPackage.equals("android") && EventPackage.equals(now_active)
                    && !EventPackage.equals("com.android.systemui") && !EventPackage.equals("com.example.accessibility_detect")){
                change_app = true;
            }
            boolean question_interrupt = pref.getBoolean("Question_interrupt",false);

            if (EventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
                    && (home || change_app || question_interrupt)) {
//                CSVHelper.storeToCSV("AccessibilityDetect.csv",  "PTT is close(home condition)");
                pref.edit().putBoolean("Question_interrupt",false).apply();
                state_array[8] = false;
                init_application();
                lastid = pref.getLong("SessionID", 0);
                db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());

                Log.d(TAG, "Ptt is close!!");
                MyEventText = "Ptt is close";
//                    WriteToFile("Test", "PTT");
                NewsRelated(false);
                ESMjump("PTT");
//                CSVHelper.storeToCSV("AccessibilityDetect.csv", "In ptt running scope, ptt flag is " + ptt + " ( " + home +"," + change_app + " )");
            }
            else{ //如果不是離開ptt的話
                if(EventPackage.equals("com.ihad.ptt")) {
                    AccessibilityNodeInfo nodeInfo = event.getSource();
                    dfs(nodeInfo);
                }
            }
//            else if(
//                    false
////                    EventType == AccessibilityEvent.TYPE_VIEW_CLICKED
////                    && (Arrays.asList(HomePackage).contains(EventPackage)
////                    || EventPackage.equals("com.android.systemui"))
////                    && !Arrays.asList(Utils.Permission_ui).contains(EventText)
//                )
//            {
//                CSVHelper.storeToCSV("AccessibilityDetect.csv",  "PTT is close(noti condition)");
//
//                init_application();
//                state_array[8] = false;
//                lastid = pref.getLong("SessionID", 0);
//                db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
////                    WriteToFile("Test", "PTT");
//                Log.d(TAG, "Ptt is close!!");
//                MyEventText = "Ptt is close";
//                NewsRelated(false);
//            }
        }
        if (EventType == AccessibilityEvent.TYPE_VIEW_CLICKED && Arrays.asList(PttPack).contains(now_active)
                && Arrays.asList(PttPack).contains(EventPackage) && ptt) {
            if(EventText.contains("新聞") && !EventPackage.equals("com.ihad.ptt"))
            {
                Log.d(TAG, "Origin news: " + EventText);
//                    URLtemp.add(EventText);
//                if(apptimesStreamGenerator!=null) {
//                    apptimesStreamGenerator.updateapptimes("PTTtitle");
//                }
                WriteToFile(EventText, "PTT");
                Log.d(TAG, "News In PTT: " + EventText);
            }
        }
    }*/

    /*private void IsLineRunning(int EventType, String EventText, String EventPackage, Intent service_intent, boolean screenshot, AccessibilityNodeInfo nodeInfo )
    {
        String today = getSharedPreferences("test", MODE_PRIVATE)
                .getString("Line", "");
        Log.d(TAG, "Last line event (in running): " + today);
        if (nodeInfo != null && nodeInfo.getContentDescription() != null){
            if (EventType == AccessibilityEvent.TYPE_VIEW_CLICKED &&
                    EventPackage.equals("jp.naver.line.android") && nodeInfo.getContentDescription().equals("新聞選單 勾選")
                    && !line && !Utils.cancel){
                CSVHelper.storeToCSV("AccessibilityDetect.csv", "linetoday is open");

                clean_prevApp(state_array, 7);
                state_array[7] = true;
                Utils.first = false;
                line = true;
                Log.d(TAG, "LineToday is open (new version)!!");
                MyEventText = "LineToday is open";

                getSharedPreferences("test", MODE_PRIVATE).edit().putString("Line", "TODAY").apply();
                storeSession("LineToday","Image");
                NewsRelated(true);

//                CSVHelper.storeToCSV("AccessibilityDetect.csv", "In LineToday screen shot trigger, LineToday flag is " + line);

//                service_intent.putExtra("LineToday", line);
                pref.edit().putString("Trigger", "LineToday").apply();
                service_intent.putExtra("FromNotification", false);

                service_intent.setFlags(FLAG_ACTIVITY_NEW_TASK);

                startActivity(service_intent);

                SendCaptureNoti("LineToday", line, service_intent, screenshot);
            }
        }
        if ((EventType == AccessibilityEvent.TYPE_VIEW_SELECTED &&
                EventPackage.equals("jp.naver.line.android") && EventText.equals("TODAY")
                && !line && !Utils.cancel) ) {
//                CSVHelper.storeToCSV("AccessibilityDetect.csv", "LineToday call startActivity time: " + (System.currentTimeMillis()));
            CSVHelper.storeToCSV("AccessibilityDetect.csv", "linetoday is open");

            clean_prevApp(state_array, 7);
            state_array[7] = true;
            Utils.first = false;
            line = true;
            Log.d(TAG, "LineToday is open!!");
            MyEventText = "LineToday is open";

            getSharedPreferences("test", MODE_PRIVATE).edit().putString("Line", "TODAY").apply();
            storeSession("LineToday","Image");
            NewsRelated(true);

//                CSVHelper.storeToCSV("AccessibilityDetect.csv", "In LineToday screen shot trigger, LineToday flag is " + line);

//                service_intent.putExtra("LineToday", line);
            pref.edit().putString("Trigger", "LineToday").apply();
            service_intent.putExtra("FromNotification", false);

            service_intent.setFlags(FLAG_ACTIVITY_NEW_TASK);

            startActivity(service_intent);

            SendCaptureNoti("LineToday", line, service_intent, screenshot);
//                CSVHelper.storeToCSV("AccessibilityDetect.csv", "Send LineToday screen shot notification");
        }
        else if ((EventType != AccessibilityEvent.TYPE_VIEW_SELECTED
                && EventType != AccessibilityEvent.TYPE_VIEW_CLICKED
                && EventType != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
                && today.equals("TODAY") && (EventPackage.equals("jp.naver.line.android"))
                && !line && Utils.first)) {//出去再進來


                clean_prevApp(state_array, 7);
                state_array[7] = true;
                Utils.first = false;
                line = true;
                Log.d(TAG, "LineToday is open!! (back)");
                CSVHelper.storeToCSV("AccessibilityDetect.csv", "Line Today is open (back)");
                MyEventText = "LineToday is open";
    //                if (apptimesStreamGenerator != null) {
    //                    apptimesStreamGenerator.updateapptimes("LinetodayOpen");
    //                }
                getSharedPreferences("test", MODE_PRIVATE).edit().putString("Line", "TODAY").apply();
                storeSession("LineToday","Image");
                NewsRelated(true);

    //                CSVHelper.storeToCSV("AccessibilityDetect.csv", "In LineToday screen shot trigger, LineToday flag is " + line);

    //                service_intent.putExtra("LineToday", line);
                pref.edit().putString("Trigger", "LineToday").apply();

                service_intent.putExtra("FromNotification", false);
                service_intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                try {
                    Thread.sleep(1500);
                } catch (Exception e) {

                }
                startActivity(service_intent);

                SendCaptureNoti("LineToday", line, service_intent, screenshot);
    //                CSVHelper.storeToCSV("AccessibilityDetect.csv", "Send LineToday screen shot notification");
        }
        //            else if (screenshot) {
//                if (line) {
//                    if (home || ((EventPackage.equals("jp.naver.line.android")
//                            && (EventType == AccessibilityEvent.TYPE_VIEW_SELECTED && (EventText.equals("主頁") || EventText.equals("聊天") ||
//                            EventText.equals("貼文串") || EventText.equals("錢包")))))) {
//                        Utils.first = true;
//                        init_application();
//                        intent.putExtra("LineToday", line);
//                        stopService(new Intent(getApplicationContext(), ScreenCapture.class));
//                        Log.d(TAG, "LineToday is close!!");
//                        MyEventText = "LineToday is close";
//                        NewsRelated(false);
//                    } else if (EventType == AccessibilityEvent.TYPE_VIEW_CLICKED
//                            && (Arrays.asList(Utils.HomePackage).contains(EventPackage)
//                            || EventPackage.equals("com.android.systemui"))
//                            && !Arrays.asList(Utils.Permission_ui).contains(EventText)) {
//                        Utils.first = true;
//                        init_application();
//                        intent.putExtra("LineToday", line);
//                        stopService(new Intent(getApplicationContext(), ScreenCapture.class));
//                        Log.d(TAG, "LineToday is close!!");
//                        MyEventText = "LineToday is close";
//                        NewsRelated(false);
//                    }
//                }
//            }
//            if (Utils.cancel && line) {
//                line = true;
//                if (home || ((EventPackage.equals("jp.naver.line.android")
//                        && (EventType == AccessibilityEvent.TYPE_VIEW_SELECTED && (EventText.equals("主頁") || EventText.equals("聊天") ||
//                        EventText.equals("貼文串") || EventText.equals("錢包")))))) {
//                    Log.d(TAG, "line Initial");
//                    Utils.first = true;
//                    Utils.cancel = false;
//                    NewsRelated(false);
//                    MyEventText = "LineToday is close";
//                    init_application();
//                } else if (EventType == AccessibilityEvent.TYPE_VIEW_CLICKED
//                        && (Arrays.asList(Utils.HomePackage).contains(EventPackage)
//                        || EventPackage.equals("com.android.systemui"))
//                        && !Arrays.asList(Utils.Permission_ui).contains(EventText)) {
//                    Utils.first = true;
//                    Utils.cancel = false;
//                    NewsRelated(false);
//                    MyEventText = "LineToday is close";
//                    init_application();
//                }
//            }
    }*/

    private void clean_prevApp(boolean[] now_state, int index){
        for(int i = 0; i < now_state.length; i++){
//            CSVHelper.storeToCSV("AccessibilityDetect.csv", i + " " + now_state[i]);
            if(now_state[i]) {
                switch (i) {
                    case 1: //Facebook
//                        init_application();
                        facebook = false;
                        if(Utils.cancel){
                            Utils.cancel = false;
//                            CSVHelper.storeToCSV("AccessibilityDetect.csv", "In facebook cleaned, set cancel flag to " + Utils.cancel);
                        }
                        else{
                            intent.putExtra("Facebook", false);
                            pref.edit().putString("Trigger", "").apply();

                            //stopService(new Intent(this, ScreenCapture.class));
                        }
//                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "In facebook cleaned, set facebook flag to " + facebook);

                        MyEventText = "Facebook" + " is close";
//                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "Facebook is cleaned");
                        //notificationListenService.updateRecordingNotification(this, Accessibility_ID, "Facebook", true, intent);
//                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "Send Facebook screen shot update notification");

//                        lastid = pref.getLong("SessionID", 0);
//                        db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
                        Log.d(TAG, "Facebook" + " is close!!");
                        endSession();
                        //ESMjump("Facebook");
//                        NewsRelated(false);
                        break;
                    case 5: // youtube
//                        init_application();
                        youtube = false;
                        watch_video = false;
                        if(Utils.cancel){
                            Utils.cancel = false;
//                            CSVHelper.storeToCSV("AccessibilityDetect.csv", "In youtube cleaned, set cancel flag to " + Utils.cancel);
                        }
                        else{
                            intent.putExtra("Youtube", false);
                            pref.edit().putString("Trigger", "").apply();

                            //stopService(new Intent(this, ScreenCapture.class));
                        }
//                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "In youtube cleaned, set youtube flag to " + youtube);

                        MyEventText = "Youtube" + " is close";
//                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "Youtube is cleaned");
                        //notificationListenService.updateRecordingNotification(this, Accessibility_ID, "Youtube", true, intent);
//                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "Send youtube screen shot update notification");
                        endSession();
                        //ESMjump("Youtube");
//                        lastid = pref.getLong("SessionID", 0);
//                        db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
                        Log.d(TAG, "Youtube" + " is close!!");
//                        NewsRelated(false);
                    case 7: //Line Today
                        Utils.first = true;
                        line  = false;
//                        init_application();
                        if(Utils.cancel){
                            Utils.cancel = false;
//                            CSVHelper.storeToCSV("AccessibilityDetect.csv", "In line today cleaned, set cancel flag to " + Utils.cancel);
                        }
                        else{
                            intent.putExtra("LineToday", false);
                            pref.edit().putString("Trigger", "").apply();

                            //stopService(new Intent(getApplicationContext(), ScreenCapture.class));
                        }
//                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "In line today cleaned, set line today flag to " + line);
//                        CSVHelper.storeToCSV("MyDataRecord.csv", "LineToday is close!!");

                        MyEventText = "LineToday" + " is close";
//                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "LineToday is cleaned");
                        //notificationListenService.updateRecordingNotification(this, Accessibility_ID, "LineToday", true, intent);
//                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "Send line today screen shot update notification");
                        endSession();
                        //ESMjump("LineToday");
                        //                        lastid = pref.getLong("SessionID", 0);
//                        db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
                        Log.d(TAG, "LineToday" + " is close!!");
//                        NewsRelated(false);
                    case 8: // PTT
                        ptt = false;
//                        init_application();
                        lastid = pref.getLong("SessionID", 0);
                        //db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
//                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "PTT is cleaned");
                        Log.d(TAG, "Ptt is close!!");
                        MyEventText = "Ptt is close";
                        //NewsRelated(false);
                        //ESMjump("PTT");
//                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "In ptt cleaned, set ptt flag to false");
                }
                now_state[i] = false;
            }
//            CSVHelper.storeToCSV("AccessibilityDetect.csv", i + " " + now_state[i]);
        }
    }

    public boolean Dialog_pop(long last, long now){
        if(now - last < agree_interval*60*60*1000){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        //CSVHelper.storeToCSV("TestService.csv", "Accessibility service connected");
//        Fabric.with(this, new Crashlytics());
        Toast.makeText(getApplicationContext(),"已連接",Toast.LENGTH_SHORT).show();

        pref = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences pref = getSharedPreferences("test", MODE_PRIVATE);
        pref.edit()
                .putBoolean("LineToday", false)
                .apply();
        /*Resources res = getResources();
        NewsPack = res.getStringArray(labelingStudy.nctu.minuku.R.array.NewsPack);
        NewsName = res.getStringArray(labelingStudy.nctu.minuku.R.array.NewsName);
        HomePackage = res.getStringArray(labelingStudy.nctu.minuku.R.array.HomePackage);
        AppPack = res.getStringArray(labelingStudy.nctu.minuku.R.array.AppPackage);
        WebPack = res.getStringArray(labelingStudy.nctu.minuku.R.array.WebPackage);
        WebEvent = res.getStringArray(labelingStudy.nctu.minuku.R.array.WebText);
        PttPack = res.getStringArray(labelingStudy.nctu.minuku.R.array.PttPackage);*/

        List<String> UrlList = new ArrayList<>();
        Set<String> UrlSet = new HashSet<String>(UrlList);
        pref = getSharedPreferences("URL", MODE_PRIVATE);
        pref.edit()
                .putStringSet("UrlSet", UrlSet)
                .apply();

        mPowerManager = (PowerManager)getSystemService(POWER_SERVICE);
        //db = appDatabase.getDatabase(getApplicationContext());
        //userRecord = db.userDataRecordDao().getLastRecord();
        Log.d(TAG, "onServiceConnected");

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();

//        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED |
//                            AccessibilityEvent.TYPE_WINDOWS_CHANGED |
//                            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED |
//                            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED |
//                            AccessibilityEvent.TYPE_VIEW_CLICKED |
//                            AccessibilityEvent.TYPE_VIEW_SCROLLED |
//                            AccessibilityEvent.TYPE_VIEW_SELECTED |
//                            AccessibilityEvent.TYPE_VIEW_FOCUSED|
//                            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED |
//                            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED |
//                            AccessibilityEvent.TYPE_ANNOUNCEMENT |
//                            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        /*mScheduledExecutorService = Executors.newScheduledThreadPool(Constants.MAIN_THREAD_SIZE);
        mScheduledFuture = mScheduledExecutorService.scheduleAtFixedRate(
                uploadAppUsageRunnable,
                Constants.STREAM_UPDATE_DELAY,
                Constants.STREAM_UPDATE_FREQUENCY,
                TimeUnit.SECONDS);*/
        setServiceInfo(info);
    }

    public void runPhoneStatusMainThread(boolean start){

        //labelingStudy.nctu.minuku.logger.Log.d(TAG, "runSensorMainThread") ;

        if(start) {
            Log.d(TAG, "Start Thread");
            mMainThread.post(runnable);
        }
        else{
            Log.d(TAG, "Stop Thread");
            mMainThread.removeCallbacks(runnable);
        }
    }

    public MyAccessibilityService() {
        super();
        //apptimesStreamGenerator.setLatestInAppAction(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        //apptimesStreamGenerator.updateStream();
        Log.d(TAG, "Initial MyAccessibility Service");
    }

    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }

    private String printForegroundTask() {
        String currentApp = "NULL";
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
        Log.e(TAG, "Current App in foreground is: " + currentApp);
        return currentApp;
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //CSVHelper.storeToCSV("TestService.csv", "Accessibility service onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        /*if (mAccessTokenLoader != null) {
            mAccessTokenLoader.unregisterListener(mLoaderListener);
            mAccessTokenLoader.cancelLoad();
            mAccessTokenLoader.stopLoading();
        }*/
        return super.onUnbind(intent);
    }

    public static void init_application()
    {
        facebook = false;
        youtube = false;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void DetectLastForeground(int EventType, String EventText, String EventPackage, String extra) {
        //Fixed: Window State Changed from the same application (showing keyboard within an app) should be ignored
        //boolean same_app = false;

        //String ForegroundNow = printForegroundTask();
        String Deviceid = getSharedPreferences("test", MODE_PRIVATE).getString("UserID", "");
//        String text = "";
//        String type = "";
//        String packageName = "";
        String Eventtype = EventTypeIwant(EventType);
//        if(Eventtype.equals("TYPE_VIEW_SCROLLED") && event_scroll){
//            Eventtype = "NA";
//        }
//        if(Eventtype.equals("TYPE_VIEW_SCROLLED") && !event_scroll){
//            event_scroll = true;
//        }
        if (!Eventtype.equals("NA") || !MyEventText.equals("NA")) {
//            if(!Eventtype.equals("TYPE_VIEW_SCROLLED"))
//            {
//                event_scroll = false;
//            }
//            type = Eventtype;
//            text = getEventText(event);
//            packageName = event.getPackageName().toString();
//            if (event.getContentDescription() != null) {
//                extra = event.getContentDescription().toString();
//                // Log.d(TAG,"extra : "+ extra);
//            }

            /*PackageInfo pkgInfo;
            try {
                pkgInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException | NullPointerException | Resources.NotFoundException e) {
                pkgInfo = null;
            }*/

            if(isOurTarget(EventPackage))
            {
//                JSONObject jobject = new JSONObject();
                try {
//                    JSONObject appobject = new JSONObject();
//                    appobject.put(TIMESTAMP, System.currentTimeMillis());
//                    appobject.put(READABLE, getReadableTimeLong(System.currentTimeMillis()));
//                    appobject.put(DEVICEID, Deviceid);
//                    appobject.put(PACKAGENAME, packageName);
//                    appobject.put("MyEventText", MyEventText);
//                    appobject.put(EVENTTEXT, text);
//                    appobject.put(EVENTTYPE, type);
//                    appobject.put(EXTRA, extra);
//                    jobject.put("myAccessibility", appobject.toString());
                    /*if (accessibilityStreamGenerator != null) {
                        Log.d(TAG, "accessibilityStreamGenerator not null");
                        int index = Arrays.asList(NewsPack).indexOf(EventPackage);
                        String NewsApp_Name;
                        if(index >= 0){
                            NewsApp_Name = NewsName[index];
                        }
                        else{
                            NewsApp_Name = "";
                        }
                        Log.d(TAG, "News name: " + NewsApp_Name);
                        accessibilityStreamGenerator.setLatestInAppAction(System.currentTimeMillis(), Deviceid, EventPackage, EventText, Eventtype, MyEventText, extra, NewsApp_Name);
                        accessibilityStreamGenerator.updateStream();
                    }*/
//                    CSVHelper.storeAccessibilityCSV("CheckStoreAccess.csv", packageName, text, type, extra);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isOurTarget(String pkgName){
        if(Arrays.asList(AppPack).contains(pkgName) || Arrays.asList(WebPack).contains(pkgName)
                || Arrays.asList(NewsPack).contains(pkgName) || Arrays.asList(PttPack).contains(pkgName)){
            return true;
        }
        return false;
    }

    public static boolean isSystemPackage(PackageInfo pkgInfo) {
        return pkgInfo != null && ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1);
    }

    public static String EventTypeIwant(int EventType) {
        String type = "NA";
        switch (EventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                type = "TYPE_VIEW_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                type = "TYPE_VIEW_LONG_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                type = "TYPE_VIEW_FOCUSED";
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                type = "TYPE_VIEW_SELECTED";
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                type = "TYPE_VIEW_TEXT_CHANGED";
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                type = "TYPE_NOTIFICATION_STATE_CHANGED";
                break;
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                type = "TYPE_ANNOUNCEMENT";
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                type = "TYPE_VIEW_SCROLLED";
                break;
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                type = "TYPE_WINDOWS_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                type = "TYPE_WINDOW_STATE_CHANGED";
                break;
//            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
//                type = "YPE_WINDOW_CONTENT_CHANGED";
//                break;
        }
        return type;
    }

    public boolean getScreenStatus() {
        boolean screenOn = false;
        //use isInteractive after api 20
        if (mPowerManager.isInteractive()) screenOn = true;
        else screenOn = false;

        return screenOn;
    }

    public void endSession(){
        lastid = pref.getLong("SessionID", 0);
        //db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
        //NewsRelated(false);
    }
}
