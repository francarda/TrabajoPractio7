package com.example.trabajopractio7;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class Dialogo extends BroadcastReceiver {
    private Intent intent=new Intent("EXIT_APP");


    public void mostrarDialogo(Activity activity){
        new AlertDialog.Builder(activity)
                .setTitle("Salir")
                .setMessage("¿Desea salir de la aplicación?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        onReceive(activity, intent);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("EXIT_APP")) {
            // Cierra la actividad actual
            context.sendBroadcast(intent);
            ((Activity) context).finish();

        }
    }

}
