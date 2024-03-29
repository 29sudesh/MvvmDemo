package com.sb.ubergodriver.CommonUtils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.widget.SwitchCompat;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sb.ubergodriver.CommonUtils.CustomTextUtils.CustomTextView;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Locale;

public class Utils {

    private Context mContext;

    public Utils(Context context)
    {
        this.mContext=context;

    }

    public void changeLanguage(String language)
    {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());
    }



    public static String toJson(JsonElement jsonObject) {
        return new Gson().toJson(jsonObject);
    }

    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }


    public static boolean isValidEmail(String email)
    {
        // onClick of button perform this simplest code.
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern))
        {
           return true;
        }
        else
        {
            return false;
        }
    }

    public static void switchColor(boolean checked, SwitchCompat listSwitch) {
        if (checked) {
            listSwitch.getThumbDrawable().setColorFilter(Color.WHITE , PorterDuff.Mode.MULTIPLY);
            listSwitch.getTrackDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        }
        else {
            listSwitch.getThumbDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            listSwitch.getTrackDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
    }


    @Nullable
    public static String getDate(long toLong) {
        return DateFormat.format("dd MMM yyyy", new Date(toLong)).toString();
    }

    @Nullable
    public static String getDateWithtime(long toLong) {
        return DateFormat.format("MMM dd, hh:mm", new Date(toLong)).toString();
    }

    @Nullable
    public static String getTime(long toLong) {
        return DateFormat.format("hh:mm aa", new Date(toLong)).toString();
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
