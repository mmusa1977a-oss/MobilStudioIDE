package com.mobilstudio.ide;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class DashboardActivity extends AppCompatActivity {

    private File rootProjectDir;

    private ArrayList<String> projectList = new ArrayList<>();
    private ArrayList<String> filteredList = new ArrayList<>();

    private ArrayAdapter<String> adapter;

    private ListView listView;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        rootProjectDir = new File(getExternalFilesDir(null), "MobilStudio_Projects");

        if (!rootProjectDir.exists()) {
            rootProjectDir.mkdirs();
        }

        listView = findViewById(R.id.listProjects);
        etSearch = findViewById(R.id.etSearch);

        Button btnNewProject = findViewById(R.id.btnNewProject);

        FloatingActionButton fab = findViewById(R.id.fabNewProject);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                filteredList
        );

        listView.setAdapter(adapter);

        loadProjects();

        btnNewProject.setOnClickListener(v -> showNewProjectDialog());

        fab.setOnClickListener(v -> showNewProjectDialog());

        listView.setOnItemClickListener((parent, view, position, id) -> {

            String projectName = filteredList.get(position);

            File repo = new File(rootProjectDir, projectName);

            Intent intent = new Intent(
                    DashboardActivity.this,
                    EditorActivity.class
            );

            intent.putExtra("REPO_PATH", repo.getAbsolutePath());

            startActivity(intent);

        });

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                filterProjects(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProjects();
    }

    private void loadProjects() {

        projectList.clear();

        File[] files = rootProjectDir.listFiles();

        if (files != null) {

            for (File file : files) {

                if (file.isDirectory()) {

                    projectList.add(file.getName());

                }

            }

        }

        Collections.sort(projectList);

        filterProjects("");

    }

    private void filterProjects(String text) {

        filteredList.clear();

        for (String project : projectList) {

            if (project.toLowerCase().contains(text.toLowerCase())) {

                filteredList.add(project);

            }

        }

        adapter.notifyDataSetChanged();

    }

    private void showNewProjectDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Yeni Proje");

        final EditText input = new EditText(this);

        input.setHint("Proje Adı");

        builder.setView(input);

        builder.setPositiveButton("Oluştur", (dialog, which) -> {

            String projectName = input.getText().toString().trim();

            if (projectName.isEmpty()) {

                Toast.makeText(
                        this,
                        "Proje adı boş olamaz",
                        Toast.LENGTH_SHORT
                ).show();

                return;

            }

            File repo = new File(rootProjectDir, projectName);

            if (repo.exists()) {

                Toast.makeText(
                        this,
                        "Bu proje zaten var",
                        Toast.LENGTH_SHORT
                ).show();

                return;

            }

            if (repo.mkdirs()) {

                Toast.makeText(
                        this,
                        "Proje oluşturuldu",
                        Toast.LENGTH_SHORT
                ).show();

                loadProjects();

            }

        });

        builder.setNegativeButton("İptal", null);

        builder.show();

    }

}
