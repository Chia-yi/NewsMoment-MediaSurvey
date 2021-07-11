package com.recoveryrecord.surveyandroid.example.sqlite;

public class ActivityRecognition {
    private String KEY_DOC_ID;
    private long KEY_TIMESTAMP;
    private String KEY_DEVICE_ID;
    private int KEY_SESSION;
    private String KEY_USING_APP;
    private String KEY_ACTIVITYRECOGNITION;
    private String KEY_USER_ID;

    public ActivityRecognition(){
        this.KEY_DOC_ID = "NA";
        this.KEY_TIMESTAMP = 0;
        this.KEY_DEVICE_ID = "NA";
        this.KEY_SESSION = 0;
        this.KEY_USING_APP = "NA";
        this.KEY_ACTIVITYRECOGNITION = "NA";
        this.KEY_USER_ID = "NA";
    }
    public ActivityRecognition(String KEY_DOC_ID,
                               long KEY_TIMESTAMP,
                               String KEY_DEVICE_ID,
                               String KEY_USER_ID,
                               int KEY_SESSION,
                               String KEY_USING_APP,
                               String KEY_ACTIVITYRECOGNITION){
        this.KEY_DOC_ID = KEY_DOC_ID;
        this.KEY_TIMESTAMP = KEY_TIMESTAMP;
        this.KEY_DEVICE_ID = KEY_DEVICE_ID;
        this.KEY_USER_ID = KEY_USER_ID;
        this.KEY_SESSION = KEY_SESSION;
        this.KEY_USING_APP = KEY_USING_APP;
        this.KEY_ACTIVITYRECOGNITION = KEY_ACTIVITYRECOGNITION;
    }
    public String getKEY_DOC_ID(){
        return KEY_DOC_ID;
    }

    public void setKEY_DOC_ID(String KEY_DOC_ID){
        this.KEY_DOC_ID = KEY_DOC_ID;
    }

    public long getKEY_TIMESTAMP(){
        return KEY_TIMESTAMP;
    }

    public void setKEY_TIMESTAMP(long KEY_TIMESTAMP){
        this.KEY_TIMESTAMP = KEY_TIMESTAMP;
    }

    public String getKEY_DEVICE_ID(){
        return KEY_DEVICE_ID;
    }

    public void setKEY_DEVICE_ID(String KEY_DEVICE_ID){
        this.KEY_DEVICE_ID = KEY_DEVICE_ID;
    }

    public String getKEY_USER_ID() {
        return KEY_USER_ID;
    }

    public void setKEY_USER_ID(String KEY_USER_ID) {
        this.KEY_USER_ID = KEY_USER_ID;
    }

    public int getKEY_SESSION(){
        return KEY_SESSION;
    }

    public void setKEY_SESSION(int KEY_SESSION){
        this.KEY_SESSION = KEY_SESSION;
    }

    public String getKEY_USING_APP(){
        return KEY_USING_APP;
    }

    public void setKEY_USING_APP(String KEY_USING_APP){
        this.KEY_USING_APP = KEY_USING_APP;
    }

    public String getKEY_ACTIVITYRECOGNITION(){
        return KEY_ACTIVITYRECOGNITION;
    }

    public void setKEY_ACTIVITYRECOGNITION(String KEY_ACTIVITYRECOGNITION){

        this.KEY_ACTIVITYRECOGNITION = KEY_ACTIVITYRECOGNITION;
    }
}
