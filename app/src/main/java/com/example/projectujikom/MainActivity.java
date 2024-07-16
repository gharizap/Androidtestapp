package com.example.projectujikom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private ImageView profileImage;
    private TextView nipTextView, jenisKelaminTextView, ttlTextView, alamatTextView;
    private Button editProfileButton, deleteProfileButton, insertProfileButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileImage = findViewById(R.id.profile_image);
        Button changeImageButton = findViewById(R.id.change_image_button);
        editProfileButton = findViewById(R.id.edit_profile_button);
        deleteProfileButton = findViewById(R.id.delete_profile_button);
        insertProfileButton = findViewById(R.id.insert_profile_button);
        nipTextView = findViewById(R.id.nip_textview);
        jenisKelaminTextView = findViewById(R.id.jenis_kelamin_textview);
        ttlTextView = findViewById(R.id.ttl_textview);
        alamatTextView = findViewById(R.id.alamat_textview);

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        // Load data profil dari SharedPreferences
        loadUserProfile();

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_IMAGE);
            }
        });

        insertProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });


        deleteProfileButton.setOnClickListener(new View.OnClickListener() { // Delete button functionality
            @Override
            public void onClick(View v) {
                clearUserProfile();
                refreshActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load data profil dari SharedPreferences saat aktivitas dijalankan kembali
        loadUserProfile();
    }

    private void loadUserProfile() {
        String nip = sharedPreferences.getString("nip", "");
        String jenisKelamin = sharedPreferences.getString("jenis_kelamin", "");
        String ttl = sharedPreferences.getString("ttl", "");
        String alamat = sharedPreferences.getString("alamat", "");
        String encodedImage = sharedPreferences.getString("profile_image", "");

        boolean hasProfile = !nip.isEmpty() || !jenisKelamin.isEmpty() || !ttl.isEmpty() || !alamat.isEmpty() || !encodedImage.isEmpty();

        if (hasProfile) {
            nipTextView.setText(nip);
            jenisKelaminTextView.setText(jenisKelamin);
            ttlTextView.setText(ttl);
            alamatTextView.setText(alamat);

            if (!encodedImage.isEmpty()) {
                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                profileImage.setImageBitmap(decodedBitmap);
            }

            // Show Edit and Delete buttons, hide Insert button
            editProfileButton.setVisibility(View.VISIBLE);
            deleteProfileButton.setVisibility(View.VISIBLE);
            insertProfileButton.setVisibility(View.GONE);
        } else {
            // Hide Edit and Delete buttons, show Insert button
            editProfileButton.setVisibility(View.GONE);
            deleteProfileButton.setVisibility(View.GONE);
            insertProfileButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);

                // Simpan gambar profil ke SharedPreferences
                saveImageToSharedPreferences(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageToSharedPreferences(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profile_image", encodedImage);
        editor.apply();
    }

    private void clearUserProfile() { // Method to clear SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void refreshActivity() { // Method to refresh the activity
        Intent intent = getIntent();
        finish();
        overridePendingTransition(0, 0); // Disable the animation for finish
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}