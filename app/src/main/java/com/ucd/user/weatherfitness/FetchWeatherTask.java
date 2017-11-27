package com.ucd.user.weatherfitness;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;


/**
 * Created by User on 11/18/2017.
 */

public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

    TextView scoreID;
    FetchWeatherTask(TextView scoreID){
        this.scoreID = scoreID;
    }

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    /* The date/time conversion code is going to be moved outside the asynctask later,
    * so for convenience we're breaking it out into its own method now.
    */

    private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);
        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }


    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_DAY = "day";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "main";
        final String OWM_WIND = "speed";
        final String OWM_PRESSURE = "pressure";
        final String OWM_HUMIDITY = "humidity";
        final String OWM_CITY = "city";
        final String OWM_NAME = "name";


        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.

        Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        // now we work exclusively in UTC
        dayTime = new Time();

        String[] results = new String[0];
        for (int i = 0; i < weatherArray.length(); i++) {
            String day;
            String description;
            double pressure;
            double humidity;
            double speed;


            // Get the JSON object representing the day object
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".
            long dateTime;
            // Cheating to convert this to UTC time, which is what we want anyhow

            dateTime = dayTime.setJulianDay(julianStartDay + i);
            day = getReadableDateString(dateTime);

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            //Added Location name object, so we can return it to user screen
            JSONObject cityObject = forecastJson.getJSONObject(OWM_CITY);
            String name = cityObject.getString(OWM_NAME);

            // Temperatures are in a child object called "temp".  Try not to name variables

            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double daytemp = temperatureObject.getDouble(OWM_DAY);

            pressure = dayForecast.getDouble(OWM_PRESSURE);
            humidity = dayForecast.getDouble(OWM_HUMIDITY);
            speed = dayForecast.getDouble(OWM_WIND);

            //calculating the score using our algorithm
            Score score = new Score(description, Math.round(daytemp), Math.round(humidity), Math.round(speed));
            int iscore = score.calculateScore();

            MainActivity.precipitation = description;
            MainActivity.pressure = pressure;
            MainActivity.wind = speed;
            MainActivity.temperature = daytemp;
            MainActivity.score = iscore;
            MainActivity.locationfromfetch = name;

            //added math.round to some weather dimensions
            results = new String[]{day, description, String.valueOf(Math.round(daytemp)), String.valueOf(Math.round(pressure)), String.valueOf(humidity), String.valueOf(Math.round(speed)), String.valueOf(iscore),name};
        }
        return results;
    }


    //This WEB REQUEST is based 16 day forecast API http://openweathermap.org/forecast16
    //If we want to provide forecast for each 3 hours, we might need to create another class

    @Override
    protected String[] doInBackground(String... params) {

        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        String format = "json";
        String units = "metric";
        int numDays = 1;

        // You will need to replace this key with your own one. To get a key
        // go to http://openweathermap.org/api and sign up.
        String key = "19b104f014c41d11939f615df3a80edf";

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            final String FORECAST_BASE_URL =
                    "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String QUERY_PARAM = "lat";
            final String QUERY_PARAM2 = "lon";
            final String FORMAT_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";
            final String KEY_PARAM = "APPID";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, params[0])
                    .appendQueryParameter(QUERY_PARAM2, params[1])
                    .appendQueryParameter(FORMAT_PARAM, format)
                    .appendQueryParameter(UNITS_PARAM, units)
                    .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                    .appendQueryParameter(KEY_PARAM, key)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            //raw url
            //URL url = new URL("http://http://api.openweathermap.org/data/2.5/forecast/daily?id=524901&mode=json&units=metric&ctn=7");

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            forecastJsonStr = readStream(inputStream);

            //Printing output JSON to LOG.d

            Log.v(LOG_TAG, "Forecast JSON String: " + forecastJsonStr);

        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            //Need to inform the user about failed connection to openweathermap.org
            return null;
        }

        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getWeatherDataFromJson(forecastJsonStr, numDays);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the JSON object.
        return null;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer data = new StringBuffer("");
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }

    @Override
    protected void onPostExecute(String[] results) {
        //super.onPostExecute(strings);

        if (results != null) {
            //TextView score_id = (TextView)findViewById(R.id.score_ID);
            String strDate = "Date: " + results[0];
            String strName = "Location name: " + results[7];
            String strDescription = "Precipitation: " + results[1];
            String strTemp = "Average temp: " + results[2];
            String strPressure = "Pressure: " + results[3];
            String strHumidity = "Humidity: " + results[4] + " %";
            String strWind = "Wind speed: " + results[5] + " m/sec";
            String strScore = "Score: " + results[6];

            //Forming an output which will show in TextView in main activity
            String allValues = strDate + System.lineSeparator() + strName+ System.lineSeparator() + strDescription + System.lineSeparator() + strTemp
                    + System.lineSeparator() + strPressure + System.lineSeparator() + strHumidity + System.lineSeparator() +
                    strWind + System.lineSeparator() + strScore;

            scoreID.setText(allValues);
            // mForecastAdapter.clear();
            //mForecastAdapter.addAll(result);
            //for (String dayForeCastStr: results){
                // mForecastAdapter.add(dayForeCastStr);
            //}
        }
    }

}