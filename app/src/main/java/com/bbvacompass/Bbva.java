package com.bbvacompass;

import android.app.Application;
import android.content.Context;

import com.bbvacompass.core.net.Command;

/**
 * Created by vivek.kallur on 2/15/17.
 */

public class Bbva extends Application {

    // set this to test search results from assets
    public static final boolean LOAD_LOCAL_FILE = false;

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Command.init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Command.release();
    }

    public static Context getContext() {
        return mContext;
    }
}
