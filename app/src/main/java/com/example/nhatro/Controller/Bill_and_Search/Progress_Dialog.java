package com.example.nhatro.Controller.Bill_and_Search;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.example.nhatro.R;

public class Progress_Dialog {
    Context context;
    Dialog dialog;
    public Progress_Dialog(Context context) {
        this.context = context;
    }
    public void ShowDilag(String title){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textview_progress_dialog = dialog.findViewById(R.id.textview_progress_dialog);
        textview_progress_dialog.setText(title);
        dialog.create();
        dialog.show();
    }
    public void HideDialog(){
        dialog.dismiss();
    }
}
