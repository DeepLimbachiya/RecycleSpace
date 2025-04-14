package com.example.taxio;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FileITR extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_itr);

        // Reference the UI components
        TextView phoneTextView = findViewById(R.id.phoneTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);
        TextView locationTextView = findViewById(R.id.locationTextView);
        TextView otherInfoTextView = findViewById(R.id.otherInfoTextView);
        Button contactUsButton = findViewById(R.id.contactUsButton);

        // Set any dynamic data if needed
        phoneTextView.setText("+1 234 567 8901\n+1 234 567 8902");
        emailTextView.setText("support@rds.com\nhelp@rds.com");
        locationTextView.setText("123 Main Street, Waterloo, ON, Canada");
        otherInfoTextView.setText("We are here to help you 24/7 with any inquiries or issues you may have.");

        // Set up click listener for the contact button
        contactUsButton.setOnClickListener(v ->
                Toast.makeText(FileITR.this, "Our customer support will contact you shortly on your registered mobile number!", Toast.LENGTH_SHORT).show()
        );
    }
}
