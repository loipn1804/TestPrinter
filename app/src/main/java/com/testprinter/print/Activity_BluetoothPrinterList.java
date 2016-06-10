/**
 * Activity of Searching Bluetooth Printers
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.testprinter.print;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.brother.ptouch.sdk.NetPrinter;
import com.testprinter.R;
import com.testprinter.print.common.Common;

import java.util.ArrayList;
import java.util.Set;

public class Activity_BluetoothPrinterList extends ListActivity {

    private NetPrinter[] mBluetoothPrinter; // array of storing Printer
    // informations.
    private ArrayList<String> mItems = null; // List of storing the printer's
    // information

    /**
     * initialize activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netprinterlist);

        getPairedPrinters();
        this.setTitle(R.string.bluetoothPrinterListTitle_label);
    }

    /**
     * Called when [Settings] button is tapped
     */
    public void settingsButtonOnClick(View view) {
        Intent bluetoothSettings = new Intent(
                android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivityForResult(bluetoothSettings,
                Common.ACTION_BLUETOOTH_SETTINGS);
    }

    /**
     * Called when [Refresh] button is tapped
     */
    public void refreshButtonOnClick(View view) {
        getPairedPrinters();

    }

    /**
     * Called when the Settings activity exits
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Common.ACTION_BLUETOOTH_SETTINGS) {
            getPairedPrinters();
        }
    }

    /**
     * get paired printers
     */
    public void getPairedPrinters() {
        // get the BluetoothAdapter
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled() == false) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(enableBtIntent);
            }
        }

        try {
            if (mItems != null) {
                mItems.clear();
            }
            mItems = new ArrayList<String>();

			/*
             * if the paired devices exist, set the paired devices else set the
			 * string of "No Bluetooth Printer."
			 */
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0) {

                mBluetoothPrinter = new NetPrinter[pairedDevices.size()];
                int i = 0;
                for (BluetoothDevice device : pairedDevices) {
                    String strDev = "";
                    strDev += device.getName() + "\n" + device.getAddress();
                    mItems.add(strDev);

                    mBluetoothPrinter[i] = new NetPrinter();
                    mBluetoothPrinter[i].ipAddress = "";
                    mBluetoothPrinter[i].macAddress = device.getAddress();
                    mBluetoothPrinter[i].modelName = device.getName();
                    i++;
                }
            } else {
                mItems.add(getString(R.string.noBluetoothDevice));
            }
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter<String> fileList = new ArrayAdapter<String>(
                            Activity_BluetoothPrinterList.this,
                            android.R.layout.test_list_item, mItems);
                    Activity_BluetoothPrinterList.this.setListAdapter(fileList);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * Called when an item in the list is tapped
     */
    @Override
    protected void onListItemClick(ListView listView, View view, int position,
                                   long id) {

        final String item = (String) getListAdapter().getItem(position);
        if (!item.equalsIgnoreCase(getString(R.string.noBluetoothDevice))) {
            // send the selected printer info. to Settings Activity and close
            // the current Activity
            final Intent settings = new Intent(this, Activity_Settings.class);
            settings.putExtra("ipAddress",
                    mBluetoothPrinter[position].ipAddress);
            settings.putExtra("macAddress",
                    mBluetoothPrinter[position].macAddress);
            settings.putExtra("printer", mBluetoothPrinter[position].modelName);
            setResult(RESULT_OK, settings);
        }
        finish();
    }

}