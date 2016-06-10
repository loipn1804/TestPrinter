/**
 * Activity of printer settings
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.testprinter.print;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.testprinter.R;
import com.testprinter.print.common.Common;
import com.testprinter.print.printprocess.PrinterModelInfo;

import java.io.File;
import java.util.Arrays;

public class Activity_Settings extends PreferenceActivity implements
        OnPreferenceChangeListener {

    private SharedPreferences sharedPreferences;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String data = sharedPreferences.getString("port", "");

        // initialize the printerModel ListPreference
        ListPreference printerModelPreference = (ListPreference) getPreferenceScreen()
                .findPreference("printerModel");
        printerModelPreference.setEntryValues(PrinterModelInfo.getModelNames());
        printerModelPreference.setEntries(PrinterModelInfo.getModelNames());


        // initialize the settings
        setPreferenceValue("printerModel");
        String printerModel = sharedPreferences.getString("printerModel", "");

        // set paper size & port information
        printerModelChange(printerModel);

        setPreferenceValue("port");
        setEditValue("address");
        setEditValue("macAddress");
        setPreferenceValue("paperSize");
        setPreferenceValue("orientation");
        setEditValue("numberOfCopies");
        setPreferenceValue("halftone");
        setPreferenceValue("printMode");

        setPreferenceValue("mode9");
        setPreferenceValue("dashLine");

        setPreferenceValue("pjSpeed");
        setPreferenceValue("printerCase");
        setPreferenceValue("skipStatusCheck");
        setPreferenceValue("checkPrintEnd");

        setEditValue("imageThresholding");
        setEditValue("scaleValue");

        setPreferenceValue("pjCarbon");
        setPreferenceValue("pjDensity");
        setPreferenceValue("pjFeedMode");
        setPreferenceValue("align");
        setEditValue("leftMargin");
        setPreferenceValue("valign");

        setEditValue("topMargin");
        setEditValue("customPaperWidth");
        setEditValue("customPaperLength");
        setEditValue("customFeed");
        setPreferenceValue("customSetting");
        setPreferenceValue("paperPostion");
        // initialize the custom paper size's settings
        File newdir = new File(Common.CUSTOM_PAPER_FOLDER);
        if (!newdir.exists()) {
            newdir.mkdir();
        }
        File[] files = new File(Common.CUSTOM_PAPER_FOLDER).listFiles();
        String[] entries = new String[files.length];
        String[] entryValues = new String[files.length];
        int i = 0;
        for (File file : files) {
            String filename = file.getName();
            String extention = filename.substring(
                    filename.lastIndexOf(".", filename.length()) + 1,
                    filename.length());
            if (extention.equalsIgnoreCase("bin")) {
                entries[i] = filename;
                entryValues[i] = filename;
                i++;
            }
        }
        Arrays.sort(entries);
        Arrays.sort(entryValues);

        ListPreference customSettngPreference = (ListPreference) getPreferenceScreen()
                .findPreference("customSetting");
        customSettngPreference.setEntries(entries);
        customSettngPreference.setEntryValues(entryValues);

        setPreferenceValue("rjDensity");
        setPreferenceValue("dashLine");
        setPreferenceValue("rotate180");
        setPreferenceValue("peelMode");
        setPreferenceValue("autoCut");
        setPreferenceValue("endCut");
        setPreferenceValue("specialType");
        setPreferenceValue("halfCut");

        // initialization for printer
        PreferenceScreen printerPreference = (PreferenceScreen) getPreferenceScreen()
                .findPreference("printer");

        String printer = sharedPreferences.getString("printer", "");
        if (!printer.equals("")) {
            printerPreference.setSummary(printer);
        }

        printerPreference
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        String printerModel = sharedPreferences.getString(
                                "printerModel", "");
                        setPrinterList(printerModel);
                        return true;
                    }
                });


        // set the BackgroundForPreferenceScreens to light
        setBackgroundForPreferenceScreens("prefIpMacAddress");
        setBackgroundForPreferenceScreens("prefAlignmentSettings");
        setBackgroundForPreferenceScreens("prefPJSettings");
        setBackgroundForPreferenceScreens("prefPJTDSettings");
        setBackgroundForPreferenceScreens("prefCutSettings");

        setBackgroundForPreferenceScreens("halfToningSetting");
        setBackgroundForPreferenceScreens("scaleModelSetting");

    }

    /**
     * Called when a Preference has been changed by the user. This is called
     * before the state of the Preference is about to be updated and before the
     * state is persisted.
     */
    @SuppressWarnings("deprecation")
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (newValue != null) {
            if (preference.getKey().equals("printerModel")) {
                String printerModel = sharedPreferences.getString(
                        "printerModel", "");
                if (printerModel.equalsIgnoreCase(newValue.toString())) {
                    return true;
                }

                // initialize if printer model is changed
                printerModelChange(newValue.toString());
                ListPreference paperSizePreference = (ListPreference) getPreferenceScreen()
                        .findPreference("paperSize");
                paperSizePreference.setValue(paperSizePreference
                        .getEntryValues()[0].toString());
                paperSizePreference.setSummary(paperSizePreference
                        .getEntryValues()[0].toString());

                ListPreference portPreference = (ListPreference) getPreferenceScreen()
                        .findPreference("port");
                portPreference.setValue(portPreference.getEntryValues()[0]
                        .toString());
                portPreference.setSummary(portPreference.getEntryValues()[0]
                        .toString());

                setChangedData();
            }

            if (preference.getKey().equals("port")) {
                setChangedData();
            }

            preference.setSummary((CharSequence) newValue);

            return true;
        }

        return false;

    }

    /**
     * Called when the searching printers activity you launched exits.
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Common.PRINTER_SEARCH == requestCode) {
            EditTextPreference addressPreference = (EditTextPreference) getPreferenceScreen()
                    .findPreference("address");
            EditTextPreference macAddressPreference = (EditTextPreference) getPreferenceScreen()
                    .findPreference("macAddress");
            PreferenceScreen printerPreference = (PreferenceScreen) getPreferenceScreen()
                    .findPreference("printer");

            if (resultCode == RESULT_OK) {
                // IP address
                String ipAddress = data.getStringExtra("ipAddress");
                addressPreference.setText(ipAddress);
                if (ipAddress.equalsIgnoreCase("")) {
                    ipAddress = getString(R.string.address_value);
                }
                addressPreference.setSummary(ipAddress);

                // MAC address
                String macAddress = data.getStringExtra("macAddress");
                macAddressPreference.setText(macAddress);
                macAddressPreference.setSummary(macAddress);

                // Printer name
                printerPreference.setSummary(data.getStringExtra("printer"));

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("printer", data.getStringExtra("printer")
                        .toString());
                editor.commit();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * set data of a particular ListPreference
     */
    @SuppressWarnings("deprecation")
    private void setPreferenceValue(String value) {
        String data = sharedPreferences.getString(value, "");

        ListPreference printerValuePreference = (ListPreference) getPreferenceScreen()
                .findPreference(value);
        printerValuePreference.setOnPreferenceChangeListener(this);
        if (!data.equals("")) {
            printerValuePreference.setSummary(data);
        }
    }

    /**
     * set data of a particular EditTextPreference
     */
    @SuppressWarnings("deprecation")
    private void setEditValue(String value) {
        String name = sharedPreferences.getString(value, "");
        EditTextPreference printerValuePreference = (EditTextPreference) getPreferenceScreen()
                .findPreference(value);
        printerValuePreference.setOnPreferenceChangeListener(this);

        if (!name.equals("")) {
            printerValuePreference.setSummary(name);
        }
    }

    /**
     * Called when [printer] is tapped
     */
    private void setPrinterList(String printModel) {
        String port = sharedPreferences.getString("port", "");

        // call the Activity_NetPrinterList when port is NET
        if (port.equalsIgnoreCase("NET")) {
            Intent printerList = new Intent(this, Activity_NetPrinterList.class);
            String printTempModel = printModel.replaceAll("_", "-");
            printerList.putExtra("modelName", printTempModel);
            startActivityForResult(printerList, Common.PRINTER_SEARCH);
        } else // call the Activity_BluetoothPrinterList when port is Bluetooth
        {
            Intent printerList = new Intent(this,
                    Activity_BluetoothPrinterList.class);
            startActivityForResult(printerList, Common.PRINTER_SEARCH);
        }
    }


    /**
     * set paper size & port information with changing printer model
     */
    @SuppressWarnings("deprecation")
    private void printerModelChange(String printerModel) {

        // paper size
        ListPreference paperSizePreference = (ListPreference) getPreferenceScreen()
                .findPreference("paperSize");
        // port
        ListPreference portPreference = (ListPreference) getPreferenceScreen()
                .findPreference("port");
        if (!printerModel.equals("")) {

            String[] entryPort = null;
            String[] entryPaperSize = null;
            entryPort = PrinterModelInfo.getPortOrPaperSizeInfo(printerModel, Common.SETTINGS_PORT);
            entryPaperSize = PrinterModelInfo.getPortOrPaperSizeInfo(printerModel, Common.SETTINGS_PAPERSIZE);

            portPreference.setEntryValues(entryPort);
            portPreference.setEntries(entryPort);

            paperSizePreference.setEntryValues(entryPaperSize);
            paperSizePreference.setEntries(entryPaperSize);

        }
    }

    /**
     * initialize the address & macAddress information with changing printer
     * model or port
     */
    @SuppressWarnings("deprecation")
    private void setChangedData() {
        EditTextPreference addressPreference = (EditTextPreference) getPreferenceScreen()
                .findPreference("address");
        EditTextPreference macAddressPreference = (EditTextPreference) getPreferenceScreen()
                .findPreference("macAddress");
        PreferenceScreen printerPreference = (PreferenceScreen) getPreferenceScreen()
                .findPreference("printer");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("address", "");
        editor.putString("macAddress", "");
        editor.putString("printer", getString(R.string.printer_text));
        editor.commit();

        addressPreference.setText("");
        macAddressPreference.setText("");
        printerPreference.setSummary(getString(R.string.printer_text));
        macAddressPreference.setSummary(getString(R.string.macAddress_value));
        addressPreference.setSummary(getString(R.string.address_value));
    }

    /**
     * set the BackgroundForPreferenceScreens to light it is black when at OS
     * 2.1/2.2
     */
    @SuppressWarnings("deprecation")
    private void setBackgroundForPreferenceScreens(String key) {
        PreferenceScreen preferenceScreen = (PreferenceScreen) getPreferenceScreen()
                .findPreference(key);

        preferenceScreen
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        PreferenceScreen pref = (PreferenceScreen) preference;
                        pref.getDialog()
                                .getWindow()
                                .setBackgroundDrawableResource(
                                        android.R.drawable.screen_background_light);
                        return false;
                    }
                });
    }

    /**
     * A USB driver is acquired.
     *
     * @param activity
     * @return
     */
    @TargetApi(12)
    private boolean createUsbDevice(BaseActivity activity) {

        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        UsbDevice usbDevice = activity.getUsbDevice(usbManager);
        if (usbDevice == null) {
            return false;
        }
        PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(BaseActivity.ACTION_USB_PERMISSION), 0);
        usbManager.requestPermission(usbDevice, permissionIntent);
        return true;
    }
}
