package com.bbvacompass.core.net;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.Voice;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by vivek.kallur on 2/15/17.
 */

public class ServiceTask extends AsyncTask<Void, Void, Boolean> {

    private Activity mContext;
    private Controller mController;
    private Bundle mParams;

    public ServiceTask(Activity context, Controller controller, Bundle params) {
        mContext = context;
        mController = controller;
        mParams = params;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            mController.execute(mContext, mParams);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        Intent intent = new Intent();
        intent.setAction(Controller.RECEIVER_ACTION);
        intent.putExtra("RESPONSE", mController.getResponse().getModel());
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
