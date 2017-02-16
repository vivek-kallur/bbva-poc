package com.bbvacompass.core.net;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by vivek.kallur on 2/15/17.
 */

public abstract class Request {
    public enum CONTENT_TYPE {JSON, XML}

    private CONTENT_TYPE mContentType;
    private String mUrl;

    public Request(String url, CONTENT_TYPE contentType) {
        mUrl = url;
        mContentType = contentType;
    }

    protected void addHeaders(HashMap<String, String> headers) {

    }

    protected HashMap<String, String> getHeaders() {
        return null;
    }

    protected abstract String build() throws IOException;

    public abstract Response getResponse(String responseString);
}
