package org.astraea.sirius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import dh.Ec;
import io.paperdb.Book;
import io.paperdb.Paper;

public class HandshakeActivity extends AppCompatActivity {
    private final String PRIVATEKEY = "PRIVATEKEY", PUBLICKEY = "PUBLICKEY", HANDSHAKE = "HANDSHAKE";

    private Ec ec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handshake);
        setTitle("Handshake");

        Book book = Paper.book(HANDSHAKE);

        TextView publicKeyView = findViewById(R.id.publicKeyView);

        try {
            ec = new Ec();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "cannot init", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }

        String privateKey = book.read(PRIVATEKEY);
        if (privateKey != null) {
            try {
                ec.setPrivateKey(privateKey);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "cannot set previous private key", Toast.LENGTH_SHORT).show();
                return;
            }
            String publicKey = book.read(PUBLICKEY);
            if (publicKey != null) {
                try {
                    ec.setPublicKey(publicKey);
                    Toast.makeText(getApplicationContext(), "loaded previous key pair", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "cannot set previous public key", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        book.write(PRIVATEKEY, ec.getPrivateKey());
        book.write(PUBLICKEY, ec.getPublicKey());
        publicKeyView.setText(ec.getPublicKey());

        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener((ev) -> {
            try {
                ec = new Ec();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "cannot reset", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            publicKeyView.setText(ec.getPublicKey());

            book.write(PRIVATEKEY, ec.getPrivateKey());
            book.write(PUBLICKEY, ec.getPublicKey());
        });

        TextView passwordView = findViewById(R.id.passwordView);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        Button copyPublicKeyButton = findViewById(R.id.copyPublicKeyButton);
        copyPublicKeyButton.setOnClickListener((ev) -> {
            ClipData clipData = ClipData.newPlainText("publicKey", publicKeyView.getText());
            clipboard.setPrimaryClip(clipData);
            Toast toast = Toast.makeText(this, "copied!", Toast.LENGTH_SHORT);
            toast.show();
        });

        Button copyPasswordButton = findViewById(R.id.copyPasswordButton);
        copyPasswordButton.setOnClickListener((ev) -> {
            ClipData clipData = ClipData.newPlainText("password", passwordView.getText());
            clipboard.setPrimaryClip(clipData);
            Toast toast = Toast.makeText(this, "copied!", Toast.LENGTH_SHORT);
            toast.show();
        });

        EditText otherKeyInput = findViewById(R.id.otherPublicKeyEdit);

        Button calculateButton = findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener((ev) -> {
            String other = otherKeyInput.getText().toString();
            String result = "";
            try {
                result = ec.share(other);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "cannot share key", Toast.LENGTH_SHORT).show();
                return;
            }
            passwordView.setText(result);
        });
    }
}