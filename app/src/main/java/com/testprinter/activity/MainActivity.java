package com.testprinter.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.testprinter.R;
import com.testprinter.print.Activity_PrintImage;
import com.testprinter.print.Activity_PrinterPreference;
import com.testprinter.print.common.Common;
import com.testprinter.print.printprocess.PrinterModelInfo;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String NAME_PATH = "TestPrinter";

    private Button btnPrinter;
    private Button btnSetting;
    private RelativeLayout rltText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        btnPrinter = (Button) findViewById(R.id.btnPrinter);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        rltText = (RelativeLayout) findViewById(R.id.rltText);

        btnPrinter.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
    }

    private void initData() {
        setPrefereces();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrinter:
//                Intent intent = new Intent(this, Activity_PrintImage.class);
//                startActivity(intent);
                SaveImageAsync saveImageAsync = new SaveImageAsync();
                saveImageAsync.execute();
                break;
            case R.id.btnSetting:
                Intent intentSetting = new Intent(this, Activity_PrinterPreference.class);
                startActivity(intentSetting);
                break;
        }
    }

    private class SaveImageAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(Void... params) {
            Bitmap bitmap = convertViewToBitmap();
            String filename = null;
            filename = saveBitmapToSDCard(bitmap);
            return filename;
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressDialog();
            if (result != null) {
                showToast("Saved");
                Intent intent = new Intent(MainActivity.this, Activity_PrintImage.class);
                intent.putExtra(Common.INTENT_FILE_NAME, result);
                startActivity(intent);
            } else {
                showToast("Save fail");
            }
        }
    }

    private Bitmap convertViewToBitmap() {
        rltText.setDrawingCacheEnabled(false);
        rltText.destroyDrawingCache();
        rltText.buildDrawingCache();
        return rltText.getDrawingCache();
    }

    private String saveBitmapToSDCard(Bitmap bitmap) {
        String imageName = NAME_PATH + "_" + System.currentTimeMillis();
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + NAME_PATH);

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        String fname = imageName + ".png";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            ContentValues image = new ContentValues();
            image.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, image);
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

        }
    }

    private void setPrefereces() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        // initialization for print
        PrinterInfo printerInfo = new PrinterInfo();
        Printer printer = new Printer();
        printerInfo = printer.getPrinterInfo();
        if (printerInfo == null) {
            printerInfo = new PrinterInfo();
            printer.setPrinterInfo(printerInfo);

        }
        if (sharedPreferences.getString("printerModel", "").equals("")) {
            String printerModel = printerInfo.printerModel.toString();
            PrinterModelInfo.Model model = PrinterModelInfo.Model.valueOf(printerModel);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("printerModel", printerModel);
            editor.putString("port", printerInfo.port.toString());
            editor.putString("address", printerInfo.ipAddress.toString());
            editor.putString("macAddress", printerInfo.macAddress.toString());

            // Override SDK default paper size
            editor.putString("paperSize", model.getDefaultPaperSize());

            editor.putString("orientation", printerInfo.orientation.toString());
            editor.putString("numberOfCopies",
                    Integer.toString(printerInfo.numberOfCopies));
            editor.putString("halftone", printerInfo.halftone.toString());
            editor.putString("printMode", printerInfo.printMode.toString());
            editor.putString("pjCarbon", Boolean.toString(printerInfo.pjCarbon));
            editor.putString("pjDensity",
                    Integer.toString(printerInfo.pjDensity));
            editor.putString("pjFeedMode", printerInfo.pjFeedMode.toString());
            editor.putString("align", printerInfo.align.toString());
            editor.putString("leftMargin",
                    Integer.toString(printerInfo.margin.left));
            editor.putString("valign", printerInfo.valign.toString());
            editor.putString("topMargin",
                    Integer.toString(printerInfo.margin.top));
            editor.putString("customPaperWidth",
                    Integer.toString(printerInfo.customPaperWidth));
            editor.putString("customPaperLength",
                    Integer.toString(printerInfo.customPaperLength));
            editor.putString("customFeed",
                    Integer.toString(printerInfo.customFeed));
            editor.putString("paperPostion",
                    printerInfo.paperPosition.toString());
            editor.putString("customSetting",
                    sharedPreferences.getString("customSetting", ""));
            editor.putString("rjDensity",
                    Integer.toString(printerInfo.rjDensity));
            editor.putString("rotate180",
                    Boolean.toString(printerInfo.rotate180));
            editor.putString("dashLine", Boolean.toString(printerInfo.dashLine));
            editor.putString("peelMode", Boolean.toString(printerInfo.peelMode));
            editor.putString("mode9", Boolean.toString(printerInfo.mode9));
            editor.putString("pjSpeed", Integer.toString(printerInfo.pjSpeed));

            editor.putString("printerCase",
                    printerInfo.rollPrinterCase.toString());
            editor.putString("skipStatusCheck",
                    Boolean.toString(printerInfo.skipStatusCheck));
            editor.putString("checkPrintEnd", printerInfo.checkPrintEnd.toString());

            editor.putString("imageThresholding",
                    Integer.toString(printerInfo.thresholdingValue));
            editor.putString("scaleValue",
                    Double.toString(printerInfo.scaleValue));

            editor.commit();
        }
    }

    private Toast toast;

    private void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }

    private ProgressDialog progress_dialog;

    public void showProgressDialog() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(this);
        }

        if (!progress_dialog.isShowing()) {
            progress_dialog.setMessage("Progressing ... ");
            progress_dialog.setCancelable(false);
            progress_dialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progress_dialog != null && progress_dialog.isShowing()) {
            progress_dialog.dismiss();
        }
    }
}
