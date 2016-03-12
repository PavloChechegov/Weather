package com.example.weather.app;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SearchFragment.OnClickSearchListener {

    public DownloadAsyncTask mDownloadAsyncTask;
    public HttpURLConnection httpURLConnection;
    private FragmentManager mFragmentManager;
    private WeatherFragment mWeatherFragment;
    private FragmentTransaction mTransaction;
    private SearchFragment mSearchFragment;
    private double mLat, mLng;

    private SharedPreferences mSharedPreferences;

    private static final String PREFERENCES = "city_location";
    private static final String PREFERENCES_LATITUDE = "latitude";
    private static final String PREFERENCES_LONGITUDE = "longitude";
    private static final String PREFERENCES_CITYNAME = "city";

    private String mCityName;
    private Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mSharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        mSearchFragment = new SearchFragment();
        mFragmentManager = getSupportFragmentManager();

        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.container, mSearchFragment);


//        if (mSharedPreferences != null) {
//            Log.i("SharedPreferences", "Received");
//        }

        mTransaction.commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(PREFERENCES_CITYNAME, mCityName);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCityName = savedInstanceState.getString(PREFERENCES_CITYNAME);

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return weather;
    }



    @Override
    public void getLocation(String name) {
        mCityName = name;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocationName(name, 1);
            Address obj = addresses.get(0);

            mLat = obj.getLatitude();
            mLng = obj.getLongitude();
            Log.d("LOCATION_OF_CITY", "\n Address and data: "
                    + "\n Name: " + name
                    + "\n Latitude: " + mLat
                    + "\n Longitude: " + mLng);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

//        SharedPreferences.Editor editor = mSharedPreferences.edit();
//        editor.putString(PREFERENCES_CITYNAME, name);
//        editor.putString(PREFERENCES_LONGITUDE, Double.toString(mLng));
//        editor.putString(PREFERENCES_LATITUDE, Double.toString(mLat));
//        editor.apply();

        //see city location on google map
        seeLocation();

    }

    public void seeLocation() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("location_latitude", mLat);
        intent.putExtra("location_Longitude", mLng);
        intent.putExtra("CityName", mCityName);
        startActivity(intent);
    }

    @Override
    public void cityName(String city) {

        mCityName = city;
        mWeatherFragment = new WeatherFragment();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.addToBackStack(null);
        mTransaction.replace(R.id.container, mWeatherFragment, WeatherFragment.DESCRIPTION_TAG);
        mTransaction.commit();

        //encode our text that we typing in editText field, if we have some space1 or coma or something like that
        // this text we encoding for standard UTF-8

        //InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //must be in SearchFragment in future
        //manager.hideSoftInputFromWindow(mSearchFragment.mEnterCity.getWindowToken(), 0);


        try {
            //in background thread take weather data from this url
            String encodeCityName = URLEncoder.encode(mCityName, "UTF-8");
            Log.i("CityNameInActivity", mCityName);
            mDownloadAsyncTask = new DownloadAsyncTask();
            mDownloadAsyncTask.execute("http://api.openweathermap.org/data/2.5/weather?q=" +
                    encodeCityName + "&appid=eee8ded8ee45a1c5e98a4da3e86a896f");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_SHORT).show();
        }
    }

    public class DownloadAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String resultJSON = null;


            try {
                URL url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                String line;
                StringBuffer buffer = new StringBuffer();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJSON = buffer.toString();
                Log.i("Website content", resultJSON);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


            //must be result because this variable return to onProExecute method.
            return resultJSON;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //Log.i("Website content", result);
            try {
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                simpleDateFormat.format(date);
                Log.i("Date", simpleDateFormat.format(date));
                JSONObject jsonObject = new JSONObject(result);


                weather = (Weather) getLastCustomNonConfigurationInstance();

                if (weather == null){
                    weather = new Weather(jsonObject);
                    Log.i("Weather", weather.toString());
                }

                mWeatherFragment.initWeather(weather, simpleDateFormat.format(date));


                //String weatherInfo = jsonObject.getString("weather");
                //String mainInfo = jsonObject.getString("main");
                //JSONObject jsonMain = jsonObject.getJSONObject("main");

                //Log.i("MainInfo", mainInfo);
                //Log.i("Weather content", weatherInfo);

//                JSONArray jsonArray = new JSONArray(weatherInfo);
//                Log.i("JSON Array length", jsonArray.toString());
//                for (int i = 0; i < jsonArray.length(); i++) {
//
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    weatherOfDay = new Weather(object);
//
//
//                    if (weatherOfDay != null) {
//
//                        Log.i("mWeatherFragment", mWeatherFragment.toString());
//                    /*
//                        this line what doing, explain: 1. I get weather id from JSONObject(it can be 800, 801, 500, 400...)
//                        and put to method setWeatherIcon(id) take from this method int reference to resources file
//                         and put to method setIconResource. Its very difficult process but it works
//                        mWeatherIconView.setIconResource(getString(setWeatherIcon(weatherOfDay.getId())));
//                    */
//                        mWeatherFragment.initWeather(weatherOfDay);
//                        Log.i("Weather", weatherOfDay.toString());
//
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_SHORT).show();
//                    }
//
//                    Log.i("Main: ", object.getString("main"));
//                    Log.i("Description", object.getString("description"));
//
//                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
