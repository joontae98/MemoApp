package com.example.loginapp_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

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

    ArrayList<HashMap<String, String>> dataMap;
    HashMap<String, String> memoData;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    String url, userId;
    Button btnCreate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        mAdapter = new RecyclerAdapter(dataMap, url, HomeActivity.this);
        ((RecyclerAdapter) mAdapter).setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos, HashMap memo) {
                PopupMenu popup = new PopupMenu(HomeActivity.this, v);//v는 클릭된 뷰를 의미
                popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String id = memo.get("memoId").toString();
                        switch (item.getItemId()) {
                            case R.id.modify:
                                Intent intent = new Intent(HomeActivity.this, UpdateActivity.class);
                                intent.putExtra("content",memo.get("content").toString());
                                intent.putExtra("memoId",memo.get("memoId").toString());
                                intent.putExtra("title",memo.get("title").toString());
                                intent.putExtra("url",url);
                                startActivity(intent);
                                break;
                            case R.id.delete:
                                deleteMemo(id, url);
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
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

    @Override
    protected void onResume() {
        super.onResume();
        // 화면이 홈으로 돌아왔을때 refresh 하기 위해
        getMemos();
    }

    public void init() {
        btnCreate = (Button) findViewById(R.id.btn_create_save);
        recyclerView = (RecyclerView) findViewById(R.id.view_home_recycler);
    }

    public void getMemos() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userId = bundle.getString("userId");
        url = bundle.getString("url");
        url += "/home";
        dataMap = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e(TAG, "getMemo 호출됨 :" + response);
                            JSONArray jarr = new JSONArray(response);
                            for (int i = 0; i < jarr.length(); i++) {
                                memoData = new HashMap<String, String>();
                                JSONObject order = jarr.getJSONObject(i);
                                memoData.put("title", order.getString("title"));
                                memoData.put("content", order.getString("content"));
                                memoData.put("memoId", order.getString("_id"));
                                dataMap.add(memoData);
                            }
                            mAdapter = new RecyclerAdapter(dataMap, url, HomeActivity.this);
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

    public void deleteMemo(String memoId, String url) {
        url += "/remove";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            getMemos();
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
                params.put("memoId", memoId);
                return params;
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }
}

