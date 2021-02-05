package com.example.loginapp_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView txtAddUser;
    EditText etxEmail, etxPw;
    Button btnReq;
    String TAG = "MainActivity.class",
            url = "http://192.168.0.113:3000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        txtAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "text onClick");
                Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        btnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 요청 코드
                login(etxEmail.getText().toString(), etxPw.getText().toString());
            }
        });

    }

    // view 연결 코드
    public void init() {
        etxEmail = (EditText) findViewById(R.id.etx_main_email);
        etxPw = (EditText) findViewById(R.id.etx_main_pw);
        txtAddUser = (TextView) findViewById(R.id.txt_main_addUser);
        btnReq = (Button) findViewById(R.id.btn_main_login);
    }

    //서버 요청 코드
    public void login(String email, String pw) {
        Log.e("url", url);

        JSONObject testjson = new JSONObject();
        try {
            testjson.put("email", email);
            testjson.put("password", pw);
            String jsonString = testjson.toString();

            Log.e(TAG, jsonString);
            final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, testjson, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.e(TAG,response.toString());
                        //받은 json형식의 응답을 받아
                        JSONObject jsonObject = new JSONObject(response.toString());

                        //key값에 따라 value값을 쪼개 받아옵니다.
                        String resultId = jsonObject.getString("approve_id");
                        String resultPassword = jsonObject.getString("approve_pw");
                        String userId = jsonObject.getString("_id");

                        if (!(resultId.equals("OK") & resultPassword.equals("OK"))) {
                            //로그인에 실패했을 경우 실행할 코드를 입력하시면 됩니다.
                            Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //이 곳에 성공 시 화면이동을 하는 등의 코드를 입력하시면 됩니다.
                        Toast.makeText(MainActivity.this, "로그인 완료", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("url", url);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
            //
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}