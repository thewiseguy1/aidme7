package com.example.aidme7;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonSignIn;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editTextEmail = findViewById(R.id.editTextEmailSignIn);
        editTextPassword = findViewById(R.id.editTextPasswordSignIn);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        dbHelper = new DatabaseHelper(this);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    SQLiteDatabase db = dbHelper.getReadableDatabase();

                    String[] projection = {
                            DatabaseHelper.COLUMN_EMAIL,
                            DatabaseHelper.COLUMN_PASSWORD
                    };

                    String selection = DatabaseHelper.COLUMN_EMAIL + " = ? AND " +
                            DatabaseHelper.COLUMN_PASSWORD + " = ?";
                    String[] selectionArgs = {email, password};

                    Cursor cursor = db.query(
                            DatabaseHelper.TABLE_USERS,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            null
                    );

                    if (cursor.getCount() > 0) {
                        // Sign in successful
                        Toast.makeText(SignInActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                        startActivity(intent);
                        // Redirect to home page or wherever you want
                    } else {
                        // Sign in failed
                        Toast.makeText(SignInActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }

                    cursor.close();
                    db.close();
                } else {
                    Toast.makeText(SignInActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
