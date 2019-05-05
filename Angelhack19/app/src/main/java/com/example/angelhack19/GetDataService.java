package com.example.angelhack19;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {
    @GET("/")
    Call<RetroPhoto> getAllData();
}
