package org.astraea.sirius;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import encrypt.AES;
import encrypt.BlowFish;
import encrypt.ChaCha20;
import encrypt.ARC4;

public class EncapsulateActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encapsulate);
        setTitle("Encapsulate");

        EditText message = findViewById(R.id.plainTextInput);
        EditText password = findViewById(R.id.passwordInput);

        Spinner algorithmSelect = findViewById(R.id.algorithmSelect);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.algorithms, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        algorithmSelect.setAdapter(adapter);

        Button encapsulateButton = findViewById(R.id.encapsulateButton);

        TextView resultView = findViewById(R.id.resultView);

        encapsulateButton.setOnClickListener((ev) -> {
            String result = "";
            try {
                switch ((String) algorithmSelect.getSelectedItem()) {
                    case "AES/CBC":
                        result = AES.encryptCBC(password.getText().toString(), message.getText().toString());
                        break;
                    case "AES/GCM":
                        result = AES.encryptGCM(password.getText().toString(), message.getText().toString());
                        break;
                    case "ChaCha20/Poly1305":
                        result = ChaCha20.encryptPoly1305(password.getText().toString(), message.getText().toString());
                        break;
                    case "Blowfish/CBC":
                        result = BlowFish.encryptCBC(password.getText().toString(), message.getText().toString());
                        break;
                    case "ARC4":
                        result = ARC4.encrypt(password.getText().toString(), message.getText().toString());
                        break;
                    default:
                        result = "select valid algorithm: " + algorithmSelect.getSelectedItem();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Cannot Encrypting.", Toast.LENGTH_SHORT).show();
            }
            resultView.setText(result);
        });

        Button copyButton = findViewById(R.id.copyButton);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        copyButton.setOnClickListener((ev) -> {
            ClipData clipData = ClipData.newPlainText("cipherText", resultView.getText());
            clipboard.setPrimaryClip(clipData);
            Toast toast = Toast.makeText(this, "copied!", Toast.LENGTH_SHORT);
            toast.show();
        });
    }
}