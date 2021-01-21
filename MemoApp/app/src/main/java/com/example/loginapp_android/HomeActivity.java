package com.example.loginapp_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    String TAG = "HomeActivity";

    ArrayList<HashMap<String,String>> dataMap = new ArrayList<>();
    HashMap<String,String> memoData;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    String url, userId;
    Button btnCreate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userId = bundle.getString("userId");
        url = bundle.getString("url");
        url += "/home";

        init();
        getMemos();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(HomeActivity.this);
        recyclerView.setLayoutManager(layoutManager);


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CreateActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

    public void init() {
        btnCreate = (Button) findViewById(R.id.btn_create_save);
        recyclerView = (RecyclerView) findViewById(R.id.view_home_recycler);
    }

    public void getMemos(){

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            Log.e(TAG, "getMemo 호출됨 :" + response);
                            JSONArray jarr = new JSONArray(response);
                            for (int i =0 ; i < jarr.length(); i++){
                                memoData = new HashMap<String, String>();
                                JSONObject order = jarr.getJSONObject(i);
                                memoData.put("title", order.getString("title"));
                                memoData.put("content", order.getString("content"));
                                memoData.put("memoId",order.getString("_id"));
                                dataMap.add(memoData);
                            }
                            mAdapter = new RecyclerAdapter(dataMap,url);
                            recyclerView.setAdapter(mAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                return params;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(HomeActivity.this).add(request);

    }

}

