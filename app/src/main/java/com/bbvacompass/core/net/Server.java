package com.bbvacompass.core.net;

import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vivek.kallur on 2/15/17.
 */

public class Server {

    private static final String TAG = "Server";

    private static final int TIMEOUT = 60 * 1000;

    public enum RequestType {
        Get("GET"),
        Post("POST");

        private String mRequestString;

        RequestType(String requestString) {
            mRequestString = requestString;
        }

        public String getRequestString() {
            return mRequestString;
        }
    }

    private String mURL;

    public Server(String url) throws MalformedURLException {
        if (url == null || !url.toLowerCase().startsWith("http")) {
            throw new MalformedURLException("Invalid URL");
        }
        mURL = url;
    }

    public Response sendGetRequest(Request request) throws IOException {
        String requestURL = mURL + request.build();
        URL url = new URL(requestURL);
        HttpsURLConnection urlConnection = getConnection(url, RequestType.Get, TIMEOUT);

        return handleResponse(request, urlConnection);
    }

    private HttpsURLConnection getConnection(URL url, RequestType type, int timeout) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod(type.getRequestString());
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setReadTimeout(timeout);

        //Post specific connection settings
        if (type == RequestType.Post) {
            connection.setDoOutput(true);
        }

        return connection;
    }

    private Response handleResponse(Request request, HttpsURLConnection connection) throws IOException {
        int serverResponseCode = connection.getResponseCode();
        String serverResponseMessage = connection.getResponseMessage();
        Log.i(TAG, "Status code: " + serverResponseCode + "\nStatus Message: " + serverResponseMessage);

        // TODO handled if SSLHandshakeException is thrown in case of an untrusted certificate
        // TODO use CertificateFactory, TrustManagerFactory, Certificate and KeyStore apis for HttpsURLConnection to trust.
        if (serverResponseCode == HttpURLConnection.HTTP_OK ||
                serverResponseCode == HttpURLConnection.HTTP_ACCEPTED ||
                serverResponseCode == HttpURLConnection.HTTP_CREATED) {

            LineNumberReader lnr = new LineNumberReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder responseTxt = new StringBuilder();

            while (null != (line = lnr.readLine())) {
                responseTxt.append(line.trim());
                responseTxt.append("\n");
            }

            Log.i(TAG, "Response received:\n" + responseTxt);

            Response response = request.getResponse(responseTxt.toString());
            response.setResponseCode(serverResponseCode);

            return response;
        } else if (serverResponseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new IOException("Unauthorized access");
        }

        throw new IOException("Error communicating with server: " + serverResponseCode + " : " + serverResponseMessage);
    }
}
