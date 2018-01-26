package com.graction.developer.lumi.Data;


import com.graction.developer.lumi.Model.Response.IntegratedAirQualityModel;
import com.graction.developer.lumi.Model.Response.WeatherModel;
import com.graction.developer.lumi.Model.Xml.Weather;

import java.util.Map;

/**
 * Created by Hare on 2017-07-16.
 */

public class DataStorage {
//    public static double Location_Longitude, Location_Latitude;
//    public static FragmentEventListener S_FragmentEventListener;

    public static boolean GpsPermissionOn = false;
    public static WeatherModel weatherModel;
    public static IntegratedAirQualityModel integratedAirQualityModel;
    public static Map<Integer, Weather> weathers;

    public class OpenWeather {
        public static final String
                PARAM_KEY_APPID = "appid",
                PARAM_KEY_SEARCH = "q",
                PARAM_KEY_MDOE = "mode",
                PARAM_KEY_LAT = "lat",
                PARAM_KEY_LON = "lon";

        /*
        public static final String API_KEY = "5a165ea40110d89c6a33e762fb7501c6",
                API_URL = "http://api.openweathermap.org",
                RESOURCE_MODE = "json";
        */
    }

    public class Path{
        public static final String PATH_ASSET = "file:///android_asset/"
                                , PATH_XML = "xml/"
                                , PATH_IMAGES= "images/"
                                , PATH_BACKGROUND= PATH_IMAGES+"background/"
                                ;
    }

    public class Intent{
        public static final String KEY_WEEK = "week"
                                    , KEY_ALARM_ITEM= "alarm_item"
                                    ;
    }

    public class Request{
        public static final int PLACE_PICKER_REQUEST = 0x001

                                , RESULT_OK = 200

                                , GOOGLE_PLACE_OK = -1
                                , GOOGLE_PLACE_NONE_SELECTED = 0
                                ;
    }

    public class Action{
        public static final String RECEIVE_ACTION_ALARM = "com.graction.developer.lumi.ALARM_START"

                            ;
    }

    public class Preference{
        public static final String PREFERENCE_ALARM_DATA= "alarm_dataT"

                ;
    }

    public static class Date{
        public static final String[] DayOfTheWeek = {"","일", "월", "화", "수", "목", "금", "토"};

    }
}
