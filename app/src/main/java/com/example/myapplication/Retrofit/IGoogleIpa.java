package com.example.myapplication.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleIpa {
    @GET
    Call<String> getDireccions(@Url String url);
}
