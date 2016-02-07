package com.example.weather.app;

import android.widget.ImageView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pasha on 2/6/16.
 */
public class WeatherOfDay {

    public String mIconView;
    public String mMainTitle;
    public String mDescription;
    public int mId;

    @Override
    public String toString() {
        return mMainTitle +
                ": " + mDescription;
    }

    public WeatherOfDay(String mainTitle, String description, String iconView) {
        mMainTitle = mainTitle;
        mDescription = description;
        mIconView = iconView;
    }

    public WeatherOfDay(JSONObject object) throws JSONException {
            mMainTitle = object.getString("main");
            mDescription = object.getString("description");
            mIconView = object.getString("icon");
            mId = object.getInt("id");
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getIconView() {
        return mIconView;
    }

    public void setIconView(String iconView) {
        mIconView = iconView;
    }

    public String getMainTitle() {
        return mMainTitle;
    }

    public void setMainTitle(String mainTitle) {
        mMainTitle = mainTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
