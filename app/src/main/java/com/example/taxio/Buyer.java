package com.example.taxio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Buyer extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private Map<String, Integer> productQuantities;

    private static final String PREFS_NAME = "ProductPrefs";
    private static final String PRODUCTS_KEY = "Products";
    private static final String QUANTITIES_KEY = "Quantities";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        productQuantities = new HashMap<>();
        loadProductsFromLocalStorage();

        // Initialize ProductAdapter
        productAdapter = new ProductAdapter(productList, this, productQuantities);
        recyclerViewProducts.setAdapter(productAdapter);

        Button buttonCheckout = findViewById(R.id.buttonCheckout);
        buttonCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(this, CheckoutActivity.class);
            startActivity(intent);
        });
    }

    private void loadProductsFromLocalStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();

        // Load products
        String productsJson = sharedPreferences.getString(PRODUCTS_KEY, "[]");
        List<Product> storedProducts = gson.fromJson(productsJson, new TypeToken<List<Product>>() {}.getType());

        // Load quantities
        String quantitiesJson = sharedPreferences.getString(QUANTITIES_KEY, "{}");
        productQuantities = gson.fromJson(quantitiesJson, new TypeToken<Map<String, Integer>>() {}.getType());

        if (storedProducts != null) {
            productList.clear();
            for (Product product : storedProducts) {
                Integer quantity = productQuantities.getOrDefault(product.getImageUrl(), 0);
                product.setQuantity(quantity);
                productList.add(product);
            }

            // Notify adapter of data change
            if (productAdapter != null) {
                productAdapter.notifyDataSetChanged();
            }
        }
    }

    // Method to increase quantity
    public void increaseQuantity(Product product) {
        String productImageUrl = product.getImageUrl();
        int currentQuantity = productQuantities.getOrDefault(productImageUrl, 0);
        productQuantities.put(productImageUrl, currentQuantity + 1);
        saveProductQuantities();
        productAdapter.notifyDataSetChanged(); // Notify adapter of data change
    }

    // Method to remove product
    public void removeProduct(Product product) {
        String productImageUrl = product.getImageUrl();
        productQuantities.remove(productImageUrl);
        saveProductQuantities();
        productAdapter.notifyDataSetChanged(); // Notify adapter of data change
    }

    private void saveProductQuantities() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String quantitiesJson = gson.toJson(productQuantities);
        editor.putString(QUANTITIES_KEY, quantitiesJson);
        editor.apply();
    }
}
