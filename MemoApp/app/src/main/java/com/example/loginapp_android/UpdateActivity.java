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

public class UpdateActivity extends AppCompatActivity {

    EditText etxtitle, etxMemo;
    Button btnUpdate;
    String id, content, title, url;
    String TAG = "UpdateActivitiy";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        init();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("memoId");
        content = bundle.getString("content");
        title = bundle.getString("title");


        etxtitle.setText(title);
        etxMemo.setText(content);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = bundle.getString("url") + "/Update";
                updateMemo(etxtitle.getText().toString(), etxMemo.getText().toString());
                finish();
            }
        });
    }

    private void init() {
        etxtitle = (EditText) findViewById(R.id.etx_update_title);
        etxMemo = (EditText) findViewById(R.id.etx_update_memo);
        btnUpdate = (Button) findViewById(R.id.btn_update);
    }

    public void updateMemo(String title, String memo) {

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse(Update) 호출됨 :" + response);
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
                params.put("userId", id);
                return params;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(UpdateActivity.this).add(request);
    }
}