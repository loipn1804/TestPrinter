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

public class Activity_RJ4PrinterSettings extends BasePrinterSettingActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_cutome_layout);
        addPreferencesFromResource(R.xml.rj4_printer_settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateValue();

        mList = Arrays.asList(
                PrinterSettingItem.PRINT_DENSITY,
                PrinterSettingItem.PRINT_JPEG_HALFTONE,
                PrinterSettingItem.PRINTER_PAWEROFFTIME);

    }

    private void updateValue() {

        setPreferenceValue("print_jpeg_halftone");
        setPreferenceValue("rj_print_density");
        setEditValue("printer_pawerofftime");
    }

    protected Map<PrinterSettingItem, String> createSettingsMap() {

        Map<PrinterSettingItem, String> settings = new HashMap<PrinterSettingItem, String>();

        settings.put(PrinterSettingItem.PRINTER_PAWEROFFTIME,
                sharedPreferences.getString("printer_pawerofftime", ""));
        settings.put(PrinterSettingItem.PRINT_JPEG_HALFTONE,
                sharedPreferences.getString("print_jpeg_halftone", ""));
        settings.put(PrinterSettingItem.PRINT_DENSITY,
                sharedPreferences.getString("rj_print_density", ""));
        return settings;

    }

    protected void saveSettings(Map<PrinterSettingItem, String> settings) {

        for (PrinterSettingItem str : settings.keySet()) {
            switch (str) {

                case PRINTER_PAWEROFFTIME:
                    setEditValue("printer_pawerofftime", settings.get(str));
                    break;
                case PRINT_JPEG_HALFTONE:
                    setPreferenceValue("print_jpeg_halftone", settings.get(str));
                    break;
                case PRINT_DENSITY:
                    setPreferenceValue("rj_print_density", settings.get(str));
                    break;
                default:
                    break;
            }
        }

    }

}
