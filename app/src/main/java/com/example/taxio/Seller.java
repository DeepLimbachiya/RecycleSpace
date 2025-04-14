package com.example.taxio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Seller extends AppCompatActivity {

    private EditText editTextProductName, editTextProductDescription, editTextProductPrice, editTextProductQuantity;
    private ImageView imageViewProductPreview;
    private Uri imageUri;

    private static final String PREFS_NAME = "ProductPrefs";
    private static final String PRODUCTS_KEY = "Products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductDescription = findViewById(R.id.editTextProductDescription);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        editTextProductQuantity = findViewById(R.id.editTextProductQuantity);
        imageViewProductPreview = findViewById(R.id.imageViewProductPreview);

        Button buttonUploadImage = findViewById(R.id.buttonUploadImage);
        buttonUploadImage.setOnClickListener(v -> openImagePicker());

        Button buttonSubmitProduct = findViewById(R.id.buttonSubmitProduct);
        buttonSubmitProduct.setOnClickListener(v -> submitProduct());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageViewProductPreview.setVisibility(View.VISIBLE);
            imageViewProductPreview.setImageURI(imageUri);
        }
    }

    private void submitProduct() {
        String name = editTextProductName.getText().toString();
        String description = editTextProductDescription.getText().toString();
        String price = editTextProductPrice.getText().toString();
        String quantity = editTextProductQuantity.getText().toString();

        // Validate inputs
        if (name.isEmpty() || description.isEmpty() || price.isEmpty() || quantity.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the image locally and get the file path
        String imagePath = saveImageLocally(imageUri);
        if (imagePath == null) {
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a product object
        Product product = new Product(name, description, Double.parseDouble(price), imagePath, Integer.parseInt(quantity));

        // Save product locally using SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        // Retrieve existing products
        String productsJson = sharedPreferences.getString(PRODUCTS_KEY, "[]");
        List<Product> productList = gson.fromJson(productsJson, new TypeToken<List<Product>>() {}.getType());

        // Add new product to list
        productList.add(product);

        // Save updated product list
        String updatedProductsJson = gson.toJson(productList);
        editor.putString(PRODUCTS_KEY, updatedProductsJson);
        editor.apply();

        // Inform the user
        Toast.makeText(this, "Product submitted successfully", Toast.LENGTH_SHORT).show();

        // Clear the input fields
        clearInputs();
    }

    private String saveImageLocally(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            File imageFile = new File(getFilesDir(), System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void clearInputs() {
        editTextProductName.setText("");
        editTextProductDescription.setText("");
        editTextProductPrice.setText("");
        editTextProductQuantity.setText("");
        imageViewProductPreview.setVisibility(View.GONE);
        imageUri = null;
    }
}
