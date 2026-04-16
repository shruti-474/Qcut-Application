package com.mountreachsolution.qcut.common;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.mountreachsolution.qcut.R;


public class NetworkChangeListner extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!NetworkDetails.isconnectedtointernet(context)){
            AlertDialog.Builder ad = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.checkinternet_connection,null);
            ad.setView(layout_dialog);

            AppCompatButton btnretry = layout_dialog.findViewById(R.id.btncheckinternetRetry);

            AlertDialog alertDialog = ad.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);

            btnretry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    onReceive(context,intent);
                }
            });


        }
        else{

        }

    }
}
