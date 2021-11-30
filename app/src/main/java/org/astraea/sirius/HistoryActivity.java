package org.astraea.sirius;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.paperdb.Book;
import io.paperdb.Paper;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("History");

        ArrayList<String> ciphers = new ArrayList<String>(Paper.book().getAllKeys());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ciphers);

        Spinner historySelect = findViewById(R.id.historySelect);
        historySelect.setAdapter(arrayAdapter);

        TextView cipherTextView = findViewById(R.id.cipherTextView);
        TextView plainTextView = findViewById(R.id.plainTextView);

        historySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (ciphers.size() < 1) {
                    return;
                }
                String plainText = Paper.book().read(ciphers.get(position));
                cipherTextView.setText(ciphers.get(position));
                plainTextView.setText(plainText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button removeButton = findViewById(R.id.removeButton);
        removeButton.setOnClickListener((ev) -> {
            String item = (String)historySelect.getSelectedItem();
            if (item == null || item.isEmpty()) {
                return;
            }
            Paper.book().delete(item);
            finish();
            startActivity(getIntent());
        });
    }
}