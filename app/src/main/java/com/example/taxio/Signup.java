package com.example.taxio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    private TextInputLayout username_signup, email_signup, phoneno_signup, password_signup, confirm_password_signup;
    private Button callLogin, signup_btn;
    private FirebaseAuth auth;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        callLogin = findViewById(R.id.callLogin);
        username_signup = findViewById(R.id.username_signup);
        email_signup = findViewById(R.id.email_signup);
        phoneno_signup = findViewById(R.id.phoneno_signup);
        password_signup = findViewById(R.id.password_signup);
        confirm_password_signup = findViewById(R.id.conpassword_signup);
        signup_btn = findViewById(R.id.signup_btn);

        auth = FirebaseAuth.getInstance();

        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private Boolean validateUsername() {
        String val = username_signup.getEditText().getText().toString();
        String noWhiteSpaces = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            username_signup.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            username_signup.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpaces)) {
            username_signup.setError("White spaces are not allowed");
            return false;
        } else {
            username_signup.setError(null);
            username_signup.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = email_signup.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            email_signup.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email_signup.setError("Invalid Email Address, Only accept @gmail.com");
            return false;
        } else {
            email_signup.setError(null);
            email_signup.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneno() {
        String val = phoneno_signup.getEditText().getText().toString();
        if (val.isEmpty()) {
            phoneno_signup.setError("Field cannot be empty");
            return false;
        } else if (!(val.length() == 10)) {
            phoneno_signup.setError("Enter 10 digits");
            return false;
        } else {
            phoneno_signup.setError(null);
            phoneno_signup.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password_signup.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if (val.isEmpty()) {
            password_signup.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password_signup.setError("Password is too weak");
            return false;
        } else {
            password_signup.setError(null);
            password_signup.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateConfirmPassword() {
        String val = confirm_password_signup.getEditText().getText().toString();
        String passwordVal = password_signup.getEditText().getText().toString();

        if (val.isEmpty()) {
            confirm_password_signup.setError("Field cannot be empty");
            return false;
        } else if (!val.equals(passwordVal)) {
            confirm_password_signup.setError("Passwords do not match");
            return false;
        } else {
            confirm_password_signup.setError(null);
            confirm_password_signup.setErrorEnabled(false);
            return true;
        }
    }

    public void signupUser(View view) {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        if (!validateUsername() | !validateEmail() | !validatePhoneno() | !validatePassword() | !validateConfirmPassword()) {
            return;
        }

        //Get all the values
        String username = username_signup.getEditText().getText().toString();
        String email = email_signup.getEditText().getText().toString();
        String phoneno = phoneno_signup.getEditText().getText().toString();
        String password = password_signup.getEditText().getText().toString();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    userID = firebaseUser.getUid();
                    UserHelperClass helperClass = new UserHelperClass(username, email, phoneno, password);
                    reference.child(userID).setValue(helperClass);

                    Toast.makeText(Signup.this, "Signup Successful. Now Login", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Signup.this, Login.class));
                } else {
                    Toast.makeText(Signup.this, "Signup Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
