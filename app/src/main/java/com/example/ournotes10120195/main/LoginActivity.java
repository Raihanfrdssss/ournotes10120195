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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/*
 * NIM : 10120195
 *Nama : Muhammad Raihan Firdaus
 *Kelas : IF5
 *Email : raihan.10120195@mahasiswa.unikom.ac.id
 * */

public class LoginActivity extends AppCompatActivity {

    // inisialisasi variable
    private Button btnLogin;
    private EditText inputEmail, inputPassword;
    private TextView register;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //firebase
        auth = FirebaseAuth.getInstance();

        // inisialisasi view
        btnLogin = (Button) findViewById(R.id.button_login);
        register = (TextView) findViewById(R.id.register);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        // validasi user
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        // event klik button login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        // event klik text register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    // fungsi register user
    private void registerUser() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    // fungsi login user
    private void loginUser() {
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

        // validasi email dan password dengan firebase
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Login gagal!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}