package com.testprinter.print;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.brother.ptouch.sdk.PrinterInfo.ErrorCode;
import com.brother.ptouch.sdk.PrinterStatus;
import com.brother.ptouch.sdk.TemplateInfo;
import com.testprinter.R;
import com.testprinter.print.TempalateListAdapter.TempInfo;
import com.testprinter.print.common.MsgDialog;
import com.testprinter.print.common.MsgHandle;
import com.testprinter.print.printprocess.TemplateRemove;
import com.testprinter.print.printprocess.TemplateRemove.TemplateRemoveListener;

import java.util.ArrayList;
import java.util.List;

public class Activity_RemoveTemplate extends BaseActivity {

    private final Handler handler = new Handler();
    private TempalateListAdapter mListAdapter = null;
    private ListView mListView = null;
    private TextView mNodataTextView;
    private Activity mActivity;
    private TemplateRemoveListener listener = new TemplateRemoveListener() {

        @Override
        public void finish(final PrinterStatus status,
                           final List<TemplateInfo> tmplList) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    if (status.errorCode == ErrorCode.ERROR_NONE) {
                        if (tmplList != null && tmplList.size() > 0) {
                            setTeamplate(tmplList);
                        }

                    } else {
                        displayNoFile();
                    }
                }
            });
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_remove_template);
        mActivity = this;

        mDialog = new MsgDialog(this);
        mHandle = new MsgHandle(this, mDialog);
        myPrint = new TemplateRemove(this, mHandle, mDialog);
        BluetoothAdapter bluetoothAdapter = super.getBluetoothAdapter();
        myPrint.setBluetoothAdapter(bluetoothAdapter);

        mNodataTextView = (TextView) findViewById(R.id.text_no_data);
        mNodataTextView.setVisibility(View.VISIBLE);
        mListView = (ListView) this.findViewById(R.id.list_template_list);
        mListView.setVisibility(View.GONE);

    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Called when [Remove Templat] button is tapped
     *
     * @param view
     */
    public void removeTemplateButtonOnClick(View view) {
        if (!checkUSB())
            return;

        List<Integer> removeList = getRemoveList();

        ((TemplateRemove) myPrint).removeTemplate(removeList, listener);

    }

    /**
     * Called when [Get List] button is tapped
     *
     * @param view
     */
    public void getTemplateButtonOnClick(View view) {
        if (!checkUSB())
            return;

        ((TemplateRemove) myPrint).getTemplateList(listener);

    }

    /**
     * Set template list to ListView
     *
     * @param tmplList template list
     */
    private void setTeamplate(List<TemplateInfo> tmplList) {
        if (tmplList.size() == 0) {
            displayNoFile();
            return;
        } else {
            mNodataTextView.setVisibility(View.GONE);
            mListAdapter = new TempalateListAdapter(mActivity, tmplList);
            mListView.setAdapter(mListAdapter);
            mListView.setVisibility(View.VISIBLE);
            mListAdapter.notifyDataSetChanged();
        }

    }

    /**
     * Show that template does not exist
     */
    private void displayNoFile() {
        mNodataTextView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        mListAdapter = null;
        return;
    }

    /**
     * Get template list to be deleted
     *
     * @return
     */
    private List<Integer> getRemoveList() {
        if (mListAdapter == null) {
            return null;
        }
        List<TempInfo> list = mListAdapter.getTemplateList();
        List<Integer> enableList = new ArrayList<Integer>();
        for (TempInfo temp : list) {
            if (temp.getEnabled()) {
                enableList.add(temp.getTemplateInfo().key);
            }
        }
        return enableList;
    }

    @Override
    public void selectFileButtonOnClick(View view) {
        // TODO Auto-generated method stub

    }

    @Override
    public void printButtonOnClick(View view) {
        // TODO Auto-generated method stub

    }

}
