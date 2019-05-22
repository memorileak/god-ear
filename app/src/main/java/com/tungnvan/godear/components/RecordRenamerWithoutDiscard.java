package com.tungnvan.godear.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.tungnvan.godear.R;
import com.tungnvan.godear.utils.FileUtils;
import com.tungnvan.godear.utils.RecordNameUtils;

public class RecordRenamerWithoutDiscard extends RecordRenamer {

    private Runnable on_renamed;

    RecordRenamerWithoutDiscard(Context context, final String file_path) {
        super(context, file_path);
        dialog_builder = new AlertDialog.Builder(own_context);
        dialog = dialog_builder
                .setTitle(R.string.rename_record_dialog_title)
                .setView(record_rename_form)
                .setPositiveButton(R.string.record_rename_without_discard_dialog_positive_button, null)
                .setNegativeButton(R.string.record_rename_without_discard_dialog_negative_button, null)
                .create();
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected boolean renameRecord() {
        try {
            String new_file_path = RecordNameUtils.produceFilePathFromName(record_name_input.getText().toString());
            if (RecordNameUtils.produceFileNameFromPath(new_file_path).compareTo("") == 0) {
                Toast.makeText(own_context, "Please enter a name!", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if (old_file_path.compareTo(new_file_path) == 0 || FileUtils.renameFile(old_file_path, new_file_path)) {
                    Toast.makeText(own_context, "Record file has successfully renamed!", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Toast.makeText(own_context, "An error occured!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(own_context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected boolean deleteRecord() {return true;}

    @Override
    public void showDialog() {
        if (dialog != null) {
            dialog.show();
            showKeyboard();
            dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (renameRecord()) {
                        if (on_renamed != null) on_renamed.run();
                        dialog.dismiss();
                        hideKeyboard();
                    }
                }
            });
            dialog.getButton(Dialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    hideKeyboard();
                }
            });
        }
    }

    public RecordRenamerWithoutDiscard setOnRenamed(Runnable renamedHandler) {
        on_renamed = renamedHandler;
        return this;
    }
}
