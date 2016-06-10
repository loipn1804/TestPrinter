/**
 * TemplateTransfer for printing
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.testprinter.print.printprocess;

import android.content.Context;
import android.os.Message;

import com.brother.ptouch.sdk.PrinterInfo;
import com.brother.ptouch.sdk.PrinterStatus;
import com.testprinter.print.common.Common;
import com.testprinter.print.common.MsgDialog;
import com.testprinter.print.common.MsgHandle;

public class TemplateTransfer extends BasePrint {

    private String mPdzFile;

    public TemplateTransfer(Context context, MsgHandle mHandle,
                            MsgDialog mDialog) {
        super(context, mHandle, mDialog);
    }

    /**
     * Launch the thread to transfer
     */
    public void transfer() {
        mCancel = false;
        TransferThread transfer = new TransferThread();
        transfer.start();
    }

    // set the print data
    public void setFile(String file) {
        mPdzFile = file;
    }

    @Override
    protected void doPrint() {
    }

    /**
     * Thread for transferring
     */
    private class TransferThread extends Thread {
        @Override
        public void run() {

            // set info. for printing
            setPrinterInfo();

            // start message
            Message msg = mHandle.obtainMessage(Common.MSG_TRANSFER_START);
            mHandle.sendMessage(msg);
            mHandle.setFunction(MsgHandle.FUNC_TRANSFER);

            mPrintResult = new PrinterStatus();
            if (!mCancel) {
                mPrintResult = mPrinter.transfer(mPdzFile);
            } else {
                mPrintResult.errorCode = PrinterInfo.ErrorCode.ERROR_CANCEL;
            }
            // end message
            mHandle.setResult(showResult());
            mHandle.setBattery(getBattery());

            msg = mHandle.obtainMessage(Common.MSG_PRINT_END);
            mHandle.sendMessage(msg);
        }
    }
}