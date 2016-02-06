package com.example.weather.app;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private EditText mEnterCity;
    private Button mButtonSearch;
    private DownloadsWeather mWeather;
    private TextView mResultTextView;
    private HttpURLConnection httpURLConnection;

    //when you click for button "Search" this method being invoked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEnterCity = (EditText) findViewById(R.id.etCity);
        mResultTextView = (TextView) findViewById(R.id.tvResult);

        mButtonSearch = (Button) findViewById(R.id.search_button);
        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Click on button", mEnterCity.getText().toString());

                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(mEnterCity.getWindowToken(), 0);

                try {
                    String encodeCityName = URLEncoder.encode(mEnterCity.getText().toString(),"UTF-8");

                    mWeather = new DownloadsWeather();
                    mWeather.execute("http://api.openweathermap.org/data/2.5/weather?q=" +
                            encodeCityName + "&appid=eee8ded8ee45a1c5e98a4da3e86a896f");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    public class DownloadsWeather extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String resultJSON = null;


            try {

                URL url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                String line;
                StringBuffer stringBuffer = new StringBuffer();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

//                int data = reader.read();

                while ((line = reader.readLine()) != null){
                    stringBuffer.append(line);
                    /* why we doing something like that, i can't understand
                        because its not normal, we must create String or in best
                        case StringBuffer and add to this our reader. I think so!
                    char current = (char) data;
                    result += current;
                    data = reader.read();*/


                }


                resultJSON = stringBuffer.toString();
                Log.i("Website content", resultJSON);






            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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

                String message = null;

                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather content", weatherInfo);

                JSONArray jsonArray = new JSONArray(weatherInfo);
                for(int i = 0; i < jsonArray.length(); i++){

                    JSONObject object = jsonArray.getJSONObject(i);

                    String main = object.getString("main");;
                    String description = object.getString("description");;


                    if(main != null && description != null){
                        message = main + ": " + description +"\r\n" ;
                    }
                    Log.i("Message", message);

                    if(message != null){
                         mResultTextView.setText(message);

                    } else Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();



                    Log.i("Main: ", object.getString("main"));
                    Log.i("Description", object.getString("description"));

                }


            } catch (JSONException e) {
                e.printStackTrace();
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
