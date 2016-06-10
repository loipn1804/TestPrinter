/**
 * Activity of printer settings
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.testprinter.print;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.brother.ptouch.sdk.PrinterInfo.PrinterSettingItem;
import com.testprinter.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Activity_BluetoothSettings extends BasePrinterSettingActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_cutome_layout);
        addPreferencesFromResource(R.xml.pj7_bluetooth_settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        updateValue();
        mList = Arrays.asList(PrinterSettingItem.BT_ISDISCOVERABLE,
                PrinterSettingItem.BT_DEVICENAME, PrinterSettingItem.BT_BOOTMODE);

    }

    private void updateValue() {

        setPreferenceValue("bt_isdiscoverable");
        // setPreferenceValue("bt_enable_pincode");
        setPreferenceValue("bt_bootmode");
        setEditValue("bt_devicename");
    }

    protected Map<PrinterSettingItem, String> createSettingsMap() {

        Map<PrinterSettingItem, String> settings = new HashMap<PrinterSettingItem, String>();
        settings.put(PrinterSettingItem.BT_ISDISCOVERABLE,
                sharedPreferences.getString("bt_isdiscoverable", ""));
        settings.put(PrinterSettingItem.BT_DEVICENAME,
                sharedPreferences.getString("bt_devicename", ""));

        settings.put(PrinterSettingItem.BT_BOOTMODE,
                sharedPreferences.getString("bt_bootmode", ""));

        return settings;

    }

    protected void saveSettings(Map<PrinterSettingItem, String> settings) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        for (PrinterSettingItem str : settings.keySet()) {
            switch (str) {
                case BT_ISDISCOVERABLE:
                    setPreferenceValue("bt_isdiscoverable", settings.get(str));
                    break;

                case BT_DEVICENAME:
                    setEditValue("bt_devicename", settings.get(str));
                    break;
                case BT_BOOTMODE:
                    setPreferenceValue("bt_bootmode", settings.get(str));
                    break;
                default:
                    break;
            }
        }
        editor.commit();
        updateValue();

    }

}
