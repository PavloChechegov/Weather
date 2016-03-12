package com.example.weather.app;

import android.util.Log;
import android.widget.ImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pasha on 2/6/16.
 */
public class Weather {


    private String mMainTitle;
    private String mDescription;
    private int mId;
    private int mTemperature;
    private String mCity;


    @Override
    public String toString() {
        return mMainTitle +
                ": " + mDescription;
    }

    public Weather(String mainTitle, String description, int id, int temperature) {
        mMainTitle = mainTitle;
        mDescription = description;
        mId = id;
        mTemperature = temperature;

    }

    public Weather(JSONObject object) throws JSONException {

        String weatherInfo = object.getString("weather");
        JSONArray jsonArray = new JSONArray(weatherInfo);
        JSONObject jsonWeatherObject = jsonArray.getJSONObject(0);

        JSONObject jsonMainObject = object.getJSONObject("main");
//        this(jsonWeatherObject.getString("main"),
//                jsonWeatherObject.getString("description"),
//                jsonWeatherObject.getInt("id"),
//                jsonMainObject.getDouble("temp"));

        mCity = object.getString("name");
        mMainTitle = jsonWeatherObject.getString("main");
        mDescription = jsonWeatherObject.getString("description");
        mId = jsonWeatherObject.getInt("id");
        mTemperature = jsonMainObject.getInt("temp");

    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
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

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(int temperature) {
        mTemperature = temperature;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }
}
