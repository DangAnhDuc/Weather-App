package com.example.admin.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * AnhDucDang_Assignment1: WeatherApp
 * WeatherApp  is an application that display weather condition of current weather of Townsville region
 * Through the WeatherApp, the user can have the basic weather conditon such as temperature,humidity,
 * wind speed and
 *
 *
 * MainActivity. class contains the functionality to connect and get the data from API weather and
 * then pass it to display to correct component in activity_main.xml. The inner class also interact
 * with the SettingsActivity to peform the setting task to the  aspects of the content,which is can
 * not peform in the onCreate().
 * The MainActivity.class also override some methods to add the Toolbar with the item of option menu
 * which allow us to move to the setting screen
 *
 */
public class MainActivity extends AppCompatActivity {
    TextView tvCity, tvCountry, tvtTemp, tvStatus, tvHumidity, tvCloud, tvWind, tvDay;
    ImageView weatherView;
    boolean storedFahrenheit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Setup();
        GetCurrentWeatherData();

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

    }

    @Override
    public void onStart() {
        final String PREFS_NAME = "PREFS_FILE";

        //Shared Preferences with Setting activity
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        boolean enlargedVal = sharedPreferences.getBoolean("ENLARGED_SWITCH", false);
        storedFahrenheit = sharedPreferences.getBoolean("FAHRENHEIT_SWITCH", false);
        EnlargedTextSetting(enlargedVal);

        super.onStart();
    }


    //Change the size of text base on the setting
    public void EnlargedTextSetting(boolean enlarge) {
        float sp = getResources().getDisplayMetrics().scaledDensity;
        final int enlargeVal = 15;

        //Get current text size in actual pixel
        float cityTextSize = tvCity.getTextSize();
        float countryTextSize = tvCountry.getTextSize();
        float conditionTextSize = tvStatus.getTextSize();
        float dateTextSize = tvDay.getTextSize();

        if (enlarge) {
            if ((conditionTextSize / sp) != 30) {
                tvCity.setTextSize((cityTextSize + enlargeVal) / sp);
                tvCountry.setTextSize((countryTextSize + enlargeVal) / sp);
                tvStatus.setTextSize((conditionTextSize + enlargeVal) / sp);
                tvDay.setTextSize((dateTextSize + enlargeVal) / sp);
            }
        } else {
            if ((conditionTextSize / sp) != 29) {
                tvCity.setTextSize((conditionTextSize - enlargeVal) / sp);
                tvCountry.setTextSize((conditionTextSize - enlargeVal) / sp);
                tvStatus.setTextSize((conditionTextSize - enlargeVal) / sp);
                tvDay.setTextSize((dateTextSize - enlargeVal) / sp);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        //Action Bar menu
        switch (item.getItemId()) {
            case R.id.option_menu:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetCurrentWeatherData();
    }

    //Main function to get and display the data
    public void GetCurrentWeatherData() {

        //Create a request for the MainActivity and connect with the api to get the data from internet
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://api.apixu.com/v1/current.json?key=611a3e6b33314b3591931329182103&q=Townsville";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Create jsonobject for each object and read the data of each tag
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject location = jsonObject.getJSONObject("location");
                            String city = location.getString("name");
                            String region = location.getString("region");
                            String cityRegion= setCity(city,region);
                            tvCity.setText(cityRegion);

                            String country = location.getString("country");
                            setCountry(country);
                            String localTime = location.getString("localtime");
                            tvDay.setText(localTime);

                            JSONObject current = jsonObject.getJSONObject("current");

                            //get and display the temperature base on the setting option
                            if (storedFahrenheit) {
                                String temp_f = current.getString("temp_f");
                                temp_f = setTempFahrenheit(temp_f);
                                tvtTemp.setText(temp_f);
                            } else {
                                String temp_c = current.getString("temp_c");
                                temp_c = setTempCelsius(temp_c);
                                tvtTemp.setText(temp_c);

                            }

                            //Create and display the special data
                            String humidity = current.getString("humidity");
                            tvHumidity.setText(humidity);
                            String cloud = current.getString("cloud");
                            tvCloud.setText(String.format("%s%%", cloud));
                            String wind = current.getString("wind_mph");
                            tvWind.setText(String.format("%smph", wind));

                            JSONObject condition = current.getJSONObject("condition");
                            String conditionStatus = condition.getString("text");
                            tvStatus.setText(conditionStatus);

                            //Display the special weather gif icon based on the data
                            if (conditionStatus.contains("cloudy")) {
                                weatherView.setImageResource(R.drawable.weather_cloudy);
                            } else if (conditionStatus.contains("cold")) {
                                weatherView.setImageResource(R.drawable.weather_cold);
                            } else if (conditionStatus.contains("drizzle")) {
                                weatherView.setImageResource(R.drawable.weather_drizzle);
                            } else if (conditionStatus.contains("foggy")) {
                                weatherView.setImageResource(R.drawable.weather_foggy);
                            } else if (conditionStatus.contains("rainy")) {
                                weatherView.setImageResource(R.drawable.weather_rainy);
                            } else if (conditionStatus.contains("snowy")) {
                                weatherView.setImageResource(R.drawable.weather_snowy);
                            } else if (conditionStatus.contains("stormy")) {
                                weatherView.setImageResource(R.drawable.weather_stormy);
                            } else if (conditionStatus.contains("sunny")) {
                                weatherView.setImageResource(R.drawable.weather_sunny);
                            } else if (conditionStatus.contains("windy")) {
                                weatherView.setImageResource(R.drawable.weather_windy);
                            } else {
                                weatherView.setImageResource(R.drawable.weather_clouds);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }


    public String setCity(String city, String region) {
        String cityRegion;
        cityRegion= "City: "+city+","+region;
        return cityRegion;
    }

    public void setCountry(String country) {
        tvCountry.setText(String.format("Country: %s", country));
    }

    //Set temperature in Celcius
    public String setTempCelsius(String temperature) {
        Double a=Double.valueOf(temperature);
        String temp=String.valueOf(a.intValue());
        return (temp + " ℃");
    }
    //Set temperature in Fahrenheit
    public String setTempFahrenheit(String temperature) {
        Double a=Double.valueOf(temperature);
        String temp=String.valueOf(a.intValue());
        return (temp + " ℉");

    }

    // Find the target for each variable based on component's ids.
    private void Setup() {
        tvCity=(TextView) findViewById(R.id.textviewCity);
        tvCountry=(TextView) findViewById(R.id.textviewCountry);

        tvtTemp=(TextView) findViewById(R.id.textviewTemp);
        tvStatus=(TextView) findViewById(R.id.textviewStatus);
        tvHumidity=(TextView) findViewById(R.id.textviewHumidity);
        tvCloud=(TextView) findViewById(R.id.textviewCloud);
        tvWind=(TextView) findViewById(R.id.textviewWind);
        tvDay=(TextView) findViewById(R.id.textviewDay);

        weatherView = (ImageView) findViewById(R.id.imageIcon);

    }
}
