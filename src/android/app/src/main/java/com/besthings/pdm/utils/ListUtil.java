package com.besthings.pdm.utils;

import android.util.Log;

import java.util.List;

/**
 * Created by Chas on 2017/10/10 0010.
 */

public class ListUtil {
    public static String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0,sb.toString().length()-1);
    }

    public static String symbolReplace(String string, String separator) {
        Log.i("MyDesigner", string.replace("/", separator));
        return string.replace("/", separator);
    }
}
