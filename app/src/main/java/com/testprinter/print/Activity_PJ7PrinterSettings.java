/**
 * Activity of printer settings
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.testprinter.print;

import android.os.Bundle;
import android.preference.PreferenceManager;

import com.brother.ptouch.sdk.PrinterInfo.PrinterSettingItem;
import com.testprinter.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Activity_PJ7PrinterSettings extends BasePrinterSettingActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_cutome_layout);
        addPreferencesFromResource(R.xml.pj7_printer_settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateValue();

        mList = Arrays.asList(PrinterSettingItem.PRINT_SPEED,
                PrinterSettingItem.PRINT_DENSITY,
                PrinterSettingItem.PRINT_JPEG_SCALE,
                PrinterSettingItem.PRINT_JPEG_HALFTONE,
                PrinterSettingItem.PRINTER_PAWEROFFTIME_BATTERY,
                PrinterSettingItem.PRINTER_PAWEROFFTIME);

    }

    private void updateValue() {

        setPreferenceValue("print_jpeg_halftone");
        setPreferenceValue("print_density");
        setPreferenceValue("print_jpeg_scale");
        setPreferenceValue("print_speed");
        setEditValue("printer_pawerofftime_battery");
        setEditValue("printer_pawerofftime");
    }

    protected Map<PrinterSettingItem, String> createSettingsMap() {

        Map<PrinterSettingItem, String> settings = new HashMap<PrinterSettingItem, String>();

        settings.put(PrinterSettingItem.PRINTER_PAWEROFFTIME,
                sharedPreferences.getString("printer_pawerofftime", ""));
        settings.put(PrinterSettingItem.PRINTER_PAWEROFFTIME_BATTERY,
                sharedPreferences.getString("printer_pawerofftime_battery", ""));
        settings.put(PrinterSettingItem.PRINT_JPEG_HALFTONE,
                sharedPreferences.getString("print_jpeg_halftone", ""));
        settings.put(PrinterSettingItem.PRINT_JPEG_SCALE,
                sharedPreferences.getString("print_jpeg_scale", ""));
        settings.put(PrinterSettingItem.PRINT_DENSITY,
                sharedPreferences.getString("print_density", ""));
        settings.put(PrinterSettingItem.PRINT_SPEED,
                sharedPreferences.getString("print_speed", ""));
        return settings;

    }

    protected void saveSettings(Map<PrinterSettingItem, String> settings) {

        for (PrinterSettingItem str : settings.keySet()) {
            switch (str) {

                case PRINTER_PAWEROFFTIME:
                    setEditValue("printer_pawerofftime", settings.get(str));
                    break;
                case PRINTER_PAWEROFFTIME_BATTERY:
                    setEditValue("printer_pawerofftime_battery", settings.get(str));
                    break;
                case PRINT_JPEG_HALFTONE:
                    setPreferenceValue("print_jpeg_halftone", settings.get(str));
                    break;
                case PRINT_JPEG_SCALE:
                    setPreferenceValue("print_jpeg_scale", settings.get(str));
                    break;

                case PRINT_DENSITY:
                    setPreferenceValue("print_density", settings.get(str));
                    break;
                case PRINT_SPEED:
                    setPreferenceValue("print_speed", settings.get(str));
                    break;

                default:
                    break;
            }
        }

    }

}
