/**
 * Activity of Searching Network Printers
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.testprinter.print;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.brother.ptouch.sdk.NetPrinter;
import com.brother.ptouch.sdk.Printer;
import com.testprinter.R;
import com.testprinter.print.common.Common;
import com.testprinter.print.common.MsgDialog;

import java.util.ArrayList;

public class Activity_NetPrinterList extends ListActivity {

    public String modelName; // the print model name.
    private NetPrinter[] mNetPrinter; // array of storing Printer informations.
    private ArrayList<String> mItems = null; // List of storing the printer's
    // information
    private MsgDialog msgDialog = new MsgDialog(this);
    private SearchThread searchPrinter;

    /**
     * initialize activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // get the modelName
        final Bundle extras = getIntent().getExtras();
        modelName = extras.getString(Common.MODEL_NAME);
        setContentView(R.layout.activity_netprinterlist);

        // show searching dialog
        setDialog();

        // launch printer searching thread
        searchPrinter = new SearchThread();
        searchPrinter.start();

        this.setTitle(R.string.netPrinterListTitle_label);
    }

    /**
     * Called when [Settings] button is tapped
     */
    public void settingsButtonOnClick(View view) {
        Intent wifiSettings = new Intent(
                android.provider.Settings.ACTION_WIFI_SETTINGS);
        startActivityForResult(wifiSettings, Common.ACTION_WIFI_SETTINGS);
    }

    /**
     * Called when [Refresh] button is tapped
     */
    public void refreshButtonOnClick(View view) {
        setDialog();
        searchPrinter = new SearchThread();
        searchPrinter.start();
    }

    /**
     * Called when the Settings activity exits
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Common.ACTION_WIFI_SETTINGS) {
            setDialog();
            searchPrinter = new SearchThread();
            searchPrinter.start();
        }
    }

    /**
     * This method will be called when an item in the list is selected.
     *
     * @return
     */
    @Override
    protected void onListItemClick(ListView listView, View view, int position,
                                   long id) {

        final String item = (String) getListAdapter().getItem(position);
        if (!item.equalsIgnoreCase(getString(R.string.noNetDevice))) {
            // send the selected printer info. to Settings Activity and close
            // the current Activity
            final Intent settings = new Intent(this, Activity_Settings.class);
            settings.putExtra("ipAddress", mNetPrinter[position].ipAddress);
            settings.putExtra("macAddress", mNetPrinter[position].macAddress);
            settings.putExtra("printer", mNetPrinter[position].modelName);
            setResult(RESULT_OK, settings);
        }
        finish();
    }

    /**
     * search the net printer and adds the searched printer information into the
     * printerList
     */
    private boolean netPrinterList(int times) {

        boolean searchEnd = false;

        try {
            // clear the item list
            if (mItems != null) {
                mItems.clear();
            }

            // get net printers of the particular model
            mItems = new ArrayList<String>();
            Printer myPrinter = new Printer();
            mNetPrinter = myPrinter.getNetPrinters(modelName);
            final int netPrinterCount = mNetPrinter.length;

            // when find printers,set the printers' information to the list.
            if (netPrinterCount > 0) {
                searchEnd = true;

                String dispBuff[] = new String[netPrinterCount];
                for (int i = 0; i < netPrinterCount; i++) {
                    dispBuff[i] = mNetPrinter[i].modelName + "\n\n"
                            + mNetPrinter[i].ipAddress + "\n"
                            + mNetPrinter[i].macAddress + "\n"
                            + mNetPrinter[i].serNo + "\n"
                            + mNetPrinter[i].nodeName;
                    mItems.add(dispBuff[i]);
                }
            } else if (netPrinterCount == 0
                    && times == (Common.SEARCH_TIMES - 1)) { // when no printer
                // is found
                String dispBuff[] = new String[1];
                dispBuff[0] = getString(R.string.noNetDevice);
                mItems.add(dispBuff[0]);
                searchEnd = true;
            }

            if (searchEnd) {
                // list the result of searching for net printer
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ArrayAdapter<String> fileList = new ArrayAdapter<String>(
                                Activity_NetPrinterList.this,
                                android.R.layout.test_list_item, mItems);
                        Activity_NetPrinterList.this.setListAdapter(fileList);
                    }
                });
            }
        } catch (Exception e) {
        }

        return searchEnd;
    }

    /**
     * make a dialog, which shows the message during searching
     */
    public void setDialog() {
        msgDialog.showMsgNoButton(
                getString(R.string.netPrinterListTitle_label),
                getString(R.string.search_printer));
    }

    /**
     * printer searching thread
     */
    public class SearchThread extends Thread {

        /* search for the printer for 10 times until printer has been found. */
        @Override
        public void run() {

            for (int i = 0; i < Common.SEARCH_TIMES; i++) {
                // search for net printer.
                if (netPrinterList(i)) {
                    msgDialog.close();
                    break;
                }
            }
            msgDialog.close();
        }
    }
}