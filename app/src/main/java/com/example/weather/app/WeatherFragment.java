package com.example.weather.app;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.pwittchen.weathericonview.WeatherIconView;


public class WeatherFragment extends Fragment {

    public static final String DESCRIPTION_TAG = "WeatherFragment";

    public WeatherIconView mWeatherIconView;
    public TextView mResultTextView;
    public TextView mTitleTextView;
    public TextView mTemperature;
    public TextView mCity;
    public TextView mTime;
    public String mLastUpdate = "last update ";
//    private Button mButton;
//    private Weather mWeatherOfDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        mWeatherIconView = (WeatherIconView) view.findViewById(R.id.ivIconWeather);
        mResultTextView = (TextView) view.findViewById(R.id.tvDescription);
        mTitleTextView = (TextView) view.findViewById(R.id.tvTitle);
        mTemperature = (TextView) view.findViewById(R.id.tvTemperature);
        mCity = (TextView) view.findViewById(R.id.tvCity);
        mTime = (TextView) view.findViewById(R.id.tvLastUpdate)
;
        return view;
    }

    public void initWeather(Weather weatherOfDay, String time){
        mWeatherIconView.setIconResource(getString(setWeatherIcon(weatherOfDay.getId())));
        mResultTextView.setText(weatherOfDay.getDescription());
        mTitleTextView.setText(weatherOfDay.getMainTitle());
        mTemperature.setText(String.valueOf( (int) (weatherOfDay.getTemperature() - 273)) + (char) 0x00B0);
        mCity.setText(weatherOfDay.getCity());
        mTime.setText(mLastUpdate + time);
    }


    //method that give me int to resource object;
    public int setWeatherIcon(int idIcon) {
        int id = idIcon / 100;
        int icon;
        if (idIcon == 800) {
            icon = R.string.wi_wu_sunny;
        } else {
            switch (id) {
                case 2:
                    icon = R.string.wi_thunderstorm;
                    break;
                case 3:
                    icon = R.string.wi_fog;
                    break;
                case 7:
                    icon = R.string.wi_wu_hazy;
                    break;
                case 8:
                    icon = R.string.wi_cloudy;
                    break;
                case 6:
                    icon = R.string.wi_snow;
                    break;
                case 5:
                    icon = R.string.wi_rain_wind;
                    break;
                default:
                    icon = R.string.wi_fire;
            }
        }

        return icon;
    }

}
