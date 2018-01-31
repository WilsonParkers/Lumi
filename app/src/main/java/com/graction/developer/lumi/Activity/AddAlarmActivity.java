package com.graction.developer.lumi.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TimePicker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.graction.developer.lumi.Data.DataStorage;
import com.graction.developer.lumi.DataBase.DataBaseStorage;
import com.graction.developer.lumi.Model.Address.PostcodifyModel;
import com.graction.developer.lumi.Model.DataBase.AlarmTable;
import com.graction.developer.lumi.Model.Item.AlarmItem;
import com.graction.developer.lumi.R;
import com.graction.developer.lumi.UI.Layout.CustomArrayView;
import com.graction.developer.lumi.Util.Alarm.AlarmManager;
import com.graction.developer.lumi.Util.Log.HLogger;
import com.graction.developer.lumi.Util.StringUtil;
import com.graction.developer.lumi.databinding.ActivityAddAlarmBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class AddAlarmActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private ActivityAddAlarmBinding binding;

    private PlacePicker.IntentBuilder builder;
    private int hourOfDay, minute;
    private int[] selectedWeek = new int[8];
    private String memo, place_name, place_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_alarm);
    }

    @Override
    protected void init() {
        builder = new PlacePicker.IntentBuilder();

        createArray();
        binding.setActivity(this);
    }

    private void createArray() {
        ArrayList<CustomArrayView.CustomItemViewModel> items = new ArrayList<>();
        for (int i = 1; i < DataStorage.Date.DayOfTheWeek.length; i++)
            items.add(binding.activityAddAlarmCustomArrayView.new CustomItemViewModel(DataStorage.Date.DayOfTheWeek[i], i));
        binding.activityAddAlarmCustomArrayView.setWeekClickListener((idx, value) -> selectedWeek[idx] = value);
        binding.activityAddAlarmCustomArrayView.bindView(this, items);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // onTimeChange
    public void onTimeChanage(TimePicker view, int hourOfDay, int minute){
        logger.log(HLogger.LogType.INFO, "AlarmReceiver", "%d : %d", hourOfDay, minute);
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        // 8: 15, 17 : 15
    }

    // OnClick
    public void addAlarm(View view) {
        logger.log(HLogger.LogType.INFO, "addAlarm(view)");
        logger.log(HLogger.LogType.INFO, "addAlarm(view)", "selectedWeek : " + Arrays.toString(selectedWeek));
//        AlarmData.AlarmItem item = alarmData.new AlarmItem(place_name, place_address, memo, selectedWeek, hourOfDay, minute);
        AlarmTable alarmTable = new AlarmTable(hourOfDay, minute, place_name, place_address, memo, StringUtil.arrayToString(selectedWeek), 1);
        AlarmItem item = new AlarmItem(alarmTable);
        DataBaseStorage.alarmDataBaseHelper.insert(DataBaseStorage.Table.TABLE_ALARM, alarmTable);
        AlarmManager.getInstance().setAlarmService(item);
        DataBaseStorage.alarmList.add(item);
    }

    // OnClick
    public void searchEvent(View view){
        startActivityForResult(new Intent(this, SearchAddressActivity.class), DataStorage.Request.SEARCH_ADDRESS_REQUEST);
    }

    // OnClick
    public void backEvent(View view){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DataStorage.Request.SEARCH_ADDRESS_REQUEST && resultCode == DataStorage.Request.SEARCH_ADDRESS_OK){
            PostcodifyModel.ItemModel item = (PostcodifyModel.ItemModel) data.getSerializableExtra(DataStorage.Intent.KEY_ADDRESS_ITEM);
            binding.activityAddAlarmTVAddress.setText(item.getAddress());
        }

    }
}
