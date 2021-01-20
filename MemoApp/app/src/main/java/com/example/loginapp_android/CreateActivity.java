package com.example.loginapp_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CreateActivity extends AppCompatActivity {

    String TAG = "HomeActivity";
    String title, memo;
    EditText etxTitle, etxMemo;
    Button btnCreate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = etxTitle.getText().toString();
                memo = etxMemo.getText().toString();
                createMemo(title,memo);
                etxTitle.setText("");
                etxMemo.setText("");
            }
        });
    }

    public void init() {
        etxTitle = (EditText) findViewById(R.id.etx_home_title);
        etxMemo = (EditText) findViewById(R.id.etx_home_memo);
        btnCreate = (Button) findViewById(R.id.btn_home_create);
    }

    public void createMemo(String title, String memo) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String url = bundle.getString("url");
        url += "/memo";
        Log.e(TAG, url);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse(Create) 호출됨 :" + response);
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
                params.put("title", title);
                params.put("content", memo);
                return params;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(CreateActivity.this).add(request);
    }
}

