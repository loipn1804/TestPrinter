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

public class Activity_ManageTemplate extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_template);

    }

    /**
     * Called when [Printer Settings] button is tapped
     */
    public void removaTemplateButtonOnClick(View view) {
        startActivity(new Intent(this, Activity_RemoveTemplate.class));
    }

    /**
     * Called when [Printer Settings] button is tapped
     */
    public void transferTemplateButtonOnClick(View view) {
        startActivity(new Intent(this, Activity_TransferPdz.class));
    }

}