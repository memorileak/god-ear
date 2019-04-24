package com.tungnvan.godear.components;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tungnvan.godear.R;

public class RecordRenamer {

    private Context own_context;
    private AlertDialog.Builder dialog_builder;
    private AlertDialog dialog;
    private View record_rename_form;
    private EditText record_name_input;

    public RecordRenamer(Context context) {
        own_context = context;
        dialog_builder = new AlertDialog.Builder(own_context);
        record_rename_form = LayoutInflater.from(own_context).inflate(R.layout.rename_record_form, null);
        record_name_input = record_rename_form.findViewById(R.id.record_name);
        dialog = dialog_builder
            .setTitle(R.string.rename_record_dialog_title)
            .setView(record_rename_form)
            .setPositiveButton(R.string.rename_record_dialog_positive_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(own_context, record_name_input.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            })
            .create();
    }

    public void showDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

}
