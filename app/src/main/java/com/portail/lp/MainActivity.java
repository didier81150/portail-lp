package com.portail.lp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    public static final String PORTAL_PHONE_NUMBER = "07000011363000";
    private static final int REQUEST_CALL_PERMISSION = 1001;

    private TextView statusText;
    private Button permissionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.status_text);
        permissionButton = findViewById(R.id.permission_button);

        permissionButton.setOnClickListener(v -> requestCallPermission());

        checkAndMakeCall();
    }

    private void checkAndMakeCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            makeCallAndFinish();
        } else {
            statusText.setText(R.string.permission_required);
            permissionButton.setVisibility(View.VISIBLE);
            requestCallPermission();
        }
    }

    private void requestCallPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CALL_PHONE},
                REQUEST_CALL_PERMISSION
        );
    }

    private void makeCallAndFinish() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + PORTAL_PHONE_NUMBER));
            startActivity(callIntent);
            Toast.makeText(this, R.string.calling_portal, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, R.string.call_failed, Toast.LENGTH_SHORT).show();
        } finally {
            finishAndRemoveTask();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCallAndFinish();
            } else {
                statusText.setText(R.string.permission_required);
                permissionButton.setVisibility(View.VISIBLE);
            }
        }
    }
}
