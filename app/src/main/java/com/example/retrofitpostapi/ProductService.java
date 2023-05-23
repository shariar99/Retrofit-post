package com.example.retrofitpostapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProductService {
    @POST("products")
    Call<Product> addProduct(@Body Product product);
}
