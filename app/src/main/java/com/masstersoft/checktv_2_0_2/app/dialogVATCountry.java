package com.masstersoft.checktv_2_0_2.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;

/**
 * Created by Andrey on 23.03.2015.
 */
public class dialogVATCountry extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<String> ar = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            ar.add("Country #" + (i + 1));
        }
        CharSequence[] cs = ar.toArray(new CharSequence[ar.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("VAT BY COUNTRY")
                .setItems(cs, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        return builder.create();
    }
}
