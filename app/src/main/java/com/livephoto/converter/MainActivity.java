package com.livephoto.converter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_VIDEO = 1;
    private Button btnSelect;
    private LinearProgressIndicator progress;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSelect = findViewById(R.id.btnSelectVideo);
        progress = findViewById(R.id.progressIndicator);
        statusText = findViewById(R.id.statusText);

        btnSelect.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
            startActivityForResult(intent, PICK_VIDEO);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && data != null) {
            Uri videoUri = data.getData();
            processVideo(videoUri);
        }
    }

    private void processVideo(Uri uri) {
        // Logika sederhana untuk mendapatkan path asli (disesuaikan untuk production)
        String inputPath = uri.getPath(); // Ini contoh, aslinya butuh FileUtil
        
        statusText.setText("Sedang Memproses Live Photo...");
        progress.setVisibility(View.VISIBLE);
        btnSelect.setEnabled(false);

        LivePhotoHelper.convertVideoToLivePhoto(this, inputPath, new LivePhotoHelper.ConvertCallback() {
            @Override
            public void onSuccess(String jpgPath, String movPath) {
                runOnUiThread(() -> {
                    progress.setVisibility(View.GONE);
                    btnSelect.setEnabled(true);
                    statusText.setText("Berhasil! Live Photo disimpan di cache.");
                    Toast.makeText(MainActivity.this, "Live Photo Ready!", Toast.LENGTH_LONG).show();
                    
                    // Di sini lu bisa tambahin logic buat 'Share' ke TikTok
                    shareToGallery(jpgPath, movPath);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    progress.setVisibility(View.GONE);
                    btnSelect.setEnabled(true);
                    statusText.setText("Gagal: " + message);
                });
            }
        });
    }

    private void shareToGallery(String jpg, String mov) {
        // TikTok butuh kedua file ini ada di Gallery agar terdeteksi Live Photo
        // Gunakan MediaScannerConnection untuk mendaftarkan file ke sistem
    }
}
