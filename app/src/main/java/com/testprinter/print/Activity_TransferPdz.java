/**
 * Activity of transferring the pdz files into printers
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.testprinter.print;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.testprinter.R;
import com.testprinter.print.common.Common;
import com.testprinter.print.common.MsgDialog;
import com.testprinter.print.common.MsgHandle;
import com.testprinter.print.printprocess.TemplateTransfer;

public class Activity_TransferPdz extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_pdz);

        // initialization for printing
        mDialog = new MsgDialog(this);
        mHandle = new MsgHandle(this, mDialog);
        myPrint = new TemplateTransfer(this, mHandle, mDialog);

        // when use bluetooth print set the adapter
        BluetoothAdapter bluetoothAdapter = super.getBluetoothAdapter();
        myPrint.setBluetoothAdapter(bluetoothAdapter);

        ((Button) findViewById(R.id.btnTransfer)).setEnabled(false);
        // get data from other application by way of intent sending
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String file = extras.getString(Common.INTENT_FILE_NAME);

            setPdzFile(file);
        }
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional data
     * from it.
     */
    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // get pdz File and set the new data to display
        if (resultCode == RESULT_OK && requestCode == Common.FILE_SELECT_PDZ) {
            final String strRtn = data.getStringExtra(Common.INTENT_FILE_NAME);
            setPdzFile(strRtn);
        }
    }

    /**
     * set the pdz file
     */
    void setPdzFile(String file) {
        if (Common.isTemplateFile(file)) {
            TextView txt = (TextView) findViewById(R.id.tvSelectedPdz);
            txt.setText(file);
            ((TemplateTransfer) myPrint).setFile(file);

            ((Button) findViewById(R.id.btnTransfer)).setEnabled(true);
        }
    }

    /**
     * Called when [select] button is tapped
     */
    @Override
    public void selectFileButtonOnClick(View view) {

        // call File Explorer Activity to select a pdz file
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        final String pdzPath = prefs.getString(Common.PREFES_PDZ_PATH, "");
        final Intent fileList = new Intent(Activity_TransferPdz.this,
                Activity_FileList.class);
        fileList.putExtra(Common.INTENT_TYPE_FLAG, Common.FILE_SELECT_PDZ);
        fileList.putExtra(Common.INTENT_FILE_NAME, pdzPath);
        startActivityForResult(fileList, Common.FILE_SELECT_PDZ);

    }

    /**
     * Called when [Transfer] button is tapped
     */
    public void transferButtonOnClick(View view) {
        if (!checkUSB())
            return;
        ((TemplateTransfer) myPrint).transfer();

    }

    @Override
    public void printButtonOnClick(View view) {
    }
}
