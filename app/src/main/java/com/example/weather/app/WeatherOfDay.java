package com.example.weather.app;

import android.widget.ImageView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pasha on 2/6/16.
 */
public class WeatherOfDay {

    public int mIconView;
    public String mMainTitle;
    public String mDescription;

    public WeatherOfDay(String mainTitle, String description, int iconView) {
        mMainTitle = mainTitle;
        mDescription = description;
        mIconView = iconView;
    }

    public WeatherOfDay(JSONObject object) throws JSONException {
            mMainTitle = object.getString("main");
            mDescription = object.getString("description");
            mIconView = object.getInt("icon");
    }

}
