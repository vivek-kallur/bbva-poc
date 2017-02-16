package com.bbvacompass.core.net;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.bbvacompass.core.net.search.TextSeachController;

import java.util.HashMap;

/**
 * Created by vivek.kallur on 2/15/17.
 */
public class Command {
    public static final Command SEARCH_TEXT = new Command("Google Map Text Search");

    private static HashMap<Command, Controller> mCommandMap;

    private String mName;

    public Command(String name) {
        mName = name;
    }

    public static void init() {
        addCommend(SEARCH_TEXT, new TextSeachController());
    }

    public static void release() {
        mCommandMap.clear();
    }

    private static void addCommend(Command command, Controller controller) {
        if (mCommandMap == null) {
            mCommandMap = new HashMap<>();
        }
        mCommandMap.put(command, controller);
    }

    public void execute(Command command, Activity activity, Bundle params, boolean isParallel) {
        Controller controller = mCommandMap.get(command);
        ServiceTask serviceTask = new ServiceTask(activity, controller, params);

        if (isParallel) {
            serviceTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            serviceTask.execute();
        }
    }
}
