package com.seniorproject.salleh.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.seniorproject.salleh.R;

public class WelcomeActivity extends AppCompatActivity {
    private static final int REQUEST_SALLEM_PERMISSIONS = 123;
    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        attachSigninButton();
        attachRegisterButton();
        getPermissions();
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-7249219499142063~2560440435");
        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("DFDC2A32E5ECB1E43EB3ADAEFB76B2FF")
                .build();
        boolean isTestDevice = adRequest.isTestDevice(this);
        adView.loadAd(adRequest);

    }

    private void attachRegisterButton() {
        Button registerButton = (Button)findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
    }

    private void attachSigninButton() {
        Button signinButton = (Button)findViewById(R.id.btn_signin);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signinIntent);
                finish();
            }
        });
    }
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Make sure it's our original READ_LOCATION request
        if (requestCode == REQUEST_SALLEM_PERMISSIONS) {

            Toast.makeText(getApplicationContext(), "Needed Permissions Granted", Toast.LENGTH_SHORT).show();

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @TargetApi(23)
    public void getPermissions() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                )
        {

            shouldShowRequestPermissionRationale(
                    Manifest.permission_group.LOCATION);
            shouldShowRequestPermissionRationale(
                    Manifest.permission_group.STORAGE);
            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, REQUEST_SALLEM_PERMISSIONS
            );

        }

    }

}
