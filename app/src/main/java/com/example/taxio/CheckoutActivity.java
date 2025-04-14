package com.example.taxio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCheckout;
    private CheckoutAdapter checkoutAdapter;
    private List<Product> checkoutProductList;
    private Map<String, Integer> productQuantities;

    private static final String PREFS_NAME = "ProductPrefs";
    private static final String QUANTITIES_KEY = "Quantities";
    private static final String PRODUCTS_KEY = "Products";
    private static final int PAYPAL_REQUEST_CODE = 123;

    // PayPal Configuration
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION) // Use ENVIRONMENT_PRODUCTION for production
            .clientId(PaymentConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Start PayPal Service
        Intent serviceIntent = new Intent(this, PayPalService.class);
        serviceIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(serviceIntent);

        recyclerViewCheckout = findViewById(R.id.recyclerViewCheckoutProducts);
        recyclerViewCheckout.setLayoutManager(new LinearLayoutManager(this));

        checkoutProductList = new ArrayList<>();
        checkoutAdapter = new CheckoutAdapter(checkoutProductList, this);
        recyclerViewCheckout.setAdapter(checkoutAdapter);

        Button buttonPay = findViewById(R.id.buttonPayment);
        buttonPay.setOnClickListener(v -> processPayment());

        // Load products and quantities for checkout
        loadCheckoutProducts();
    }

    private void loadCheckoutProducts() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String productsJson = sharedPreferences.getString(PRODUCTS_KEY, "[]");
        List<Product> storedProducts = gson.fromJson(productsJson, new TypeToken<List<Product>>() {}.getType());

        String quantitiesJson = sharedPreferences.getString(QUANTITIES_KEY, "{}");
        productQuantities = gson.fromJson(quantitiesJson, new TypeToken<Map<String, Integer>>() {}.getType());

        if (storedProducts != null && productQuantities != null) {
            checkoutProductList.clear();
            for (Product product : storedProducts) {
                Integer quantity = productQuantities.get(product.getImageUrl());
                if (quantity != null && quantity > 0) {
                    // Add product with quantity to the checkout list
                    Product productWithQuantity = new Product(product.getName(), product.getDescription(), product.getPrice(), product.getImageUrl(), quantity);
                    checkoutProductList.add(productWithQuantity);
                }
            }
            checkoutAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No products found in checkout", Toast.LENGTH_SHORT).show();
        }
    }

    private void processPayment() {
        BigDecimal amount = calculateTotalAmount();
        PayPalPayment payment = new PayPalPayment(amount, "USD", "Test Payment",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show();
                        // Optionally, send payment details to your server for verification
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Invalid Payment", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private BigDecimal calculateTotalAmount() {
        // Calculate total amount from checkoutProductList
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Product product : checkoutProductList) {
            BigDecimal price = BigDecimal.valueOf(product.getPrice()); // Convert double to BigDecimal
            totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(product.getQuantity())));
        }
        return totalAmount;
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
