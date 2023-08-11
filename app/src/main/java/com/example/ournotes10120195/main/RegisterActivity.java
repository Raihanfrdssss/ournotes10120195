package com.example.ournotes10120195.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ournotes10120195.R;
import com.example.ournotes10120195.main.menu.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/*
 * NIM : 10120195
 *Nama : Muhammad Raihan Firdaus
 *Kelas : IF5
 *Email : raihan.10120195@mahasiswa.unikom.ac.id
 * */

public class RegisterActivity extends AppCompatActivity {
    // inisialisasi variable
    private Button btnRegister;
    private EditText inputEmail, inputPassword;
    private TextView login;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // firebase
        auth = FirebaseAuth.getInstance();

        // inisialisasi view
        btnRegister = (Button) findViewById(R.id.button_register);
        login = (TextView) findViewById(R.id.login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        // validasi user
        if(auth.getCurrentUser() != null){
            finish();
            return;
        }

        // event klik button register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        // event klik text login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    // fungsi login user
    private void loginUser() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

    // fungsi register user
    private void registerUser() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // validasi jika email kosong
        if (email.isEmpty()) {
            inputEmail.setError("Anda harus mengisikan email!");
            inputEmail.requestFocus();
            return;
        }

        // validasi input type email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Mohon masukkan email yang valid!");
            inputEmail.requestFocus();
            return;
        }

        // validasi jika password kosong
        if (password.isEmpty()) {
            inputPassword.setError("Anda harus mengisikan password!");
            inputPassword.requestFocus();
            return;
        }

        // validasi jika password kurang dari 8 karakter
        if (password.length() < 8) {
            inputPassword.setError("Minimum panjang password adalah 8 karakter!");
            inputPassword.requestFocus();
            return;
        }

        // membuat user baru email dan password yang sudah diinputkan oleh user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(email);
                    // menambahkan user ke database
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // menampilkan main activity jika berhasil
                                showMainActivity();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Registrasi gagal!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // fungsi menampilkan main activity jika registrasi berhasil
    private void showMainActivity() {
        Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }

}