package com.mobilstudio.ide;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class EditorActivity extends AppCompatActivity {

    private EditText etFilePath;
    private EditText etCode;

    private Button btnSave;
    private Button btnOpen;

    private LinearLayout floatingPreview;
    private FrameLayout phoneCanvas;

    private float dX, dY;

    private String repoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        repoPath = getIntent().getStringExtra("REPO_PATH");

        etFilePath = findViewById(R.id.etFilePath);
        etCode = findViewById(R.id.etCode);

        btnSave = findViewById(R.id.btnSave);
        btnOpen = findViewById(R.id.btnOpen);

        floatingPreview = findViewById(R.id.floatingPreview);
        phoneCanvas = findViewById(R.id.phoneCanvas);

        // Önizleme sürükleme
        floatingPreview.setOnTouchListener((view, event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    view.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    break;

                default:
                    return false;
            }

            return true;
        });

        // Dosya yolu dinleme
        etFilePath.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().endsWith("/")) {

                    File dir = new File(repoPath, s.toString());

                    if (!dir.exists()) {

                        if (dir.mkdirs()) {

                            Toast.makeText(EditorActivity.this,
                                    "Klasör oluşturuldu",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        // Kod değişince canlı önizleme
        etCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                renderLiveXml(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        btnSave.setOnClickListener(v -> saveCurrentFile());

        btnOpen.setOnClickListener(v -> openCurrentFile());

    }

    private void saveCurrentFile() {

        String path = etFilePath.getText().toString().trim();

        if (path.isEmpty()) {

            Toast.makeText(this,
                    "Dosya yolu boş",
                    Toast.LENGTH_SHORT).show();

            return;

        }

        try {

            File file = new File(repoPath, path);

            if (file.getParentFile() != null &&
                    !file.getParentFile().exists()) {

                file.getParentFile().mkdirs();

            }

            FileOutputStream fos = new FileOutputStream(file);

            fos.write(etCode.getText().toString().getBytes());

            fos.close();

            Toast.makeText(this,
                    "Dosya kaydedildi",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

            Toast.makeText(this,
                    e.getMessage(),
                    Toast.LENGTH_LONG).show();

        }

    }

    private void openCurrentFile() {

        String path = etFilePath.getText().toString().trim();

        if (path.isEmpty()) {

            Toast.makeText(this,
                    "Dosya yolu boş",
                    Toast.LENGTH_SHORT).show();

            return;

        }

        try {

            File file = new File(repoPath, path);

            if (!file.exists()) {

                Toast.makeText(this,
                        "Dosya bulunamadı",
                        Toast.LENGTH_SHORT).show();

                return;

            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file)
                    )
            );

            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {

                builder.append(line).append("\n");

            }

            reader.close();

            etCode.setText(builder.toString());

            Toast.makeText(this,
                    "Dosya açıldı",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

            Toast.makeText(this,
                    e.getMessage(),
                    Toast.LENGTH_LONG).show();

        }

    }

    private void renderLiveXml(String xmlCode) {

        phoneCanvas.removeAllViews();

        if (xmlCode.contains("<Button")) {

            Button button = new Button(this);

            button.setText("AI Butonu");

            phoneCanvas.addView(button);

        }

        if (xmlCode.contains("<TextView")) {

            TextView textView = new TextView(this);

            textView.setText("AI Metni");

            phoneCanvas.addView(textView);

        }

    }

}
