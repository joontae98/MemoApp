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

public class AddUserActivity extends AppCompatActivity {

    EditText etxName, etxEmail, etxPw;
    Button btnReq;
    String TAG = "AddUserActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);

        init();

        btnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser(etxName.getText().toString(), etxEmail.getText().toString(), etxPw.getText().toString());
                finish();
            }
        });
    }

    //view connect
    public void init() {
        etxName = (EditText) findViewById(R.id.etx_addUser_name);
        etxEmail = (EditText) findViewById(R.id.etx_addUser_email);
        etxPw = (EditText) findViewById(R.id.etx_addUser_pw);
        btnReq = (Button) findViewById(R.id.btn_addUser_request);
    }

    //server request
    public void createUser(String name, String email, String pw) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String url = bundle.getString("url");
        url += "/adduser";
        Log.e(TAG, url);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse(AddUser) 호출됨 :" + response);
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
                params.put("name", name);
                params.put("email", email);
                params.put("password", pw);
                return params;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(AddUserActivity.this).add(request);
    }
}
