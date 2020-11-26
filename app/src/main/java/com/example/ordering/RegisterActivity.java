package com.example.ordering;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText txtFirstName;
    private TextInputEditText txtLastName;
    private TextInputEditText txtAddress;
    private TextInputEditText txtNumber;
    private TextInputEditText txtEmail;
    private TextInputEditText txtPass;
    private TextInputEditText txtConfirmPass;
    private TextInputEditText txtCity;
    private TextInputEditText txtProvince;
    private TextInputEditText txtZipCode;
    private TextInputEditText txtBday;
    private Button btnReg;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("users");
        mAuth = FirebaseAuth.getInstance();

        txtFirstName = findViewById(R.id.txtRegFirstName);
        txtLastName = findViewById(R.id.txtRegLastName);
        txtAddress = findViewById(R.id.txtRegAddress);
        txtNumber = findViewById(R.id.txtRegNum);
        txtEmail = findViewById(R.id.txtRegEmail);
        txtPass = findViewById(R.id.txtRegPass);
        txtBday = findViewById(R.id.txtRegBirthdate);
        txtCity = findViewById(R.id.txtRegCity);
        txtProvince = findViewById(R.id.txtRegProvince);
        txtZipCode = findViewById(R.id.txtZipcode);
        btnReg = findViewById(R.id.btnRegister);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String inputEmail = txtEmail.getText().toString().trim();
                final String inputPass = txtPass.getText().toString().trim();


                final String TAG = "SYSTEM TEST:";
                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(inputEmail, inputPass)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    updateDatabase(firebaseUser, inputEmail, inputPass);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    cancelDatabase(null);
                                }
                            }
                        });
            }
        });

        Button goUp = (Button) findViewById(R.id.btnGoLogin);

        goUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGoLogin) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        }
    }

    public void updateDatabase(FirebaseUser fUser, String inputEmail, String inputPass) {
        final String inputFirstName = txtFirstName.getText().toString().trim();
        final String inputLastName = txtLastName.getText().toString().trim();
        final String inputAddress = txtAddress.getText().toString().trim();
        final String inputNumber = txtNumber.getText().toString().trim();
        final String inputBday = txtBday.getText().toString().trim();
        final String inputCity = txtCity.getText().toString().trim();
        final String inputProvince = txtProvince.getText().toString().trim();

        String uid = fUser.getUid();

        if (inputFirstName.isEmpty() || inputLastName.isEmpty() || inputAddress.isEmpty() || inputNumber.isEmpty() || inputEmail.isEmpty() || inputPass.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please enter all the details!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            Toast.makeText(RegisterActivity.this, "Please provide a valid email address!", Toast.LENGTH_SHORT).show();
        } else {
            mDatabase = FirebaseDatabase.getInstance();
            mRef = mDatabase.getReference("users");
            User user = new User(inputFirstName, inputLastName, inputAddress, inputNumber, inputEmail, inputBday, inputCity, inputProvince, uid);
            mRef.child(uid).setValue(user);
            Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void cancelDatabase(FirebaseUser user) {
        if(user==null) {
            Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT);
        }
    }
}