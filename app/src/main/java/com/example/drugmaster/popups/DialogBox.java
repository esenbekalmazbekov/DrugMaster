package com.example.drugmaster.popups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogBox extends AppCompatDialogFragment {
    private String title;
    private String message;

    private boolean mustDestroy;
    public DialogBox(String title, String message) {
        this.title = title;
        this.message = message;
        mustDestroy = true;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message);

        return builder.create();
    }

    @Override
    public void onDestroy() {
        if(mustDestroy)
            getActivity().onBackPressed();
        super.onDestroy();
    }

    public void setMustDestroy(boolean mustDestroy) {
        this.mustDestroy = mustDestroy;
    }
}
