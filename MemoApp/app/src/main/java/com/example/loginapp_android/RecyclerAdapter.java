package com.example.loginapp_android;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

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

//RecyclerView 연결 Adapter 클래스
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<HashMap<String, String>> mDataset;
    private String mUrl;

    String TAG = "RecyclerAdapter";

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,
                txtContent;
        ImageButton btnMore;

        public MyViewHolder(View v) {
            super(v);
            //view connect
            txtTitle = (TextView) v.findViewById(R.id.txt_row_title);
            txtContent = (TextView) v.findViewById(R.id.txt_row_content);
            btnMore = (ImageButton) v.findViewById(R.id.btn_row_more);
        }
    }

    public RecyclerAdapter(ArrayList<HashMap<String, String>> myDataset, String myUrl) {                     //생성자 매서드
        mDataset = myDataset;
        mUrl = myUrl + "/remove";
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_memo, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //view insert
        HashMap memo = mDataset.get(position);
        holder.txtTitle.setText(memo.get("title").toString());
        holder.txtContent.setText(memo.get("content").toString());
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu.OnMenuItemClickListener listener = new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // TODO Auto-generated method stub
                        switch (item.getItemId()) {
                            case R.id.modify:
                                break;
                            case R.id.delete:
                                StringRequest request = new StringRequest(Request.Method.POST, mUrl,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    Log.e(TAG, "removeMemo 호출됨 :" + response);
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
                                        params.put("memoId", memo.get("memoId").toString());
                                        return params;
                                    }
                                };
                                request.setShouldCache(false);
                                Volley.newRequestQueue(v.getContext()).add(request);
                                break;
                        }
                        return false;
                    }
                };

                PopupMenu popup = new PopupMenu(v.getContext(), v);//v는 클릭된 뷰를 의미
                        popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());
                        popup.setOnMenuItemClickListener(listener);
                        popup.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

}

