package com.graction.developer.lumi.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.graction.developer.lumi.Data.DataStorage;
import com.graction.developer.lumi.DataBase.DataBaseHelper;
import com.graction.developer.lumi.DataBase.DataBaseStorage;
import com.graction.developer.lumi.Model.Xml.Weather;
import com.graction.developer.lumi.R;
import com.graction.developer.lumi.UI.UIFactory;
import com.graction.developer.lumi.Util.File.BaseActivityFileManager;
import com.graction.developer.lumi.Util.File.PreferenceManager;
import com.graction.developer.lumi.Util.GPS.GpsManager;
import com.graction.developer.lumi.Util.Log.HLogger;
import com.graction.developer.lumi.Util.Parser.XmlPullParserManager;
import com.graction.developer.lumi.Util.System.AlarmManager;

import java.util.HashMap;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.graction.developer.lumi.DataBase.DataBaseStorage.DATABASE_NAME;

public class Intro extends BaseActivity {
    private Context context;
    private Handler handler = new Handler();
    private int completeState = 2, runState, errorState;
    // initialize Thread
    private Thread iThread = new Thread(() -> dataInitialize())
            // Check Permission
            , pThread = new Thread(() -> permissionInitialize())
            // Check Thread
            , cThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (completeState == runState) {
                    startActivity(new Intent(context, MainActivity.class));
                    finish();
                    break;
                } else if (errorState == 1) {
                    break;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    logger.log(HLogger.LogType.ERROR, "cThread error", e);
                    errorState = 1;
                }
            }
            Looper.prepare();
            handler.post(() -> {
                if (errorState == 1)
                    Toast.makeText(Intro.this, "초기화에 문제가 발생하였습니다", Toast.LENGTH_SHORT).show();
            });
        }
    });

    @Override
    protected void onCreateMojo(Bundle savedInstanceState) {
        setContentView(R.layout.activity_intro);
    }

    @Override
    protected void init() {
        context = this;
        handler.postDelayed(() -> {
            pThread.start();
            iThread.start();
            cThread.start();
        }, 250);
    }

    private void dataInitialize() {
        XmlPullParserManager xmlPullParserManager = XmlPullParserManager.getInstance();
        xmlPullParserManager.setContext(context);

        UIFactory.init(this);
//            FontManager.getInstance().setAssetManager(getAssets());
        BaseActivityFileManager.getInstance().setActivity(this);
        ;
        PreferenceManager.setContext(getBaseContext());
        AlarmManager.getInstance().init(getApplicationContext());
        DataBaseStorage.alarmDataBaseHelper = new DataBaseHelper(getBaseContext(), DATABASE_NAME, null, DataBaseStorage.Version.TABLE_ALARM_VERSION);
        try {
            DataStorage.weathers = new HashMap<>();
            for (Weather weather : xmlPullParserManager.<Weather>xmlParser(Weather.class, R.xml.weathers))
                DataStorage.weathers.put(weather.getId(), weather);
        } catch (Exception e) {
            logger.log(HLogger.LogType.ERROR, "iThread error", e);
            errorState = 1;
            return;
        }
        runState++;
    }

    private void permissionInitialize() {
        GpsManager gpsManager = new GpsManager(this);
        String[] permissions = new String[]{ACCESS_FINE_LOCATION, INTERNET, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE};
        gpsManager.requestPermissions(permissions, DataStorage.Request.PERMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grant : grantResults)
            if (grant == -1)
                return;
        runState++;
    }
}
