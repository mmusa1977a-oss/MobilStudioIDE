package com.mobilstudio.ide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;

public class DashboardActivity extends AppCompatActivity {

    private File rootProjectDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        rootProjectDir = new File(getExternalFilesDir(null), "MobilStudio_Projects");
        if (!rootProjectDir.exists()) {
            rootProjectDir.mkdirs();
        }

        FloatingActionButton fab = findViewById(R.id.fabNewProject);
        fab.setOnClickListener(v -> showNewProjectDialog());
    }

    private void showNewProjectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Yeni Repo Oluştur");

        final EditText input = new EditText(this);
        input.setHint("Örn: OyunKlavyesi");
        builder.setView(input);

        builder.setPositiveButton("Oluştur", (dialog, which) -> {
            String repoName = input.getText().toString().trim();
            if (!repoName.isEmpty()) {
                createNewRepo(repoName);
            }
        });
        builder.setNegativeButton("İptal", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void createNewRepo(String repoName) {
        File newRepo = new File(rootProjectDir, repoName);
        if (!newRepo.exists() && newRepo.mkdirs()) {
            Toast.makeText(this, "Oluşturuldu: " + repoName, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DashboardActivity.this, EditorActivity.class);
            intent.putExtra("REPO_PATH", newRepo.getAbsolutePath());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Bu proje zaten var!", Toast.LENGTH_SHORT).show();
        }
    }
}
