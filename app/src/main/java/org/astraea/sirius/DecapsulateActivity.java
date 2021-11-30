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

import encrypt.AES;
import encrypt.ARC4;
import encrypt.BlowFish;
import encrypt.ChaCha20;
import io.paperdb.Paper;

public class DecapsulateActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decapsulate);
        setTitle("Decapsulate");

        EditText message = findViewById(R.id.cipherTextInput);
        EditText password = findViewById(R.id.passwordInput);

        Spinner algorithmSelect = findViewById(R.id.algorithmSelect);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.algorithms, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        algorithmSelect.setAdapter(adapter);

        Button decapsulateButton = findViewById(R.id.decapsulateButton);

        TextView resultView = findViewById(R.id.resultView);

        decapsulateButton.setOnClickListener((ev) -> {
            String result = "";
            try {
                switch ((String) algorithmSelect.getSelectedItem()) {
                    case "AES/CBC":
                        result = AES.decryptCBC(password.getText().toString(), message.getText().toString());
                        break;
                    case "AES/GCM":
                        result = AES.decryptGCM(password.getText().toString(), message.getText().toString());
                        break;
                    case "ChaCha20/Poly1305":
                        result = ChaCha20.decryptPoly1305(password.getText().toString(), message.getText().toString());
                        break;
                    case "Blowfish/CBC":
                        result = BlowFish.decryptCBC(password.getText().toString(), message.getText().toString());
                        break;
                    case "ARC4":
                        result = ARC4.decrypt(password.getText().toString(), message.getText().toString());
                        break;
                    default:
                        result = "select valid algorithm: " + algorithmSelect.getSelectedItem();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Cannot Decrypting.", Toast.LENGTH_SHORT).show();
            }
            resultView.setText(result);
            Paper.book().write(message.getText().toString(), result);
        });

        Button copyButton = findViewById(R.id.copyButton);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        copyButton.setOnClickListener((ev) -> {
            ClipData clipData = ClipData.newPlainText("plainText", resultView.getText());
            clipboard.setPrimaryClip(clipData);
            Toast toast = Toast.makeText(this, "copied!", Toast.LENGTH_SHORT);
            toast.show();
        });
    }
}