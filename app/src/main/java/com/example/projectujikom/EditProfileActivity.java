package com.example.projectujikom;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText nipEditText, jenisKelaminEditText, ttlEditText, alamatEditText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nipEditText = findViewById(R.id.nip_edittext);
        jenisKelaminEditText = findViewById(R.id.jenis_kelamin_edittext);
        ttlEditText = findViewById(R.id.ttl_edittext);
        alamatEditText = findViewById(R.id.alamat_edittext);
        Button saveButton = findViewById(R.id.save_button);

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        // Load data profil dari SharedPreferences
        loadUserProfile();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validasi setiap kolom
                String nip = nipEditText.getText().toString();
                String jenisKelamin = jenisKelaminEditText.getText().toString();
                String ttl = ttlEditText.getText().toString();
                String alamat = alamatEditText.getText().toString();

                if (nip.isEmpty()) {
                    nipEditText.setError("NIP harus diisi");
                    return;
                }

                if (jenisKelamin.isEmpty()) {
                    jenisKelaminEditText.setError("Jenis Kelamin harus diisi");
                    return;
                }

                if (ttl.isEmpty()) {
                    ttlEditText.setError("Tempat Tanggal Lahir harus diisi");
                    return;
                }

                if (alamat.isEmpty()) {
                    alamatEditText.setError("Alamat harus diisi");
                    return;
                }

                // Jika semua kolom telah diisi, simpan data profil yang diedit ke SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("nip", nip);
                editor.putString("jenis_kelamin", jenisKelamin);
                editor.putString("ttl", ttl);
                editor.putString("alamat", alamat);
                editor.apply();
                finish();
            }
        });
    }

    private void loadUserProfile() {
        String nip = sharedPreferences.getString("nip", "");
        String jenisKelamin = sharedPreferences.getString("jenis_kelamin", "");
        String ttl = sharedPreferences.getString("ttl", "");
        String alamat = sharedPreferences.getString("alamat", "");

        nipEditText.setText(nip);
        jenisKelaminEditText.setText(jenisKelamin);
        ttlEditText.setText(ttl);
        alamatEditText.setText(alamat);
    }
}