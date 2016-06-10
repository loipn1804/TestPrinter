/**
 * Base Activity for printing
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.testprinter.print;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.testprinter.R;

public class Activity_PrinterPreference extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_preference);

    }

    /**
     * Called when [Printer Settings] button is tapped
     */
    public void MWBluetoothPreferenceButtonOnClick(View view) {
        startActivity(new Intent(this,
                Activity_MWBluetoothPrinterPreference.class));
    }

    /**
     * Called when [Printer Settings] button is tapped
     */
    public void BluetoothPreferenceButtonOnClick(View view) {
        startActivity(new Intent(this, Activity_BluetoothSettings.class));
    }

    /**
     * Called when [Printer Settings] button is tapped
     */
    public void PJ7PrinterPreferenceButtonOnClick(View view) {
        startActivity(new Intent(this, Activity_PJ7PrinterSettings.class));
    }

    /**
     * Called when [Printer Settings] button is tapped
     */
    public void NetPreferenceButtonOnClick(View view) {
        startActivity(new Intent(this, Activity_NetSettings.class));
    }

    /**
     * Called when [Printer Settings] button is tapped
     */
    public void RJ4PrinterPreferenceButtonOnClick(View view) {
        startActivity(new Intent(this, Activity_RJ4PrinterSettings.class));
    }

    /**
     * Called when [Printer Settings] button is tapped
     */
    public void printerSettingsButtonOnClick(View view) {
        startActivity(new Intent(this, Activity_Settings.class));
    }

}