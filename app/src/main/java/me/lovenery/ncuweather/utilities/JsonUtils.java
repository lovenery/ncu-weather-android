package me.lovenery.ncuweather.utilities;

/**
 * Created by Hsu on 2017/4/10.
 */

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();

    public static String[] getWeatherStringsFromJson(Context context, String jsonStr) throws JSONException {

        final int DATA_LENGTH = 3;
        final String DATA_TEMP = "temperature";
        final String DATA_PRECIP = "precip";

        JSONObject jsonObj = new JSONObject(jsonStr);

        String[] parsedWeatherData = null;
        parsedWeatherData = new String[DATA_LENGTH];
        parsedWeatherData[0] = "現在溫度：" + jsonObj.getString(DATA_TEMP) + " \u00b0C";
        parsedWeatherData[1] = "目前降雨：" + jsonObj.getString(DATA_PRECIP) + " mm/hr";


        long localDate = System.currentTimeMillis();
        String localDateStr = new SimpleDateFormat("kk:mm EEEE").format(localDate);
        parsedWeatherData[2] = "現在時間：" + localDateStr;

        for (int i = 0; i < parsedWeatherData.length; i++) {
            Log.v(TAG, "Weather String[" + i + "] is " + parsedWeatherData[i]);
        }

        return parsedWeatherData;
    }

}