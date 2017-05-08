package com.example.mcardenas.remoteconfigpoc;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class RemoteConfigActivity extends AppCompatActivity {

    private static final String LOG_TAG = "RCActivity";
    private static final String RC_KEY_SALUDO = "saludo";
    private static final String RC_KEY_PRIMARY_COLOR = "primary_color";

    private FirebaseRemoteConfig mRemoteConfig;
    private TextView saludo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_config);

        setup();

    }


    private void setup(){


        saludo = (TextView) findViewById(R.id.display_saludo);


        // 1.- obtiene instancia
        mRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build();
        mRemoteConfig.setConfigSettings(configSettings);


        // 2.- configura valores predeterminados
        mRemoteConfig.setDefaults(R.xml.rc_defaults);


        // 3.- Configura el evento "fetch" para recuperar valores
        int cacheExpiration = 15;
        mRemoteConfig.fetch(cacheExpiration).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RemoteConfigActivity.this, "Fetch Success", Toast.LENGTH_LONG).show();

                // activa el nuevo valor
                mRemoteConfig.activateFetched();
                mostrarSaludo();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RemoteConfigActivity.this, "Fetch failed", Toast.LENGTH_LONG).show();
            }
        });

    }


    private void mostrarSaludo(){
        String saludo_msg = mRemoteConfig.getString(RC_KEY_SALUDO);
        saludo.setText(saludo_msg);

        String primary_color = mRemoteConfig.getString(RC_KEY_PRIMARY_COLOR);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(primary_color)));
    }
}
