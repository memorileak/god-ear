package com.tungnvan.godear.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.tungnvan.godear.R;

public class ConfirmDialog {

    private Runnable on_granted;
    private Runnable on_denied;
    private AlertDialog.Builder dialog_builder;
    private AlertDialog dialog;

    public ConfirmDialog(Context context, String message) {
        dialog_builder = new AlertDialog.Builder(context);
        dialog = dialog_builder
            .setMessage(message)
            .setPositiveButton(R.string.confirm_dialog_positive_button, null)
            .setNegativeButton(R.string.confirm_dialog_negative_button, null)
            .create();
        dialog.setCanceledOnTouchOutside(false);
    }

    public ConfirmDialog setOnGranted(Runnable grantedHandler) {
        on_granted = grantedHandler;
        return this;
    }

    public ConfirmDialog setOnDenied(Runnable deniedHandler) {
        on_denied = deniedHandler;
        return this;
    }

    public void showDialog() {
        if (dialog != null) {
            dialog.show();
            dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (on_granted != null) on_granted.run();
                    dialog.dismiss();
                }
            });
            dialog.getButton(Dialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (on_denied != null) on_denied.run();
                    dialog.dismiss();
                }
            });
        }
    }

}
