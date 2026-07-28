package com.mobilstudio.ide;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

public class EditorActivity extends AppCompatActivity {

    private EditText etFilePath, etCode;
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
        floatingPreview = findViewById(R.id.floatingPreview);
        phoneCanvas = findViewById(R.id.phoneCanvas);

        // Sürüklenebilir Önizleme
        floatingPreview.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    view.animate().x(event.getRawX() + dX).y(event.getRawY() + dY).setDuration(0).start();
                    break;
                default:
                    return false;
            }
            return true;
        });

        // '/' İle Klasör Oluşturma
        etFilePath.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().endsWith("/")) {
                    File newDir = new File(repoPath, s.toString());
                    if (!newDir.exists() && newDir.mkdirs()) {
                        Toast.makeText(EditorActivity.this, "Klasör açıldı", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Canlı Render
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                renderLiveXml(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void renderLiveXml(String xmlCode) {
        phoneCanvas.removeAllViews();
        if (xmlCode.contains("<Button")) {
            Button btn = new Button(this);
            btn.setText("AI Butonu");
            phoneCanvas.addView(btn);
        } else if (xmlCode.contains("<TextView")) {
            TextView tv = new TextView(this);
            tv.setText("AI Metni");
            phoneCanvas.addView(tv);
        }
    }
}
