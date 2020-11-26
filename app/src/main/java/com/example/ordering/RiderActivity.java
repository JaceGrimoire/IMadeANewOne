package com.example.ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;

public class RiderActivity extends AppCompatActivity {

    private TextInputEditText txtNumber;
    private TextInputEditText txtPass;
    private Button btnEnter;
    private Button btnUser;

    private String Number = "09123456789";
    private String Password = "12345";

    boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        txtNumber = findViewById(R.id.txtRiderNum);
        txtPass = findViewById(R.id.txtRiderPass);
        btnEnter = findViewById(R.id.btnRiderLogin);
        btnUser = findViewById(R.id.btnGoUser);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputNumber = txtNumber.getText().toString().trim();
                String inputPass = txtPass.getText().toString().trim();

                if (inputNumber.isEmpty() || inputPass.isEmpty()) {
                    Toast.makeText(RiderActivity.this, "Please enter all the details!", Toast.LENGTH_SHORT).show();
                } else {
                    isValid = validate(inputNumber, inputPass);

                    if (!isValid) {
                        Toast.makeText(RiderActivity.this, "Incorrect, please try again!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RiderActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RiderActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validate(String number, String password) {
        if (number.equals(Number) && password.equals(Password)) {
            return true;
        }

        return false;
    }
}