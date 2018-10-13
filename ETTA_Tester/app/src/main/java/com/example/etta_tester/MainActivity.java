package com.example.etta_tester;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private ModeAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private TextView temperature;
    private TextView humidity;
    private TextView water_level;

    private Call<List<Status>> statusContainer;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.refresh){
            subscribe();
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
            animation.setRepeatCount(Animation.INFINITE);
            findViewById(id).startAnimation(animation);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperature = findViewById(R.id.temperature);
        humidity = findViewById(R.id.humidity);
        water_level = findViewById(R.id.water_level);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Authorization", "key ttn-account-v2.zCviPKBUkdxgsxY6b6EJFZc6CzTF2AUTXXwC_i_wHVU")
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://t8.data.thethingsnetwork.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client);

        ThingsNetworkService service = builder.build().create(ThingsNetworkService.class);

        statusContainer = service.getStatus();

        subscribe();


        mRecyclerView = findViewById(R.id.mode);

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ModeAdapter();

        final List<Mode> modes = new ArrayList<>();

        modes.add(new Mode(getDrawable(R.drawable.cactus)));
        modes.add(new Mode(getDrawable(R.drawable.sunflower)));
        modes.add(new Mode(getDrawable(R.drawable.flowerinio)));
        modes.add(new Mode(getDrawable(R.drawable.rose)));

        mAdapter.changeData(modes);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public byte[] BaseToHex(String base){
        return android.util.Base64.decode(base, android.util.Base64.DEFAULT);
    }

    public void subscribe(){
            statusContainer.clone().enqueue(new Callback<List<Status>>() {
                @Override
                public void onResponse(@NonNull Call<List<Status>> call, @NonNull Response<List<Status>> response) {
                    if (response.body() != null) {
                        List<Status> data = response.body();
                        byte[] convertedData = BaseToHex(data.get(data.size() - 1).getRaw());

                        String temp = String.valueOf(convertedData[3])+"°";
                        String humi = String.valueOf(convertedData[1]+"%");
                        String waterLevel = String.valueOf(convertedData[5])+"%";

                        if(Integer.valueOf(waterLevel.replace("%", "").trim()) < 10){
                            water_level.setTextColor(Color.RED);
                        }else{
                            water_level.setTextColor(Color.BLACK);
                        }

                        temperature.setText(temp);
                        humidity.setText(humi);
                        water_level.setText(waterLevel);
                    }
                    refreshAction();
                }

                @Override
                public void onFailure(@NonNull Call<List<Status>> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    refreshAction();
                }
            });

    }

    public void refreshAction(){
        View item = findViewById(R.id.refresh);
        item.setEnabled(true);
        item.clearAnimation();
    }

}
