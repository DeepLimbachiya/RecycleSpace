package com.example.taxio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {
    private List<Product> productList;
    private Context context;

    public CheckoutAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_checkout, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewName.setText(product.getName());
        holder.textViewDescription.setText(product.getDescription());
        holder.textViewPrice.setText(String.format("$%.2f", product.getPrice()));
        holder.textViewQuantity.setText(String.format("Quantity: %d", product.getQuantity()));

        // Load image using Glide
        Glide.with(context).load(product.getImageUrl()).into(holder.imageViewProduct);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDescription, textViewPrice, textViewQuantity;
        ImageView imageViewProduct;

        CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
        }
    }
}
