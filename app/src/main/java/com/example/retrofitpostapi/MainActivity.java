package com.example.retrofitpostapi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    private Retrofit retrofit;

    private EditText titleEditText, priceEditText,descriptionEditText,imageEditText , categoryEditText;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        titleEditText = findViewById(R.id.titleEditText);
        priceEditText = findViewById(R.id.priceEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        imageEditText = findViewById(R.id.imageEditText);
        categoryEditText = findViewById(R.id.categoryEditText);
        addButton = findViewById(R.id.addButton);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, MainActivity.this);

        recyclerView.setAdapter(productAdapter);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://fakestoreapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString().trim();
                double price = Double.parseDouble(priceEditText.getText().toString().trim());
                String description = descriptionEditText.getText().toString().trim();
                String image = imageEditText.getText().toString().trim();
                String category = categoryEditText.getText().toString().trim();

                addProduct(title, price, description, image, category);
            }
        });

    }

    private void addProduct(String title, double price, String description, String image, String category) {
        ProductService productService = retrofit.create(ProductService.class);

        Product product = new Product();
        product.setTitle(title);
        product.setPrice(price);
        product.setDescription(description);
        product.setImage(image);
        product.setCategory(category);

        Call<Product> call = productService.addProduct(product);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product addedProduct = response.body();
                    productList.add(addedProduct);
                    productAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "API Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
