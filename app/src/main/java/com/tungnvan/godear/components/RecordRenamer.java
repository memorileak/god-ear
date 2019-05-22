package com.tungnvan.godear.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.tungnvan.godear.R;
import com.tungnvan.godear.utils.FileUtils;
import com.tungnvan.godear.utils.RecordNameUtils;

public class RecordRenamer {

    protected EditText record_name_input;
    protected String old_file_path;
    protected View record_rename_form;
    protected Context own_context;
    protected AlertDialog.Builder dialog_builder;
    protected AlertDialog dialog;

    protected boolean renameRecord() {
        try {
            String new_file_path = RecordNameUtils.produceFilePathFromName(record_name_input.getText().toString());
            if (RecordNameUtils.produceFileNameFromPath(new_file_path).compareTo("") == 0) {
                Toast.makeText(own_context, "Please enter a name!", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if (old_file_path.compareTo(new_file_path) == 0 || FileUtils.renameFile(old_file_path, new_file_path)) {
                    Toast.makeText(own_context, "Record file has successfully saved!", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Toast.makeText(own_context, "An error occured! Record file could not be saved.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(own_context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    protected boolean deleteRecord() {
        try {
            if (FileUtils.deleteFile(old_file_path)) {
                Toast.makeText(own_context, "Record file has been discarded!", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(own_context, "An error occured!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(own_context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    protected void showKeyboard() {
        InputMethodManager imm = (InputMethodManager)own_context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)own_context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public RecordRenamer(Context context, final String file_path) {
        own_context = context;
        old_file_path = file_path;
        dialog_builder = new AlertDialog.Builder(own_context);
        record_rename_form = LayoutInflater.from(own_context).inflate(R.layout.rename_record_form, null);
        record_name_input = record_rename_form.findViewById(R.id.record_name);
        record_name_input.setText(RecordNameUtils.produceFileNameFromPath(old_file_path));
        record_name_input.setSelectAllOnFocus(true);
        dialog = dialog_builder
            .setTitle(R.string.rename_record_dialog_title)
            .setView(record_rename_form)
            .setPositiveButton(R.string.rename_record_dialog_positive_button, null)
            .setNegativeButton(R.string.rename_record_dialog_negative_button, null)
            .create();
        dialog.setCanceledOnTouchOutside(false);
    }

    public void showDialog() {
        if (dialog != null) {
            dialog.show();
            showKeyboard();
            dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (renameRecord()) {
                        dialog.dismiss();
                        hideKeyboard();
                    }
                }
            });
            dialog.getButton(Dialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteRecord()) {
                        dialog.dismiss();
                        hideKeyboard();
                    }
                }
            });
        }
    }

}
