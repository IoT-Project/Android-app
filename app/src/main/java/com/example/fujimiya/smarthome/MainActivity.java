package com.example.fujimiya.smarthome;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ImageView kipas, lampu, gas, telur;
    TextView suhu;
    String Skipas,lampu1,lampu2,lampu3,lampu4, Sgas, Stelur, Ssuhu;
    Firebase refD, refS;
    ImageView tel1,tel2,tel3,tel4,tel5,tel6,tel7,tel8,tel9,tel10,Ldapur,Lteras,Ltamu,Lkeluarga;
    DialogInterface.OnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Firebase.setAndroidContext(this);
            refD = new Firebase("https://iot-ict.firebaseio.com/digital");
            refS = new Firebase("https://iot-ict.firebaseio.com/sensor");

            kipas = (ImageView) findViewById(R.id.kipas);
            lampu = (ImageView) findViewById(R.id.lampu);
            gas = (ImageView) findViewById(R.id.gas);
            telur = (ImageView) findViewById(R.id.telur);
            suhu = (TextView) findViewById(R.id.suhu);


            refD.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lampu1 = dataSnapshot.child("data_0").getValue().toString();
                    lampu2 = dataSnapshot.child("data_1").getValue().toString();
                    lampu3 = dataSnapshot.child("data_2").getValue().toString();
                    lampu4 = dataSnapshot.child("data_3").getValue().toString();

                    if (dataSnapshot.child("data_0").getValue().toString().equals("on0") || dataSnapshot.child("data_1").getValue().toString().equals("on1") || dataSnapshot.child("data_2").getValue().toString().equals("on2") || dataSnapshot.child("data_3").getValue().toString().equals("on3")) {
                        lampu.setImageResource(R.drawable.lamp_luar_on);

                    }
                    if (dataSnapshot.child("data_0").getValue().toString().equals("off0") && dataSnapshot.child("data_1").getValue().toString().equals("off1") && dataSnapshot.child("data_2").getValue().toString().equals("off2") && dataSnapshot.child("data_3").getValue().toString().equals("off3")) {
                        lampu.setImageResource(R.drawable.lamp_luar_off);
                    }

                    // titik e

                    if (dataSnapshot.child("data_4").getValue().toString().equals("on4")) {
                        kipas.setImageResource(R.drawable.kipas_blue);
                        Skipas = "Hidup";
                    }
                    if (dataSnapshot.child("data_4").getValue().toString().equals("off4")) {
                        kipas.setImageResource(R.drawable.kipas_off);
                        Skipas = "Mati";
                    }


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            refS.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //Toast.makeText(getApplication().getBaseContext(), "sensor", Toast.LENGTH_LONG).show();
                    if (dataSnapshot.child("sen_0").getValue().toString().equals("Gas bocor")) {
                        notifgas();
                        gas.setImageResource(R.drawable.gas_on);
                    }
                    if (dataSnapshot.child("sen_0").getValue().toString().equals("Gas aman")) {
                        gas.setImageResource(R.drawable.gas_off);

                    }

                    suhu.setText(dataSnapshot.child("sen_1").getValue().toString() + " °C");

                    Sgas = dataSnapshot.child("sen_0").getValue().toString();
                    Ssuhu = dataSnapshot.child("sen_1").getValue().toString();
                    Stelur = dataSnapshot.child("sen_2").getValue().toString();


                    if (Double.parseDouble(Ssuhu) > 30.00){
                        notifsuhu();
                        //Toast.makeText(getApplication(),"Suhu panas",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        } catch (Exception e) {

            Toast.makeText(this,""+e,Toast.LENGTH_LONG).show();
        }


    }

    public void kipas(View view) {
        //Toast.makeText(this,"Kipas",Toast.LENGTH_LONG).show();
        if (Skipas == "Hidup") {
            refD.child("data_4").setValue("off4");
        } else if (Skipas == "Mati") {
            refD.child("data_4").setValue("on4");
        }
    }

    public void lampu(View view) {

        try
        {
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.lampudialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
            alertDialogBuilder.setView(v);
            AlertDialog alert = alertDialogBuilder.create();


            Lteras = (ImageView) v.findViewById(R.id.lam4);
            Ldapur = (ImageView) v.findViewById(R.id.lam3);
            Lkeluarga = (ImageView) v.findViewById(R.id.lam2);
            Ltamu = (ImageView) v.findViewById(R.id.lam1);

            if(lampu1.equals("on0")){
                Ltamu.setImageResource(R.drawable.lamp_on);
            }
            if(lampu1.equals("off0")){
                Ltamu.setImageResource(R.drawable.lamp_off);
            }

            if(lampu2.equals("on1")){
                Lkeluarga.setImageResource(R.drawable.lamp_on);
            }
            if(lampu2.equals("off1")){
                Lkeluarga.setImageResource(R.drawable.lamp_off);
            }

            if(lampu3.equals("on2")){
                Ldapur.setImageResource(R.drawable.lamp_on);
            }
            if(lampu3.equals("off2")){
                Ldapur.setImageResource(R.drawable.lamp_off);
            }
            if(lampu4.equals("on3")){
                Lteras.setImageResource(R.drawable.lamp_on);
            }
            if(lampu4.equals("off3")){
                Lteras.setImageResource(R.drawable.lamp_off);
            }
            alert.setTitle("CONTROLLING LAMPU RUMAH");
            alert.show();

        }catch (Exception e){

        }

        //Toast.makeText(this, "Lampu", Toast.LENGTH_LONG).show();
    }

    public void stok(View view) {
        try
        {
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.telurdialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
            alertDialogBuilder.setView(v);
            AlertDialog alert = alertDialogBuilder.create();

            tel1 = (ImageView) v.findViewById(R.id.tel1);
            tel2 = (ImageView) v.findViewById(R.id.tel2);
            tel3 = (ImageView) v.findViewById(R.id.tel3);
            tel4 = (ImageView) v.findViewById(R.id.tel4);
            tel5 = (ImageView) v.findViewById(R.id.tel5);
            tel6 = (ImageView) v.findViewById(R.id.tel6);
            tel7 = (ImageView) v.findViewById(R.id.tel7);
            tel8 = (ImageView) v.findViewById(R.id.tel8);
            tel9 = (ImageView) v.findViewById(R.id.tel9);
            tel10 = (ImageView) v.findViewById(R.id.tel10);

            if(Stelur.equals("10"))
            {
                tel1.setImageResource(R.drawable.egg_on);
                tel2.setImageResource(R.drawable.egg_on);
                tel3.setImageResource(R.drawable.egg_on);
                tel4.setImageResource(R.drawable.egg_on);
                tel5.setImageResource(R.drawable.egg_on);
                tel6.setImageResource(R.drawable.egg_on);
                tel7.setImageResource(R.drawable.egg_on);
                tel8.setImageResource(R.drawable.egg_on);
                tel9.setImageResource(R.drawable.egg_on);
                tel10.setImageResource(R.drawable.egg_on);
            }

            if(Stelur.equals("9"))
            {
                tel1.setImageResource(R.drawable.egg_off);
                tel2.setImageResource(R.drawable.egg_on);
                tel3.setImageResource(R.drawable.egg_on);
                tel4.setImageResource(R.drawable.egg_on);
                tel5.setImageResource(R.drawable.egg_on);
                tel6.setImageResource(R.drawable.egg_on);
                tel7.setImageResource(R.drawable.egg_on);
                tel8.setImageResource(R.drawable.egg_on);
                tel9.setImageResource(R.drawable.egg_on);
                tel10.setImageResource(R.drawable.egg_on);
            }

            if(Stelur.equals("8"))
            {
                tel1.setImageResource(R.drawable.egg_off);
                tel2.setImageResource(R.drawable.egg_off);
                tel3.setImageResource(R.drawable.egg_on);
                tel4.setImageResource(R.drawable.egg_on);
                tel5.setImageResource(R.drawable.egg_on);
                tel6.setImageResource(R.drawable.egg_on);
                tel7.setImageResource(R.drawable.egg_on);
                tel8.setImageResource(R.drawable.egg_on);
                tel9.setImageResource(R.drawable.egg_on);
                tel10.setImageResource(R.drawable.egg_on);
            }

            if(Stelur.equals("7"))
            {
                tel1.setImageResource(R.drawable.egg_off);
                tel2.setImageResource(R.drawable.egg_off);
                tel3.setImageResource(R.drawable.egg_off);
                tel4.setImageResource(R.drawable.egg_on);
                tel5.setImageResource(R.drawable.egg_on);
                tel6.setImageResource(R.drawable.egg_on);
                tel7.setImageResource(R.drawable.egg_on);
                tel8.setImageResource(R.drawable.egg_on);
                tel9.setImageResource(R.drawable.egg_on);
                tel10.setImageResource(R.drawable.egg_on);
            }
            if(Stelur.equals("6"))
            {
                tel1.setImageResource(R.drawable.egg_off);
                tel2.setImageResource(R.drawable.egg_off);
                tel3.setImageResource(R.drawable.egg_off);
                tel4.setImageResource(R.drawable.egg_off);
                tel5.setImageResource(R.drawable.egg_on);
                tel6.setImageResource(R.drawable.egg_on);
                tel7.setImageResource(R.drawable.egg_on);
                tel8.setImageResource(R.drawable.egg_on);
                tel9.setImageResource(R.drawable.egg_on);
                tel10.setImageResource(R.drawable.egg_on);
            }
            if(Stelur.equals("5"))
            {
                tel1.setImageResource(R.drawable.egg_off);
                tel2.setImageResource(R.drawable.egg_off);
                tel3.setImageResource(R.drawable.egg_off);
                tel4.setImageResource(R.drawable.egg_off);
                tel5.setImageResource(R.drawable.egg_off);
                tel6.setImageResource(R.drawable.egg_on);
                tel7.setImageResource(R.drawable.egg_on);
                tel8.setImageResource(R.drawable.egg_on);
                tel9.setImageResource(R.drawable.egg_on);
                tel10.setImageResource(R.drawable.egg_on);
            }
            if(Stelur.equals("4"))
            {
                tel1.setImageResource(R.drawable.egg_off);
                tel2.setImageResource(R.drawable.egg_off);
                tel3.setImageResource(R.drawable.egg_off);
                tel4.setImageResource(R.drawable.egg_off);
                tel5.setImageResource(R.drawable.egg_off);
                tel6.setImageResource(R.drawable.egg_off);
                tel7.setImageResource(R.drawable.egg_on);
                tel8.setImageResource(R.drawable.egg_on);
                tel9.setImageResource(R.drawable.egg_on);
                tel10.setImageResource(R.drawable.egg_on);
            }
            if(Stelur.equals("3"))
            {
                tel1.setImageResource(R.drawable.egg_off);
                tel2.setImageResource(R.drawable.egg_off);
                tel3.setImageResource(R.drawable.egg_off);
                tel4.setImageResource(R.drawable.egg_off);
                tel5.setImageResource(R.drawable.egg_off);
                tel6.setImageResource(R.drawable.egg_off);
                tel7.setImageResource(R.drawable.egg_off);
                tel8.setImageResource(R.drawable.egg_on);
                tel9.setImageResource(R.drawable.egg_on);
                tel10.setImageResource(R.drawable.egg_on);
            }
            if(Stelur.equals("2"))
            {
                tel1.setImageResource(R.drawable.egg_off);
                tel2.setImageResource(R.drawable.egg_off);
                tel3.setImageResource(R.drawable.egg_off);
                tel4.setImageResource(R.drawable.egg_off);
                tel5.setImageResource(R.drawable.egg_off);
                tel6.setImageResource(R.drawable.egg_off);
                tel7.setImageResource(R.drawable.egg_off);
                tel8.setImageResource(R.drawable.egg_off);
                tel9.setImageResource(R.drawable.egg_on);
                tel10.setImageResource(R.drawable.egg_on);
            }
            if(Stelur.equals("1"))
            {
                tel1.setImageResource(R.drawable.egg_off);
                tel2.setImageResource(R.drawable.egg_off);
                tel3.setImageResource(R.drawable.egg_off);
                tel4.setImageResource(R.drawable.egg_off);
                tel5.setImageResource(R.drawable.egg_off);
                tel6.setImageResource(R.drawable.egg_off);
                tel7.setImageResource(R.drawable.egg_off);
                tel8.setImageResource(R.drawable.egg_off);
                tel9.setImageResource(R.drawable.egg_off);
                tel10.setImageResource(R.drawable.egg_on);
            }
            if(Stelur.equals("0"))
            {
                tel1.setImageResource(R.drawable.egg_off);
                tel2.setImageResource(R.drawable.egg_off);
                tel3.setImageResource(R.drawable.egg_off);
                tel4.setImageResource(R.drawable.egg_off);
                tel5.setImageResource(R.drawable.egg_off);
                tel6.setImageResource(R.drawable.egg_off);
                tel7.setImageResource(R.drawable.egg_off);
                tel8.setImageResource(R.drawable.egg_off);
                tel9.setImageResource(R.drawable.egg_off);
                tel10.setImageResource(R.drawable.egg_off);
            }

            alert.setTitle("PERSEDIAAN TELUR SAAT INI");
            alert.show();
            Toast.makeText(this, "Stok telur saat ini : "+Stelur, Toast.LENGTH_LONG).show();

        }catch (Exception e){

        }

    }


    public void tamu(View view) {
        if(lampu1.equals("on0")){
            refD.child("data_0").setValue("off0");
            Ltamu.setImageResource(R.drawable.lamp_off);
        }
        if(lampu1.equals("off0")){
            refD.child("data_0").setValue("on0");
            Ltamu.setImageResource(R.drawable.lamp_on);
        }
        //Toast.makeText(this, "Lampu tamu", Toast.LENGTH_LONG).show();
    }

    public void keluarga(View view) {
        if(lampu2.equals("on1")){
            refD.child("data_1").setValue("off1");
            Lkeluarga.setImageResource(R.drawable.lamp_off);
        }
        if(lampu2.equals("off1")){
            refD.child("data_1").setValue("on1");
            Lkeluarga.setImageResource(R.drawable.lamp_on);
        }
        //Toast.makeText(this, "Lampu keluarga", Toast.LENGTH_LONG).show();
    }

    public void dapur(View view) {
        if(lampu3.equals("on2")){
            refD.child("data_2").setValue("off2");
            Ldapur.setImageResource(R.drawable.lamp_off);
        }
        if(lampu3.equals("off2")){
            refD.child("data_2").setValue("on2");
            Ldapur.setImageResource(R.drawable.lamp_on);

        }
        //Toast.makeText(this, "Lampu dapur", Toast.LENGTH_LONG).show();
    }

    public void teras(View view) {
        if(lampu4.equals("on3")){
            refD.child("data_3").setValue("off3");
            Lteras.setImageResource(R.drawable.lamp_off);
        }
        if(lampu4.equals("off3")){
            refD.child("data_3").setValue("on3");
            Lteras.setImageResource(R.drawable.lamp_on);

        }
        //Toast.makeText(this, "Lampu teras", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Apakan anda tetap ingin menutup aplikasi?");
            builder.setCancelable(false);

            listener = new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == DialogInterface.BUTTON_POSITIVE){
                        finishAffinity();
                    }

                    if(which == DialogInterface.BUTTON_NEGATIVE){
                        dialog.cancel();
                    }
                }
            };
            builder.setPositiveButton("Ya",listener);
            builder.setNegativeButton("Tidak", listener);
            builder.show();

        }
        return super.onKeyDown(keyCode, event);
    }


    public void notifgas() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.gas_on)
                .setContentTitle("Kebocoran Gas")
                .setContentText("Bahaya gas bocor !!!")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    public void notifsuhu() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.suhu)
                .setContentTitle("Kondisi Suhu Ruangan")
                .setContentText("Suhu ruangan terlalu panas, kipas akan hidup otomatis")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
