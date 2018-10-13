package com.example.etta_tester;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ThingsNetworkService {

    @GET("/api/v2/query")
    Call<List<Status>> getStatus();
}
