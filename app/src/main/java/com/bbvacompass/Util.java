package com.bbvacompass;

import android.text.TextUtils;
import android.util.Log;

import com.bbvacompass.Bbva;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by vivek.kallur on 2/16/17.
 */

public class Util {
    public static String getFileFromSrcMainAssets(String filename) {
        if (TextUtils.isEmpty(filename))
            return "";

        StringBuilder buf = new StringBuilder();
        try {
            InputStream json = Bbva.getContext().getAssets().open(filename);
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buf.toString();
    }
}
