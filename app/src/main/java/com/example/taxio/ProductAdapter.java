package com.example.taxio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;
    private Map<String, Integer> productQuantities;

    // Updated constructor
    public ProductAdapter(List<Product> productList, Context context, Map<String, Integer> productQuantities) {
        this.productList = productList;
        this.context = context;
        this.productQuantities = productQuantities;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewName.setText(product.getName());
        holder.textViewDescription.setText(product.getDescription());
        holder.textViewPrice.setText(String.format("$%.2f", product.getPrice()));

        // Load image using Glide
        Glide.with(context).load(product.getImageUrl()).into(holder.imageViewProduct);

        // Get and display quantity if available
        Integer quantity = productQuantities.getOrDefault(product.getImageUrl(), 0);
        holder.textViewQuantity.setText(String.format("Added: %d", quantity));

        holder.buttonBuyNow.setOnClickListener(v -> {
            if (context instanceof Buyer) {
                ((Buyer) context).increaseQuantity(product);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDescription, textViewPrice, textViewQuantity;
        ImageView imageViewProduct;
        Button buttonBuyNow, buttonRemove;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            buttonBuyNow = itemView.findViewById(R.id.buttonBuyNow);
        }
    }
}
