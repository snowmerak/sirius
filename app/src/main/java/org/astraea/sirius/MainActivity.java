package org.astraea.sirius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.crypto.tink.config.TinkConfig;
import com.google.crypto.tink.proto.Tink;

import java.security.GeneralSecurityException;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.activity_main);
        setTitle("Message capsule");

        Button handshakeButton = findViewById(R.id.handshakeButton);

        handshakeButton.setOnClickListener((ev) -> {
            Intent intent = new Intent(this, HandshakeActivity.class);
            startActivity(intent);
        });

        Button encapsulateButton = findViewById(R.id.encapsulateButton);

        encapsulateButton.setOnClickListener((ev) -> {
            Intent intent = new Intent(this, EncapsulateActivity.class);
            startActivity(intent);
        });

        Button decapsulateButton = findViewById(R.id.decapsulateButton);

        decapsulateButton.setOnClickListener((ev) -> {
            Intent intent = new Intent(this, DecapsulateActivity.class);
            startActivity(intent);
        });

        Button historyButton = findViewById(R.id.historyButton);

        historyButton.setOnClickListener((ev) -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        });
    }
}