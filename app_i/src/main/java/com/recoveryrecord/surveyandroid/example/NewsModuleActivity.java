package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
//import android.icu.lang.UCharacter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.DbHelper.PushNewsDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.ReadingBehaviorDbHelper;
import com.recoveryrecord.surveyandroid.example.sqlite.DragObj;
import com.recoveryrecord.surveyandroid.example.sqlite.FlingObj;
import com.recoveryrecord.surveyandroid.example.sqlite.PushNews;
import com.recoveryrecord.surveyandroid.example.sqlite.ReadingBehavior;
import com.recoveryrecord.surveyandroid.example.receiever.ApplicationSelectorReceiver;
//import com.recoveryrecord.surveyandroid.example.receiever.ScreenStateReceiver;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
//import java.util.Random;
//import java.util.Set;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
//import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.MEDIA_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_CONTENT;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_ID_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_IMAGE;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_MEDIA_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_PUBDATE;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_URL;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_CLICK;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_OPEN_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_BYTE_PER_LINE;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_CONTENT_LENGTH;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_DISPLAY_HEIGHT;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_DISPLAY_WIDTH;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_DOC_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_DRAG_NUM;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_DRAG_RECORD;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_FLING_NUM;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_FLING_RECORD;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_FONT_SIZE;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_HAS_IMAGE;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_IMAGE_URL;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_IN_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_IN_TIME_LONG;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_MEDIA;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_NEWS_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_OUT_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_PAUSE_COUNT;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_PUBDATE;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_ROW_SPACING;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_SAMPLE_CHECK_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_SHARE;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_TIME_ON_PAGE;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_TIME_SERIES;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_TRIGGER_BY;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_TRIGGER_BY_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_USER_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_VIEWPORT_NUM;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_VIEWPORT_RECORD;
import static com.recoveryrecord.surveyandroid.example.Constants.READ_TOTAL;
//import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_DEVICE_ID;
//import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_IS_LOGIN;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_TEST_SIZE;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_USER_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.TRIGGER_BY_KEY;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NewsModuleActivity extends AppCompatActivity implements GestureListener.SimpleGestureListener {
    //    String TagCycle = "my activity cycle";
    String device_id = "";
    String text_size_string = "1";

    volatile boolean activityStopped = false;
    volatile boolean activityEnd = false;
    boolean share_clicked = false;
    boolean document_create = false;
    long in_time = System.currentTimeMillis();
    boolean first_in = true;
//    int char_num_total = 0;
    Timestamp enter_timestamp, mPubdate;

    String tmp_time_series = "";//time series
    String tmp_record = "";//viewport
    String news_id = "";
    String media_eng = "";
    String media_ch = "";
//    String my_news_id = "";

    private String mUrl = "NA", mImg = "NA", mTitle = "NA", mDate = "NA", mSource = "NA";

    private GestureListener detector;
    List<DragObj> dragObjArrayListArray = new ArrayList<>();//drag gesture

    ReadingBehavior myReadingBehavior = new ReadingBehavior();//sqlite

    boolean self_trigger = false;
    int has_img = 0;

    private static final HashSet<Character> ch_except = new HashSet<>();
    private static final HashSet<Character> full_num = new HashSet<>();
    private static final HashSet<Character> latin = new HashSet<>();
    private static final HashSet<Character> full_en = new HashSet<>();
    /// 32    空格
    /// 33-47    標點
    /// 48-57    0~9
    /// 58-64    標點
    /// 65-90    A~Z
    /// 91-96    標點
    /// 97-122    a~z
    /// 123-126  標點
    //全形字元 - 65248 = 半形字元
    //Initialize vowels hashSet to contain vowel characters
    static{
        ch_except.add('，');
        ch_except.add('、');
        ch_except.add('。');
        ch_except.add('．');
        ch_except.add('‧');
        ch_except.add('；');
        ch_except.add('：');
        ch_except.add('▶');
        ch_except.add('→');
//        vowels.add('：');
        ch_except.add('？');
        ch_except.add('！');
        ch_except.add('（');
        ch_except.add('）');
        ch_except.add('｛');
        ch_except.add('｝');
        ch_except.add('〔');
        ch_except.add('〕');
        ch_except.add('【');
        ch_except.add('】');
        ch_except.add('《');
        ch_except.add('》');
        ch_except.add('〈');
        ch_except.add('〉');
        ch_except.add('「');
        ch_except.add('」');
        ch_except.add('『');
        ch_except.add('』');
        ch_except.add('＃');
        ch_except.add('＆');
        ch_except.add('※');
        ch_except.add('／');
        ch_except.add('～');
        ch_except.add('＋');
        ch_except.add('★');
        ch_except.add('﹔');
    }

    static {
        full_num.add('０');
        full_num.add('１');
        full_num.add('２');
        full_num.add('３');
        full_num.add('４');
        full_num.add('５');
        full_num.add('６');
        full_num.add('７');
        full_num.add('８');
        full_num.add('９');
    }

    static {
        latin.add('À');
        latin.add('Á');
        latin.add('Â');
        latin.add('Ã');
        latin.add('Ä');
        latin.add('Å');
        latin.add('Æ');
        latin.add('Ç');
        latin.add('È');
        latin.add('É');
        latin.add('Ê');
        latin.add('Ë');
        latin.add('Ì');
        latin.add('Í');
        latin.add('Î');
        latin.add('Ï');
        latin.add('Ð');
        latin.add('Ñ');
        latin.add('Ò');
        latin.add('Ó');
        latin.add('Ô');
        latin.add('Õ');
        latin.add('Ö');
        latin.add('×');
        latin.add('Ø');
        latin.add('Ù');
        latin.add('Ú');
        latin.add('Û');
        latin.add('Ü');
        latin.add('Ý');
        latin.add('Þ');
        latin.add('ß');
        latin.add('à');
        latin.add('á');
        latin.add('â');
        latin.add('ã');
        latin.add('ä');
        latin.add('å');
        latin.add('æ');
        latin.add('ç');
        latin.add('è');
        latin.add('é');
        latin.add('ê');
        latin.add('ë');
        latin.add('ì');
        latin.add('í');
        latin.add('î');
        latin.add('ï');
        latin.add('ð');
        latin.add('ñ');
        latin.add('ò');
        latin.add('ó');
        latin.add('ô');
        latin.add('õ');
        latin.add('ö');
        latin.add('ø');
        latin.add('ù');
        latin.add('ú');
        latin.add('û');
        latin.add('ü');
        latin.add('ý');
        latin.add('þ');
        latin.add('ÿ');
        latin.add('µ');
        latin.add('Ž');
        latin.add('Š');
        latin.add('Ÿ');
        latin.add('ž');
    }

    static {
        full_en.add('ｑ');
        full_en.add('ｗ');
        full_en.add('ｅ');
        full_en.add('ｒ');
        full_en.add('ｔ');
        full_en.add('ｙ');
        full_en.add('ｕ');
        full_en.add('ｉ');
        full_en.add('ｏ');
        full_en.add('ｐ');
        full_en.add('ａ');
        full_en.add('ｓ');
        full_en.add('ｄ');
        full_en.add('ｆ');
        full_en.add('ｇ');
        full_en.add('ｈ');
        full_en.add('ｊ');
        full_en.add('ｋ');
        full_en.add('ｌ');
        full_en.add('ｚ');
        full_en.add('ｘ');
        full_en.add('ｃ');
        full_en.add('ｖ');
        full_en.add('ｂ');
        full_en.add('ｎ');
        full_en.add('ｍ');
        full_en.add('Ｑ');
        full_en.add('Ｗ');
        full_en.add('Ｅ');
        full_en.add('Ｒ');
        full_en.add('Ｔ');
        full_en.add('Ｙ');
        full_en.add('Ｕ');
        full_en.add('Ｉ');
        full_en.add('Ｏ');
        full_en.add('Ｐ');
        full_en.add('Ａ');
        full_en.add('Ｓ');
        full_en.add('Ｄ');
        full_en.add('Ｆ');
        full_en.add('Ｇ');
        full_en.add('Ｈ');
        full_en.add('Ｊ');
        full_en.add('Ｋ');
        full_en.add('Ｌ');
        full_en.add('Ｚ');
        full_en.add('Ｘ');
        full_en.add('Ｃ');
        full_en.add('Ｖ');
        full_en.add('Ｂ');
        full_en.add('Ｎ');
        full_en.add('Ｍ');
    }

    public static boolean is_except(Character c){
        return ch_except.contains(c);
    }

    public static boolean is_full_num(Character c){
        return full_num.contains(c);
    }

    public static boolean is_latin(Character c){
        return latin.contains(c);
    }

    public static boolean is_full_en(Character c){
        return full_en.contains(c);
    }

    @SuppressLint({"ClickableViewAccessibility", "HardwareIds"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("log: activity cycle", "NewsModuleActivity On create");
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        text_size_string = sharedPrefs.getString(SHARE_PREFERENCE_TEST_SIZE, "1");
        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_news_module);
//        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
//        setSupportActionBar(toolbar);

        //set time in ##############################################################################
//        Date date = new Date(System.currentTimeMillis());
//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        String time_now = formatter.format(date);
        myReadingBehavior.setKEY_IN_TIMESTAMP(Timestamp.now().getSeconds());
        //check trigger from #######################################################################
//        Log.d("555 news LoginActivity", String.valueOf(sharedPrefs.getInt(SHARE_PREFERENCE_IS_LOGIN, 0)));
//        Log.d("555 news LoginActivity", String.valueOf(sharedPrefs.getString(SHARE_PREFERENCE_DEVICE_ID, "123")));
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();

            if (b.getString(NEWS_ID_KEY)!= null){
                news_id = b.getString(NEWS_ID_KEY);
            }
            if (b.getString(NEWS_MEDIA_KEY)!= null){
                media_eng = b.getString(NEWS_MEDIA_KEY);
                media_ch = media_eng;
            }
            if(b.getString(TRIGGER_BY_KEY)!=null){
                myReadingBehavior.setKEY_TRIGGER_BY(b.getString(TRIGGER_BY_KEY));
            }

            if(myReadingBehavior.getKEY_TRIGGER_BY().equals(READING_BEHAVIOR_TRIGGER_BY_NOTIFICATION)){
                PushNews myPushNews = new PushNews();
                myPushNews.setKEY_DOC_ID(device_id + " " + news_id);
                myPushNews.setKEY_OPEN_TIMESTAMP(Timestamp.now().getSeconds());
                PushNewsDbHelper dbHandler = new PushNewsDbHelper(getApplicationContext());
                dbHandler.UpdatePushNewsDetailsClick(myPushNews);

                db.collection(PUSH_NEWS_COLLECTION)
                        .document(device_id + " " + news_id)
                        .update(PUSH_NEWS_CLICK, 0,
                                PUSH_NEWS_OPEN_TIME, Timestamp.now())
//                                PUSH_NEWS_READING_BEHAVIOR_ID, myReadingBehavior.getKEY_IN_TIMESTAMP())
                        .addOnSuccessListener(aVoid -> Log.d("lognewsselect", "mark as click successfully updated!"))
                        .addOnFailureListener(e -> Log.w("lognewsselect", "mark as click Error updating document", e));
            } else {
                self_trigger = true;
            }
        }

        switch (media_eng) {
            case "中央社":
                media_eng = "cna";
                break;
            case "中時":
                media_eng = "chinatimes";
                break;
            case "華視":
                media_eng = "cts";
                break;
            case "東森":
                media_eng = "ebc";
                break;
            case "自由時報":
                media_eng = "ltn";
                break;
            case "風傳媒":
                media_eng = "storm";
                break;
            case "聯合":
                media_eng = "udn";
                break;
            case "ettoday":
                media_eng = "ettoday";
                break;
            case "三立":
                media_eng = "setn";
                break;
            default:
//                media_name = "";
                break;

        }
        switch (media_ch) {
            case "cna":
                media_ch = "中央社";
                break;
            case "chinatimes":
                media_ch = "中時";
                break;
            case "cts":
                media_ch = "華視";
                break;
            case "ebc":
                media_ch = "東森";
                break;
            case "ltn":
                media_ch = "自由時報";
                break;
            case "storm":
                media_ch = "風傳媒";
                break;
            case "udn":
                media_ch = "聯合";
                break;
            case "ettoday":
                media_ch = "ettoday";
                break;
            case "setn":
                media_ch = "三立";
                break;
            default:
//                media_name = "";
                break;

        }

        //count different read sum
        SharedPreferences.Editor editor = sharedPrefs.edit();
        int read_sum = sharedPrefs.getInt(READ_TOTAL + media_eng, 0);
        editor.putInt(READ_TOTAL + media_eng, read_sum + 1);

        Calendar calendar = Calendar.getInstance();
        int day_index = calendar.get(Calendar.DAY_OF_YEAR);
        int day_sum = sharedPrefs.getInt(READ_TOTAL + day_index + media_eng, 0);
        editor.putInt(READ_TOTAL + day_index + media_eng, day_sum + 1);

        editor.apply();

        myReadingBehavior.setKEY_MEDIA(media_ch);
//        Log.d("log: media_name", media_name);
//        Log.d("log: time_in", myReadingBehavior.getKEY_TIME_IN());
        enter_timestamp = Timestamp.now();//new Timestamp(System.currentTimeMillis());
        //set gesture listener #####################################################################
        detector = new GestureListener(this,this);
        //check screen on or off ###################################################################
        //screen off #########################
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        //screen on or off
//        ScreenStateReceiver mReceiver = new ScreenStateReceiver();
//        registerReceiver(mReceiver, intentFilter);
        //screen size ##############################################################################
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        final float dpWidth = outMetrics.widthPixels / density;
        myReadingBehavior.setKEY_DISPLAY_HEIGHT(dpHeight);
        myReadingBehavior.setKEY_DISPLAY_WIDTH(dpWidth);
//        Log.d("log: display_width_dp", myReadingBehavior.getKEY_DISPLAY_WIDTH());
//        Log.d("log: display_height_dp", myReadingBehavior.getKEY_DISPLAY_HEIGHT());
        //whether is chinese #######################################################################
//        final Set<UCharacter.UnicodeBlock> chineseUnicodeBlocks = new HashSet<UCharacter.UnicodeBlock>() {{
//            add(UCharacter.UnicodeBlock.CJK_COMPATIBILITY);
//            add(UCharacter.UnicodeBlock.CJK_COMPATIBILITY_FORMS);
//            add(UCharacter.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
//            add(UCharacter.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
//            add(UCharacter.UnicodeBlock.CJK_RADICALS_SUPPLEMENT);
//            add(UCharacter.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION);
//            add(UCharacter.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
//            add(UCharacter.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
//            add(UCharacter.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B);
//            add(UCharacter.UnicodeBlock.KANGXI_RADICALS);
//            add(UCharacter.UnicodeBlock.IDEOGRAPHIC_DESCRIPTION_CHARACTERS);
//        }};
        //Scrollview has lost the acceleration######################################################
//        ScrollView mScrollView = findViewById(R.id.scroll_view);
//        mScrollView.setNestedScrollingEnabled(false);
        //news generate from server ################################################################
//        Random rand = new Random();
//        int random_news = ThreadLocalRandom.current().nextInt(1, 3 + 1);
//        String doc_id = "";
//        Log.d("log: firebase news", String.valueOf(random_news));
//        List<String> list = Arrays.asList("0000e3633ce3f3b0241d69749fc749f0", "0011f17045e0d4f40cc314f27ac91228", "001b575e65a5dd618051065f43b79974", "0030b7b0dada6069a76fb087f631bbb1", "003436a77eccd9d8f0cc9ffbced6844b");
//        doc_id = list.get(rand.nextInt(list.size()));
//        Log.d("log: firebase news", doc_id);
        DocumentReference docRef;
        if (news_id.equals("") || media_eng.equals("")){
            media_eng = "ettoday";
            news_id = "0143b739b1c33d46cd18b6af12b2d5b2";
            docRef = db.collection(MEDIA_COLLECTION).document(media_eng).collection(NEWS_COLLECTION).document(news_id);
            Toast.makeText(getApplicationContext(), "沒有資料qq", Toast.LENGTH_SHORT).show();
        } else {
//            docRef = db.collection(MEDIA_COLLECTION).document(media_name).collection(NEWS_COLLECTION).document(news_id);
            docRef = db.collection(NEWS_COLLECTION).document(news_id);
            Toast.makeText(getApplicationContext(), "努力loading中!!", Toast.LENGTH_SHORT).show();
            myReadingBehavior.setKEY_NEWS_ID(news_id);
        }
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("RtlHardcoded")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
//                        Log.d("log: firebase", "Success");
//                        Log.d("log: firebase", "DocumentSnapshot data: " + document.getData());
//                        mUrl = "";
                        if(document.getString(NEWS_URL)!=null){
                            mUrl = document.getString(NEWS_URL);
                        }
//                        mTitle = "";
                        if(document.getString(NEWS_TITLE)!=null){
                            mTitle = document.getString(NEWS_TITLE);
                            myReadingBehavior.setKEY_TITLE(mTitle);
                        }
//                        mImg = "NA";
//                        if(document.getString("media").equals("聯合")){
                            if(document.getString(NEWS_IMAGE)!=null){
                                mImg = document.getString(NEWS_IMAGE);
                            }
//                        }
                        mPubdate = Timestamp.now();
                        if(document.getTimestamp(NEWS_PUBDATE)!=null){
                            mPubdate = document.getTimestamp(NEWS_PUBDATE);
                            assert mPubdate != null;
                            myReadingBehavior.setKEY_PUBDATE(mPubdate.getSeconds());
                        }
//                        if(document.get(NEWS_CATEGORY)!=null){
//                            if(media_name.equals("storm")){
//                                categoryArray =  (List<String>) document.get("category");
////                                document.get("category")
////                                Log.d("log: firebase", String.valueOf(categoryArray));
//                            } else {
//                                categoryArray.add(document.getString(NEWS_CATEGORY));
////                                Log.d("log: firebase", categoryArray.get(0));
//                            }
//                        } else {
////                            Log.d("log: firebase", "123");
//                        }


                        Date my_date = mPubdate.toDate();
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                        List<String> my_tt = new ArrayList<>(Arrays.asList(formatter.format(my_date).split(" ")));
                        mDate = String.format("%s %s", my_tt.get(0), my_tt.get(2));
//                        mSource = document.getString(NEWS_MEDIA);
                        mSource = media_ch;
                        myReadingBehavior.setKEY_NEWS_ID(document.getString(NEWS_ID));
//                        ArrayList<String> c_list = null;
                        ArrayList<String> c_list = new ArrayList<>();
                        if(document.get(NEWS_CONTENT)!=null){
                            c_list = (ArrayList<String>) document.get(NEWS_CONTENT);
                            for (int i = 0; i < Objects.requireNonNull(c_list).size(); i++) {
                                c_list.set(i, c_list.get(i).trim().replaceAll("\n", ""));
//                                Log.d("log: firebase", "DocumentSnapshot content: " + c_list.get(i));
                            }
                            if(c_list.size()==0){
                                //no content
                                c_list.add(0, "內容載入失敗，不好意思~");
                                c_list.add(1, "有空麻煩請向我們回報是哪一篇(截圖即可)");
                            }
                        } else {
                            c_list.add(0, "內容載入失敗，不好意思~");
                            c_list.add(1, "有空麻煩請向我們回報是哪一篇(截圖即可)");
                        }

//                        Log.d("log: firebase", "DocumentSnapshot content: end");
                        List<String> divList = new ArrayList<>();
//                        int cut_size = (int) (dpWidth / 26);
                        int divided_by;
                        if(text_size_string.equals("1")){
                            divided_by = 22;
                        } else if (text_size_string.equals("0")){//smaller
                            divided_by = 18;
                        } else {//bigger
                            divided_by = 25;
                        }
                        int cut_size = (int) (dpWidth / divided_by);
                        myReadingBehavior.setKEY_BYTE_PER_LINE(cut_size*2);
                        //loop for each paragraph
                        for (int i = 0; i < c_list.size(); i++) {
                            if (c_list.get(i).length()==0 || TextUtils.isEmpty(c_list.get(i))){
//                                Log.d("log: firebase", "blank line");
                                continue;
                            } else if (c_list.get(i).contains("\n")){
//                                c_list.set(i, c_list.get(i)).trim().replaceAll("\n", "");
//                                Log.d("log: firebase", "detect new line");
                                continue;//should not happen
                            }  //全形空白 it works
                            //                                Log.d("log: firebase", "detect \u3000");

                            //full blank to half
                            String str = c_list.get(i).replaceAll("　", " ");
                            int front = 0, iter_char_para = 0;
                            boolean last_line_in_p = false;
                            str = str.replaceAll("　", " ");
                            char[] para = str.toCharArray();
                            //one paragraph split to line
                            while (true){
                                //                                Log.d("log: firebase", "this is line " + para_count);
                                int iter_char_line = 0;
                                while (true){
                                    //remove line with space first
                                    if (iter_char_line==0 && para[iter_char_para]==' '){
                                        iter_char_para+=1;
                                        front+=1;
                                        continue;
                                    }
                                    if (isChineseChar(para[iter_char_para]) || is_except(para[iter_char_para])){
//                                        Log.d("log: firebase", "chinese " + para[iter_char_para]);
                                        //is chinese
                                        //first check line space
                                        int tmp_iter_char_line = iter_char_line+2;//tmp cursor
                                        int tmp_iter_char_para = iter_char_para+1;//tmp cursor
                                        if (tmp_iter_char_line <= (cut_size*2)){
                                            iter_char_line = tmp_iter_char_line;
                                            //second check if last char in para
                                            if (tmp_iter_char_para < para.length){
                                                //not last one
                                                iter_char_para = tmp_iter_char_para;
                                            } else {
                                                //is last char in para
                                                last_line_in_p = true;
                                                break;
                                            }
                                        } else {
                                            //line space not enough
//                                            Log.d("log: firebase", "new line");
                                            break;
                                        }
                                    } else if (is_full_en(para[iter_char_para])){
//                                        Log.d("log: firebase", "full en " + para[iter_char_para]);
                                        //is number word
                                        //find word
                                        int tmp_iter_char_line = iter_char_line+2;
                                        int tmp_iter_char_para = iter_char_para+1;
                                        for (; tmp_iter_char_para< para.length; tmp_iter_char_para++){
                                            if (is_full_en(para[tmp_iter_char_para])){
//                                                Log.d("log: firebase", "full en " + para[tmp_iter_char_para]);
                                                tmp_iter_char_line +=2;
                                            } else {
//                                                Log.d("log: firebase", "break " + para[tmp_iter_char_para]);
                                                tmp_iter_char_para--;
                                                break;
                                            }
                                        }
                                        //check line space
                                        if (tmp_iter_char_line <= (cut_size*2)){
                                            iter_char_line = tmp_iter_char_line;
                                            //second check if last char in para
                                            if (tmp_iter_char_para < para.length){
                                                iter_char_para = tmp_iter_char_para+1;
                                            } else {
                                                //is last char in para
                                                iter_char_para = tmp_iter_char_para-1;
                                                last_line_in_p = true;
                                                break;
                                            }
                                        } else {
                                            break;
                                        }
                                    } else if (is_full_num(para[iter_char_para])){
//                                        Log.d("log: firebase", "full num " + para[iter_char_para]);
                                        //is number word
                                        //find word
                                        int tmp_iter_char_line = iter_char_line+2;
                                        int tmp_iter_char_para = iter_char_para+1;
                                        for (; tmp_iter_char_para< para.length; tmp_iter_char_para++){
                                            if (is_full_num(para[tmp_iter_char_para])){
                                                tmp_iter_char_line +=2;
                                            } else {
                                                tmp_iter_char_para--;
                                                break;
                                            }
                                        }
                                        //check line space
                                        if (tmp_iter_char_line <= (cut_size*2)){
                                            iter_char_line = tmp_iter_char_line;
                                            //second check if last char in para
                                            if (tmp_iter_char_para < para.length){
                                                iter_char_para = tmp_iter_char_para+1;
                                            } else {
                                                //is last char in para
                                                iter_char_para = tmp_iter_char_para-1;
                                                last_line_in_p = true;
                                                break;
                                            }
                                        } else {
                                            break;
                                        }
                                    } else if ((para[iter_char_para] >= '0' && para[iter_char_para] <= '9')){
//                                        Log.d("log: firebase", "num " + para[iter_char_para]);
                                        //is number word
                                        //find word
                                        int tmp_iter_char_line = iter_char_line+1;
                                        int tmp_iter_char_para = iter_char_para+1;
                                        for (; tmp_iter_char_para< para.length; tmp_iter_char_para++){
                                            if ((para[tmp_iter_char_para] >= '0' && para[tmp_iter_char_para] <= '9')){
                                                tmp_iter_char_line +=1;
                                            } else {
                                                tmp_iter_char_para--;
                                                break;
                                            }
                                        }
                                        //check line space
                                        if (tmp_iter_char_line <= (cut_size*2)){
                                            iter_char_line = tmp_iter_char_line;
                                            //second check if last char in para
                                            if (tmp_iter_char_para < para.length){
                                                iter_char_para = tmp_iter_char_para+1;
                                            } else {
                                                //is last char in para
                                                iter_char_para = tmp_iter_char_para-1;
                                                last_line_in_p = true;
                                                break;
                                            }
                                        } else {
                                            break;
                                        }
                                    } else if ((para[iter_char_para] >= 'a' && para[iter_char_para] <= 'z') || (para[iter_char_para] >= 'A' && para[iter_char_para] <= 'Z') || is_latin(para[iter_char_para])){
//                                        Log.d("log: firebase", "english " + para[iter_char_para]);
                                        //english word
                                        //find word
                                        int tmp_iter_char_line = iter_char_line+1;
                                        int tmp_iter_char_para = iter_char_para+1;
                                        for (; tmp_iter_char_para< para.length; tmp_iter_char_para++){
//                                            Log.d("log: firebase", "english ***** " + para[tmp_iter_char_para]);
                                            if ((para[tmp_iter_char_para] >= 'a' && para[tmp_iter_char_para] <= 'z') || (para[tmp_iter_char_para] >= 'A' && para[tmp_iter_char_para] <= 'Z')){
                                                tmp_iter_char_line +=1;
                                            } else {
                                                tmp_iter_char_para--;
                                                break;
                                            }
                                        }
                                        //check line space
                                        if (tmp_iter_char_line <= (cut_size*2)){
                                            iter_char_line = tmp_iter_char_line;
                                            //second check if last char in para
                                            if (tmp_iter_char_para < para.length){
                                                iter_char_para = tmp_iter_char_para+1;
//                                                Log.d("log: firebase", "en2 " + para[iter_char_para]);
                                            } else {
                                                //is last char in para
                                                iter_char_para = tmp_iter_char_para-1;
                                                last_line_in_p = true;
                                                //                                                Log.d("log: firebase", "en3 " + para[iter_char_para]);
                                                break;
                                            }
                                        } else {
                                            //                                            Log.d("log: firebase", "en4 " + para[iter_char_para]);
                                            break;
                                        }
                                    } else {
                                        //other symbol
                                        Log.d("log: firebase", "symbol " + para[iter_char_para]);
                                        int tmp_iter_char_line = iter_char_line+1;//tmp cursor
                                        int tmp_iter_char_para = iter_char_para+1;//tmp cursor
                                        if (tmp_iter_char_line <= (cut_size*2)){
                                            iter_char_line = tmp_iter_char_line;
                                            //second check if last char in para
                                            if (tmp_iter_char_para < para.length){
                                                iter_char_para = tmp_iter_char_para;
                                            } else {
                                                //is last char in para
                                                last_line_in_p = true;
                                                break;
                                            }
                                        } else {
                                            //line space not enough
                                            break;
                                        }
                                    }
                                }
                                //end of line do some thing;
                                if(last_line_in_p){
                                    String childStr = str.substring(front, iter_char_para+1);
//                                    Log.d("log: firebase", childStr);
                                    divList.add(childStr);
                                    divList.add("\n");
                                    break;
                                } else {
                                    String childStr = str.substring(front, iter_char_para);
//                                    Log.d("log: firebase", childStr);
                                    divList.add(childStr);
                                    front = iter_char_para;
                                }
                            }
                        }
//                        myReadingBehavior.setKEY_CHAR_NUM_TOTAL(char_num_total);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(10, 10, 10, 10);
                        //set viewport number ######################################################
                        int textview_num = divList.size();
                        myReadingBehavior.setKEY_VIEW_PORT_NUM(textview_num);
//                        Log.d("log: view_port_num", String.valueOf(myReadingBehavior.getKEY_VIEW_PORT_NUM()));
                        //##########################################################################
                        //##########################################################################
                        //##########################################################################
                        //put title into layout
                        //int text_size = (int) (dpWidth /30);
                        int text_size;
                        if(text_size_string.equals("1")){
                            text_size = 20;
                        } else if (text_size_string.equals("0")){
                            text_size = 17;
                        } else {
                            text_size = 23;
                        }
                        final TextView myTextViewsTitle = new TextView(NewsModuleActivity.this);
                        final TextView myTextViewsDate = new TextView(NewsModuleActivity.this);
                        final TextView myTextViewsSrc = new TextView(NewsModuleActivity.this);
                        myTextViewsTitle.setText(mTitle);
                        myTextViewsDate.setText(mDate);
                        myTextViewsSrc.setText(mSource);
                        myTextViewsTitle.setTextColor(Color.parseColor("black"));
                        myTextViewsDate.setTextColor(Color.parseColor("black"));
                        myTextViewsSrc.setTextColor(Color.parseColor("black"));
                        myTextViewsTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28);
                        myTextViewsDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        myTextViewsSrc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        myTextViewsTitle.setGravity(Gravity.LEFT);
                        myTextViewsDate.setGravity(Gravity.LEFT);
                        myTextViewsSrc.setGravity(Gravity.LEFT);
                        myTextViewsTitle.setLayoutParams(params);
                        myTextViewsDate.setLayoutParams(params);
                        myTextViewsSrc.setLayoutParams(params);
                        ((LinearLayout) findViewById(R.id.layout_inside)).addView(myTextViewsTitle);
                        ((LinearLayout) findViewById(R.id.layout_inside)).addView(myTextViewsDate);
                        ((LinearLayout) findViewById(R.id.layout_inside)).addView(myTextViewsSrc);
                        //put img into layout ######################################################
                        final ImageView imageView = new ImageView(NewsModuleActivity.this);
                        if(!mImg.equals("NA") ){
                            has_img = 1;
                            new DownloadImageTask(imageView).execute(mImg);
                            ((LinearLayout) findViewById(R.id.layout_inside)).addView(imageView);
                        }
                        //put content into layut
                        final TextView[] myTextViews = new TextView[textview_num]; // create an empty array;
                        for (int i = 0; i < divList.size(); i++) {
                            final TextView rowTextView = new TextView(NewsModuleActivity.this);
                            String tmp = divList.get(i);
                            rowTextView.setText(tmp);
                            rowTextView.setTextColor(Color.parseColor("black"));
                            rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, text_size);
//                            rowTextView.setGravity(Gravity.LEFT);
                            rowTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            rowTextView.setSingleLine(true);
                            rowTextView.setLayoutParams(params);
                            //rowTextView.setBackgroundColor(0xFFFFFF99);
                            ((LinearLayout) findViewById(R.id.layout_inside)).addView(rowTextView);
                            myTextViews[i] = rowTextView;
                        }
                        myTextViews[0].measure(0, 0);
                        int h_dp_unit = pxToDp(myTextViews[0].getMeasuredHeight(), myTextViews[0].getContext());
                        myReadingBehavior.setKEY_ROW_SPACING(h_dp_unit);
                        //##########################################################################
                        //##########################################################################
                        //##########################################################################
                        final LinearLayout content_view = findViewById(R.id.layout_inside);
                        ViewTreeObserver viewTreeObserver = content_view.getViewTreeObserver();
                        if (viewTreeObserver.isAlive()) {
                            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onGlobalLayout() {
                                    content_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                    int viewWidth = content_view.getWidth();
                                    int viewHeight = content_view.getHeight();
                                    int dp_unit = pxToDp(viewHeight, content_view.getContext());
                                    myReadingBehavior.setKEY_CONTENT_LENGTH(dp_unit);
//                                    Log.d("log: content_length_dp", myReadingBehavior.getKEY_CONTENT_LENGTH());
                                }
                            });
                        }
                        //visibility check #########################################################
                        final int N = textview_num;
                        class VisibleChecker {
                            private ExecutorService executor = Executors.newFixedThreadPool(1);
                            public Future<Integer> calculate(final Integer input) {
                                return executor.submit(() -> {
                                    Thread.sleep(1000);
                                    return input * input;
                                });
                            }
                            public Future<Integer> visibility_check(final int input, final int start_count) {
                                return executor.submit(new Callable<Integer>() {
                                    float count_running = 0;
                                    int[] count = new int[N];
                                    int[] count_top = new int[4];
                                    boolean[] old_flag = new boolean[N];
                                    boolean[] new_flag = new boolean[N];
                                    boolean[] old_flag_top = new boolean[4];
                                    boolean[] new_flag_top = new boolean[4];
                                    @Override
                                    public Integer call() throws Exception {
                                        Arrays.fill(old_flag, Boolean.FALSE);
                                        Arrays.fill(new_flag, Boolean.FALSE);
                                        Arrays.fill(count, 0);
//                                        Log.d("log: MyScrollView", "Start");
//                                        myReadingBehavior.setKEY_TIME_ON_PAGE(count_running / 10);
                                        while (!activityEnd) {
//                                            Log.d("log: time_on_page", String.valueOf(myReadingBehavior.getKEY_TIME_ON_PAGE()));
                                            if (activityStopped && !activityEnd) {
//                                                Log.d("log: MyScrollView", "Stop");
                                                tmp_record = "";
                                                if(has_img==0){
                                                    tmp_record+=count_top[0] / 10 + "/";
                                                    tmp_record+=count_top[1] / 10 + "/";
                                                    tmp_record+=count_top[2] / 10 + "#";
                                                } else {
                                                    tmp_record+=count_top[0] / 10 + "/";
                                                    tmp_record+=count_top[1] / 10 + "/";
                                                    tmp_record+=count_top[2] / 10 + "/";
                                                    tmp_record+=count_top[3] / 10 + "#";
                                                }
                                                for (int i = 0; i < N; i++) {
                                                    tmp_record+=count[i] / 10 + "#";
                                                }
                                                myReadingBehavior.setKEY_VIEW_PORT_RECORD(tmp_record);
//                                                Log.d("log: view_port_record", myReadingBehavior.getKEY_VIEW_PORT_RECORD());
                                                while (activityStopped) {
                                                    Thread.sleep(100);
                                                }
//                                                Log.d("log: MyScrollView", "Restart");
                                            }
                                            Rect scrollBounds = new Rect();
                                            int first_view = -100, last_view = -100;//initial -1
                                            int initial_start = -3;
                                            if(has_img==1){
                                                initial_start = -4;
                                            }
                                            //title ################################################
                                            if (!myTextViewsTitle.getLocalVisibleRect(scrollBounds)) {
//                                                Log.d("log: layout", "1");
                                                new_flag_top[0] = false;
                                            } else {
//                                                Log.d("log: layout", "2");
                                                new_flag_top[0] = true;
                                                //-1
                                                first_view = initial_start;
                                            }
                                            if (old_flag_top[0] && new_flag_top[0]) {
                                                count_top[0]++;
                                            } else if (old_flag_top[0] && !new_flag_top[0]) {
                                                old_flag_top[0] = new_flag_top[0];
                                            } else if (!old_flag_top[0] && new_flag_top[0]) {
                                                count_top[0]++;
                                                old_flag_top[0] = new_flag_top[0];
                                            }
                                            //time #################################################
                                            if (!myTextViewsDate.getLocalVisibleRect(scrollBounds)) {
//                                                Log.d("log: layout", "1");
                                                new_flag_top[1] = false;
                                            } else {
//                                                Log.d("log: layout", "2");
                                                new_flag_top[1] = true;
                                                if(first_view==-100){//-1
                                                    first_view = initial_start+1;
                                                } else {
                                                    last_view = initial_start+1;
                                                }
                                            }
                                            if (old_flag_top[1] && new_flag_top[1]) {
                                                count_top[1]++;
                                            } else if (old_flag_top[1] && !new_flag_top[1]) {
                                                old_flag_top[1] = new_flag_top[1];
                                            } else if (!old_flag_top[1] && new_flag_top[1]) {
                                                count_top[1]++;
                                                old_flag_top[1] = new_flag_top[1];
                                            } else {
                                            }
                                            // media ###############################################
                                            if (!myTextViewsSrc.getLocalVisibleRect(scrollBounds)) {
//                                                Log.d("log: layout", "media 1");
                                                new_flag_top[2] = false;
                                            } else {
//                                                Log.d("log: layout", "media 2");
                                                new_flag_top[2] = true;
                                                if(first_view==-100){//-1
                                                    first_view = initial_start+2;
                                                } else {
                                                    last_view = initial_start+2;
                                                }
                                            }
                                            if (old_flag_top[2] && new_flag_top[2]) {
                                                count_top[2]++;
                                            } else if (old_flag_top[2] && !new_flag_top[2]) {
                                                old_flag_top[2] = new_flag_top[2];
                                            } else if (!old_flag_top[2] && new_flag_top[2]) {
                                                count_top[2]++;
                                                old_flag_top[2] = new_flag_top[2];
                                            } else {
                                            }
                                            //img ##################################################
                                            if(has_img==1){
                                                if (!imageView.getLocalVisibleRect(scrollBounds)) {
//                                                    Log.d("log: layout", "img 1");
                                                    new_flag_top[3] = false;
                                                } else {
//                                                    Log.d("log: layout", "img 2");
                                                    new_flag_top[3] = true;
                                                    if(first_view==-100){//-1
                                                        first_view = initial_start+3;
                                                    } else {
                                                        last_view = initial_start+3;
                                                    }
                                                }
                                            }
                                            if (old_flag_top[3] && new_flag_top[3]) {
                                                count_top[3]++;
                                            } else if (old_flag_top[3] && !new_flag_top[3]) {
                                                old_flag_top[3] = new_flag_top[3];
                                            } else if (!old_flag_top[3] && new_flag_top[3]) {
                                                count_top[3]++;
                                                old_flag_top[3] = new_flag_top[3];
                                            }
                                            //content ##############################################
                                            for (int i = 0; i < N; i++) {
                                                if (!myTextViews[i].getLocalVisibleRect(scrollBounds)) {
                                                    new_flag[i] = false;
                                                } else {
                                                    new_flag[i] = true;
                                                    if(first_view==-100){
                                                        first_view = i;
                                                    } else {
                                                        last_view = i;
                                                    }
                                                }

                                                if (old_flag[i] && new_flag[i]) {
                                                    // still visible
                                                    count[i]++;
                                                } else if (old_flag[i] && !new_flag[i]) {
                                                    //no longer visible
                                                    old_flag[i] = new_flag[i];
                                                } else if (!old_flag[i] && new_flag[i]) {
                                                    //start visible
                                                    count[i]++;
                                                    old_flag[i] = new_flag[i];
                                                }  // still not visible

                                            }
                                            Thread.sleep(100);
                                            count_running++;
                                            float temp = count_running/10;
//                                            String output_string = temp + " top: " + (first_view+1) + " bottom: " + (last_view+1) + "\n";
                                            String output_string = temp + "," + (first_view+1) + "," + (last_view+1) + "#";
//                                            Log.d("log: MyScrollView", output_string);
                                            tmp_time_series +=output_string;
                                            tmp_record = "";
                                            if(has_img==1){
                                                tmp_record+=count_top[0] / 10 + "/";
                                                tmp_record+=count_top[1] / 10 + "/";
                                                tmp_record+=count_top[2] / 10 + "/";
                                                tmp_record+=count_top[3] / 10 + "#";
                                            } else {
                                                tmp_record+=count_top[0] / 10 + "/";
                                                tmp_record+=count_top[1] / 10 + "/";
                                                tmp_record+=count_top[2] / 10 + "#";
                                            }
                                            for (int i = 0; i < N; i++) {
                                                tmp_record+=count[i] / 10 + "#";
                                            }
                                            myReadingBehavior.setKEY_VIEW_PORT_RECORD(tmp_record);
                                        }
//                                        Log.d("log: MyScrollView", "Finish");
//                                        Log.d("log: MyScrollView", "can not reach!!!!!!!!!!!!!!");
                                        String finish_record = "";
                                        for (int i = 0; i < N; i++) {
                                            finish_record+=count[i] / 10 + "#";
                                        }
                                        myReadingBehavior.setKEY_VIEW_PORT_RECORD(finish_record);
//                                        Log.d("log: view_port_record55", myReadingBehavior.getKEY_VIEW_PORT_RECORD());
                                        return 1;
                                    }
                                });
                            }
                        }
                        VisibleChecker visibleChecker = new VisibleChecker();
                        Future<Integer> future1 = visibleChecker.visibility_check(1, 0);
                    } else {
                        Log.d("log: firebase", "NewsModuleActivity No such document");
                    }
                } else {
                    Log.d("log: firebase", "get failed with ", task.getException());
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (first_in){
            addReadingBehavior();
            first_in = false;
        }
        Log.d("log: activity cycle", "NewsModuleActivity On start");
        activityStopped = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("log: activity cycle", "NewsModuleActivity On resume");
        activityStopped = false;
        //isScreenOn(R.layout.activity_news_detail);
        in_time = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("log: activity cycle", "NewsModuleActivity On pause");
        activityStopped = true;
        myReadingBehavior.setKEY_PAUSE_ON_PAGE(myReadingBehavior.getKEY_PAUSE_ON_PAGE()+1);
        long tmp = myReadingBehavior.getKEY_TIME_ON_PAGE() + (System.currentTimeMillis()-in_time)/1000;
        myReadingBehavior.setKEY_TIME_ON_PAGE(tmp);
//        Log.d("log: pause count", String.valueOf(myReadingBehavior.getKEY_PAUSE_ON_PAGE()));
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        myReadingBehavior.setKEY_TIME_OUT(formatter.format(date));
//        Log.d("log: time_out", myReadingBehavior.getKEY_TIME_OUT());

        myReadingBehavior.setKEY_TIME_SERIES(tmp_time_series);
        Log.d("log: time_series", myReadingBehavior.getKEY_TIME_SERIES());

        String drag_str = "";
        int drag_count = 0;
        float drag_x_1 = 0;
        float drag_y_1 = 0;
        float drag_x_2 = 0;
        float drag_y_2 = 0;
        long time_one = 0, time_two = 0, tmp_long = 0;
        double duration = 0;

        for(int iter = 0; iter < dragObjArrayListArray.size(); iter++){
            if (drag_x_1==0 && drag_y_1==0){
                time_one = dragObjArrayListArray.get(iter).getTIME_ONE();
                drag_x_1 = dragObjArrayListArray.get(iter).getPOINT_ONE_X();
                drag_y_1 = dragObjArrayListArray.get(iter).getPOINT_ONE_Y();
                drag_x_2 = dragObjArrayListArray.get(iter).getPOINT_TWO_X();
                drag_y_2 = dragObjArrayListArray.get(iter).getPOINT_TWO_Y();
            } else if (drag_x_1==dragObjArrayListArray.get(iter).getPOINT_ONE_X() && drag_y_1==dragObjArrayListArray.get(iter).getPOINT_ONE_Y()){
                time_two = dragObjArrayListArray.get(iter).getTIME_ONE();
                drag_x_2 = dragObjArrayListArray.get(iter).getPOINT_TWO_X();
                drag_y_2 = dragObjArrayListArray.get(iter).getPOINT_TWO_Y();
            } else {
                //find end
//                duration = (time_two-time_one)/1000;
                drag_count+=1;
                if(time_one!=time_two){
                    tmp_long = time_two-time_one;
                    duration = (double)tmp_long/1000;
                } else {
                    duration = 0;
                }
                Date d1 = new Date(time_one);
                Date d2 = new Date(time_two);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//                Date d1 = new Date(), d2 = new Date();
                try {
                    d1 = format.parse(String.valueOf(time_one));
//                    Log.d("log: nonon", String.valueOf(d1));
                } catch (ParseException e) {
                    e.printStackTrace();
//                    Log.d("log: nonon", "String.valueOf(d1)");
                }
                try {
                    d2 = format.parse(String.valueOf(time_two));
//                    Log.d("log: nonon", String.valueOf(d2));
                } catch (ParseException e) {
                    e.printStackTrace();
//                    Log.d("log: nonon", "String.valueOf(d1)");
                }
                List<String> my_d1 = new ArrayList<String>(Arrays.asList(formatter.format(d1).split(" ")));
                List<String> my_d2 = new ArrayList<String>(Arrays.asList(formatter.format(d2).split(" ")));
                drag_str+=duration + "/";
                drag_str+= my_d1.get(2) + "/"+ my_d2.get(2) + "/";
                drag_str+="(" + drag_x_1 + "," + drag_y_1 + ")/";
                drag_str+="(" + drag_x_2 + "," + drag_y_2 + ")/";

                String direction = "";
                double velocity_x = (Math.abs(drag_x_1-drag_x_2) / duration), velocity_y = (Math.abs(drag_y_1-drag_y_2) / duration);
                drag_str+= velocity_x + "/" + velocity_y + "/";
                direction += drag_y_1 < drag_y_2 ? "N" : drag_y_1 > drag_y_2 ? "S" : "";
                direction += drag_x_1 < drag_x_2 ? "E" : drag_x_1 > drag_x_2 ? "W" : "";
                drag_str+=direction + "#";

                drag_x_1 = 0;
                drag_y_1 = 0;
                drag_x_2 = 0;
                drag_y_2 = 0;
            }
        }
        //last drag
        if ((drag_x_1+drag_y_1+drag_x_2+drag_y_2)==0){
            //end at final else
//            Log.d("log: drag_str", "123");
        } else {
            drag_count+=1;
            if(time_one!=time_two){
                tmp_long = time_two-time_one;
                duration = (double) tmp_long/1000;
            } else {
                duration = 0;
            }
            Date d1 = new Date(time_one);
            Date d2 = new Date(time_two);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//            Date d1 = new Date(), d2 = new Date();
            try {
                d1 = format.parse(String.valueOf(time_one));
//                Log.d("log: nonon", String.valueOf(d1));
            } catch (ParseException e) {
                e.printStackTrace();
//                Log.d("log: nonon", "String.valueOf(d1)");
            }
            try {
                d2 = format.parse(String.valueOf(time_two));
//                Log.d("log: nonon", String.valueOf(d2));
            } catch (ParseException e) {
                e.printStackTrace();
//                Log.d("log: nonon", "String.valueOf(d1)");
            }
            List<String> my_d1 = new ArrayList<String>(Arrays.asList(formatter.format(d1).split(" ")));
            List<String> my_d2 = new ArrayList<>(Arrays.asList(formatter.format(d2).split(" ")));
            drag_str+=duration + "/";
            drag_str+= my_d1.get(2) + "/"+ my_d2.get(2) + "/";
            drag_str+="(" + drag_x_1 + "," + drag_y_1 + ")/";
            drag_str+="(" + drag_x_2 + "," + drag_y_2 + ")/";
            String direction = "";
            double velocity_x = (Math.abs(drag_x_1-drag_x_2) / duration), velocity_y = (Math.abs(drag_y_1-drag_y_2) / duration);
            drag_str+= velocity_x + "/" + velocity_y + "/";
            direction += drag_y_1 < drag_y_2 ? "N" : drag_y_1 > drag_y_2 ? "S" : "";
            direction += drag_x_1 < drag_x_2 ? "E" : drag_x_1 > drag_x_2 ? "W" : "";
            drag_str+=direction + "#";
        }
        myReadingBehavior.setKEY_DRAG_NUM(drag_count);
        myReadingBehavior.setKEY_DRAG_RECORD(drag_str);
        updateReadingBehavior();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("log: activity cycle", "NewsModuleActivity On stop");
        activityStopped = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("log: activity cycle", "NewsModuleActivity On restart");
        activityStopped = false;
//        in_time = System.currentTimeMillis();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDestroy() {
        Log.d("log: activity cycle", "NewsHybridActivity On destroy");
//        Map<String, Object> log_service = new HashMap<>();
//        log_service.put("service_timestamp", Timestamp.now());
//        log_service.put("flag", false);
//        log_service.put("cycle", "destroy");
//        log_service.put("activity", "NewsModuleActivity");
//        db.collection("test_users")
//                .document(device_id)
//                .collection("notification_service")
//                .document(String.valueOf(Timestamp.now()))
//                .set(log_service);
        super.onDestroy();
        Log.d("log: activity cycle", "NewsModuleActivity On destroy");
        activityEnd = true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
        if (!self_trigger){
            Intent intent = new Intent(NewsModuleActivity.this, NewsHybridActivity.class);
            startActivity(intent);
        }

    }

    public int pxToDp(int px, Context tmp) {
        DisplayMetrics displayMetrics = tmp.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private boolean isChineseChar(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ) {
            return true;
        }
        return false;
    }

    //image download ###############################################################################
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @SuppressLint("StaticFieldLeak")
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sample_news, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.share){
            String share_field = "";
            final DocumentReference rbRef = db.collection(READING_BEHAVIOR_COLLECTION).document(device_id + " " + myReadingBehavior.getKEY_IN_TIMESTAMP());
            if(share_clicked){
                rbRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        if (document.exists()) {
                            Log.d("log: firebase", "Success");
                            List<String> share_result = (List<String>) document.get(READING_BEHAVIOR_SHARE);
                            share_result.add("none");
//                                share_result.set(0,"none");
                            rbRef.update(READING_BEHAVIOR_SHARE, share_result)
                                    .addOnSuccessListener(aVoid -> Log.d("log: firebase", "DocumentSnapshot successfully updated!"))
                                    .addOnFailureListener(e -> Log.w("log: firebase", "Error updating document", e));
                        } else {
                            Log.d("log: firebase", "NewsModuleActivity No such document");
                        }
                    } else {
                        Log.d("log: firebase", "get failed with ", task.getException());
                    }
                });

            } else {
                //first time
                share_clicked = true;
                rbRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        if (document.exists()) {
                            Log.d("log: firebase", "Success");
                            List<String> share_result = (List<String>) document.get(READING_BEHAVIOR_SHARE);
                            share_result.set(0, "none");
                            rbRef.update(READING_BEHAVIOR_SHARE, share_result)
                                    .addOnSuccessListener(aVoid -> Log.d("log: firebase", "DocumentSnapshot successfully updated!"))
                                    .addOnFailureListener(e -> Log.w("log: firebase", "Error updating document", e));
                        } else {
                            Log.d("log: firebase", "NewsModuleActivity No such document");
                        }
                    } else {
                        Log.d("log: firebase", "get failed with ", task.getException());
                    }
                });
            }
            try{
                String url = mUrl;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,url); // your above url

                Intent receiver = new Intent(this, ApplicationSelectorReceiver.class);
                receiver.putExtra("device_id", device_id);
                receiver.putExtra("doc_time", String.valueOf(myReadingBehavior.getKEY_IN_TIMESTAMP()));
                receiver.putExtra("doc_date", String.valueOf(l_date));
                receiver.putExtra("share_field", share_field);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
                Intent chooser = Intent.createChooser(shareIntent, "Share via...", pendingIntent.getIntentSender());
                startActivity(chooser);

            }catch (Exception e){
                Toast.makeText(this, "Hmm.. Sorry, \nCannot be share", Toast.LENGTH_SHORT).show();
            }
        } else if (id == android.R.id.home){
            Intent intent_back = new Intent(NewsModuleActivity.this, NewsHybridActivity.class);
            startActivity(intent_back);
        }
//        else if (id == R.id.web){
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(mUrl));
//            startActivity(i);
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction, FlingObj flingObj) {
        myReadingBehavior.setKEY_FLING_NUM(myReadingBehavior.getKEY_FLING_NUM()+1);
        flingObj.setFLING_ID(myReadingBehavior.getKEY_FLING_NUM());
        String str_fling = myReadingBehavior.getKEY_FLING_RECORD();
        if(str_fling.equals("NA")){
            str_fling = "";
        }
//        str_fling+="fling num:" + flingObj.getFLING_ID() + "/";
        str_fling+="(" + flingObj.getPOINT_ONE_X() + "," + flingObj.getPOINT_ONE_Y() + ")/";
        str_fling+="(" + flingObj.getPOINT_TWO_X() + "," + flingObj.getPOINT_TWO_Y() + ")/";
        str_fling+=flingObj.getDISTANCE_X() + "/";
        str_fling+=flingObj.getDISTANCE_Y() + "/";
        str_fling+=flingObj.getVELOCITY_X() + "/";
        str_fling+=flingObj.getVELOCITY_Y() + "/";
        String direction_f = "";
        direction_f += flingObj.getPOINT_ONE_Y() < flingObj.getPOINT_TWO_Y() ? "N" : flingObj.getPOINT_ONE_Y() > flingObj.getPOINT_TWO_Y() ? "S" : "";
        direction_f += flingObj.getPOINT_ONE_X() < flingObj.getPOINT_TWO_X() ? "E" : flingObj.getPOINT_ONE_X() > flingObj.getPOINT_TWO_X() ? "W" : "";
        str_fling+=direction_f + "#";
        myReadingBehavior.setKEY_FLING_RECORD(str_fling);
    }

    @Override
    public void onOnePoint(DragObj dragObj) {
        dragObjArrayListArray.add(dragObj);
    }

    public static Point getTouchPositionFromDragEvent(View item, DragEvent event) {
        Rect rItem = new Rect();
        item.getGlobalVisibleRect(rItem);
        return new Point(rItem.left + Math.round(event.getX()), rItem.top + Math.round(event.getY()));
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LocalDate l_date = LocalDate.now();

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addReadingBehavior() {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, Object> readingBehavior = new HashMap<>();

        readingBehavior.put(READING_BEHAVIOR_DOC_ID,  device_id + " " + myReadingBehavior.getKEY_IN_TIMESTAMP());
        readingBehavior.put(READING_BEHAVIOR_DEVICE_ID,  device_id);
        readingBehavior.put(READING_BEHAVIOR_USER_ID,  sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
        readingBehavior.put(READING_BEHAVIOR_SAMPLE_CHECK_ID, "NA");
        //select esm id
        readingBehavior.put(READING_BEHAVIOR_TRIGGER_BY, myReadingBehavior.getKEY_TRIGGER_BY());
        readingBehavior.put(READING_BEHAVIOR_NEWS_ID,  news_id);
        readingBehavior.put(READING_BEHAVIOR_TITLE,myReadingBehavior.getKEY_TITLE());
        readingBehavior.put(READING_BEHAVIOR_MEDIA, myReadingBehavior.getKEY_MEDIA());
        readingBehavior.put(READING_BEHAVIOR_HAS_IMAGE, has_img);
        readingBehavior.put(READING_BEHAVIOR_IMAGE_URL, mImg);
        //pubdate
        readingBehavior.put(READING_BEHAVIOR_ROW_SPACING, "NA");
        readingBehavior.put(READING_BEHAVIOR_BYTE_PER_LINE, "NA");
        readingBehavior.put(READING_BEHAVIOR_FONT_SIZE,  text_size_string);

        readingBehavior.put(READING_BEHAVIOR_CONTENT_LENGTH, "NA");
        readingBehavior.put(READING_BEHAVIOR_DISPLAY_WIDTH, myReadingBehavior.getKEY_DISPLAY_WIDTH());
        readingBehavior.put(READING_BEHAVIOR_DISPLAY_HEIGHT, myReadingBehavior.getKEY_DISPLAY_HEIGHT());
        readingBehavior.put(READING_BEHAVIOR_IN_TIME, enter_timestamp);
        readingBehavior.put(READING_BEHAVIOR_IN_TIME_LONG, enter_timestamp.getSeconds());
        readingBehavior.put(READING_BEHAVIOR_OUT_TIME, Timestamp.now());
//        readingBehavior.put(READING_BEHAVIOR_OUT_TIME_LONG, Timestamp.now().getSeconds());
        readingBehavior.put(READING_BEHAVIOR_TIME_ON_PAGE, myReadingBehavior.getKEY_TIME_ON_PAGE());
        readingBehavior.put(READING_BEHAVIOR_PAUSE_COUNT, myReadingBehavior.getKEY_PAUSE_ON_PAGE());
        readingBehavior.put(READING_BEHAVIOR_VIEWPORT_NUM, "NA");
        readingBehavior.put(READING_BEHAVIOR_VIEWPORT_RECORD, Arrays.asList("NA"));
        readingBehavior.put(READING_BEHAVIOR_FLING_NUM, myReadingBehavior.getKEY_FLING_NUM());
        readingBehavior.put(READING_BEHAVIOR_FLING_RECORD, Arrays.asList("NA"));
        readingBehavior.put(READING_BEHAVIOR_DRAG_NUM, myReadingBehavior.getKEY_DRAG_NUM());
        readingBehavior.put(READING_BEHAVIOR_DRAG_RECORD, Arrays.asList("NA"));
        readingBehavior.put(READING_BEHAVIOR_SHARE, Arrays.asList("NA"));
        readingBehavior.put(READING_BEHAVIOR_TIME_SERIES, Arrays.asList("NA"));

//        readingBehavior.put(READING_BEHAVIOR_SAMPLE_CHECK, false);
//        readingBehavior.put(READING_BEHAVIOR_CATEGORY,categoryArray);

        myReadingBehavior.setKEY_DOC_ID(device_id + " " + myReadingBehavior.getKEY_IN_TIMESTAMP());
        myReadingBehavior.setKEY_DEVICE_ID(device_id);
        myReadingBehavior.setKEY_USER_ID(sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
        myReadingBehavior.setKEY_SELECT_ESM_ID("NA");
        myReadingBehavior.setKEY_HAS_IMG(has_img);
        myReadingBehavior.setKEY_PUBDATE(0);
        myReadingBehavior.setKEY_FONT_SIZE(text_size_string);

        ReadingBehaviorDbHelper dbHandler = new ReadingBehaviorDbHelper(getApplicationContext());
        dbHandler.insertReadingBehaviorDetailsCreate(myReadingBehavior);

        db.collection(READING_BEHAVIOR_COLLECTION)
                .document(device_id + " " + myReadingBehavior.getKEY_IN_TIMESTAMP())
                .set(readingBehavior);
        document_create = true;
    }
    public void updateReadingBehavior(){
        @SuppressLint("MissingPermission")
        DocumentReference rbRef = db.collection(READING_BEHAVIOR_COLLECTION).document(device_id + " " +  myReadingBehavior.getKEY_IN_TIMESTAMP());

        List<String> time_series_list = new ArrayList<>(Arrays.asList(myReadingBehavior.getKEY_TIME_SERIES().split("#")));
        List<String> viewport_record_list = new ArrayList<>(Arrays.asList(myReadingBehavior.getKEY_VIEW_PORT_RECORD().split("#")));
        List<String> drag_record_list = new ArrayList<>(Arrays.asList(myReadingBehavior.getKEY_DRAG_RECORD().split("#")));
        List<String> fling_record_list = new ArrayList<>(Arrays.asList(myReadingBehavior.getKEY_FLING_RECORD().split("#")));

        myReadingBehavior.setKEY_TITLE(mTitle);
        myReadingBehavior.setKEY_HAS_IMG(has_img);
        if((mPubdate !=null)){
            myReadingBehavior.setKEY_PUBDATE(mPubdate.getSeconds());
        } else {
            myReadingBehavior.setKEY_PUBDATE(0);
        }

        myReadingBehavior.setKEY_OUT_TIMESTAMP(Timestamp.now().getSeconds());

        ReadingBehaviorDbHelper dbHandler = new ReadingBehaviorDbHelper(getApplicationContext());
        dbHandler.UpdateReadingBehaviorDetails(myReadingBehavior);

        rbRef.update(
                READING_BEHAVIOR_NEWS_ID,  myReadingBehavior.getKEY_NEWS_ID(),
                READING_BEHAVIOR_TITLE, myReadingBehavior.getKEY_TITLE(),
                READING_BEHAVIOR_MEDIA, myReadingBehavior.getKEY_MEDIA(),
                READING_BEHAVIOR_HAS_IMAGE, has_img,
                READING_BEHAVIOR_IMAGE_URL, mImg,
                READING_BEHAVIOR_PUBDATE,mPubdate,

                READING_BEHAVIOR_ROW_SPACING, myReadingBehavior.getKEY_ROW_SPACING(),
                READING_BEHAVIOR_BYTE_PER_LINE, myReadingBehavior.getKEY_BYTE_PER_LINE(),

                READING_BEHAVIOR_CONTENT_LENGTH, myReadingBehavior.getKEY_CONTENT_LENGTH(),

//                READING_BEHAVIOR_OUT_TIME, myReadingBehavior.getKEY_OUT_TIMESTAMP(),
                READING_BEHAVIOR_OUT_TIME, new Timestamp(myReadingBehavior.getKEY_OUT_TIMESTAMP(), 0),
//                READING_BEHAVIOR_OUT_TIME_LONG, myReadingBehavior.getKEY_OUT_TIMESTAMP(),

                READING_BEHAVIOR_TIME_ON_PAGE, myReadingBehavior.getKEY_TIME_ON_PAGE(),//auto
                READING_BEHAVIOR_PAUSE_COUNT, myReadingBehavior.getKEY_PAUSE_ON_PAGE(),//auto
                READING_BEHAVIOR_VIEWPORT_NUM, myReadingBehavior.getKEY_VIEW_PORT_NUM(),
                READING_BEHAVIOR_VIEWPORT_RECORD, viewport_record_list,
                READING_BEHAVIOR_FLING_NUM, myReadingBehavior.getKEY_FLING_NUM(),
                READING_BEHAVIOR_FLING_RECORD, fling_record_list,
                READING_BEHAVIOR_DRAG_NUM, myReadingBehavior.getKEY_DRAG_NUM(),
                READING_BEHAVIOR_DRAG_RECORD, drag_record_list,
                READING_BEHAVIOR_TIME_SERIES, time_series_list//auto
                )//auto
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("log: firebase update", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("log: firebase update", "Error updating document", e);
                    }
                });



    }


}