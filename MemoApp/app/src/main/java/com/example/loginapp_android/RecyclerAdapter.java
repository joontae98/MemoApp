package com.example.loginapp_android;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//RecyclerView 연결 Adapter 클래스
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<HashMap<String, String>> mDataset;
    private String mUrl;
    private Context mContext;

    String TAG = "RecyclerAdapter";
    public interface OnItemClickListener {
        void onItemClick(View v, int pos, HashMap memo) ;
    }
    // 리스너 객체 참조를 저장하는 변수
    private static OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,
                txtContent;
        ImageButton btnMore;

        public MyViewHolder(View view) {
            super(view);
            //view connect
            txtTitle = (TextView) view.findViewById(R.id.txt_row_title);
            txtContent = (TextView) view.findViewById(R.id.txt_row_content);
            btnMore = (ImageButton) view.findViewById(R.id.btn_row_more);
        }
    }

    public RecyclerAdapter(ArrayList<HashMap<String, String>> myDataset, String myUrl, Context myContext) {                     //생성자 매서드
        mDataset = myDataset;
        mUrl = myUrl;
        mContext = myContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
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
                int pos = position;
                if (pos != RecyclerView.NO_POSITION) {
                    // 리스너 객체의 메서드 호출.
                    if (mListener != null) {
                        mListener.onItemClick(v, pos, memo) ;
                    }
                }
//                PopupMenu popup = new PopupMenu(mContext, v);//v는 클릭된 뷰를 의미
//                        popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());
//                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem item) {
//                                switch (item.getItemId()){
//                                    case R.id.modify:
//                                        break;
//                                    case R.id.delete:
//                                        break;
//                                }
//                                return false;
//                            }
//                        });
//                        popup.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public void deleteMemo(String memoId, String url, Context mCtx) {
        url += "/remove";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("asdf", "remove 호출됨");
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
        Volley.newRequestQueue(mCtx).add(request);
    }

}

