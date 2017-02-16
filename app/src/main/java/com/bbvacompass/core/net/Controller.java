package com.bbvacompass.core.net;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by vivek.kallur on 2/15/17.
 */

public abstract class Controller {

    public static final String RECEIVER_ACTION = "TEXT_SEARCH";

    private Response mResponse;

    protected abstract void execute(Activity context, Bundle params) throws Exception;

    public void setResponse(Response response) {
        mResponse = response;
    }

    public Response getResponse() {
        return mResponse;
    }
}
