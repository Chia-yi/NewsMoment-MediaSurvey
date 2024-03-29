/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.recoveryrecord.surveyandroid.example;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Outputs log data via Simple TextView.
 */
public class TestActivityRecognitionLogView extends TextView {

    public TestActivityRecognitionLogView(Context context) {
        super(context);
    }

    public TestActivityRecognitionLogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestActivityRecognitionLogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Formats the log data and prints it out to the TestActivityRecognitionLogView.
     * @param msg The actual message to be logged. The actual message to be logged.
     */
    public void println(final String msg) {

        // In case this was originally called from an AsyncTask or some other off-UI thread,
        // make sure the update occurs within the UI thread.
        ((Activity) getContext()).runOnUiThread( (new Thread(new Runnable() {
            @Override
            public void run() {
                // Display the text we just generated within the TestActivityRecognitionLogView.
                appendToLog(msg);
            }
        })));
    }

    /** Outputs the string as a new line of log data in the TestActivityRecognitionLogView. */
    public void appendToLog(String s) {
        append("\n" + s);
    }
}