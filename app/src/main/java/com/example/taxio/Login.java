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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    Button callSignUp,login_btn;
    TextInputLayout email_login,password_login;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        callSignUp =findViewById(R.id.callSignup_btn);

        email_login=findViewById(R.id.email_login);
        password_login=findViewById(R.id.password_login);
        login_btn=findViewById(R.id.login_btn);

        auth=FirebaseAuth.getInstance();

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Signup.class);
                startActivity(intent);
            }
        });

    }
    private Boolean validateEmail(){
        String val = email_login.getEditText().getText().toString();

        if(val.isEmpty()){
            email_login.setError("Field cannot be empty");
            return false;
        }

        else{
            email_login.setError(null);
            email_login.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = password_login.getEditText().getText().toString();
        if(val.isEmpty()){
            password_login.setError("Field cannot be empty");
            return false;
        }
        else{
            password_login.setError(null);
            password_login.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(View view){
        if (!validateEmail() | !validatePassword()){
            return;
        }
        else {
            isUser();
        }
    }

    private void isUser() {

        String userEnteredEmail = email_login.getEditText().getText().toString().trim();
        String userEnteredPassword = password_login.getEditText().getText().toString().trim();

        auth.signInWithEmailAndPassword(userEnteredEmail,userEnteredPassword).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this,Dashboard.class));
                }
                else {
                    Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}