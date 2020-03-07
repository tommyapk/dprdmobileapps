package com;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Model.CircleTransform;
import com.Model.KomentarModel;
import com.Model.SharedPrefManager;
import com.Model.URLs;
import com.Model.User;
import com.Model.VolleySingleton;
import com.adapter.KomentarDetailAdapter;
import com.adapter.dprdbottom.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetailKomentar extends AppCompatActivity {
    KomentarModel thisKomentar ;
    User user;
    RecyclerView recyclerViewKomentar ;
    List<KomentarModel> data;

    private RecyclerView.Adapter adapterKomentar;
    private LinearLayoutManager linearLayoutManager;

    ImageView imgUser;
    TextView namaUser,komisiName,tanggapan,dateCreated;
    EditText komentar,txtReply ;
    Button btnSend,btnDelete;
    ProgressBar progressBar;

    View v;
    private JSONArray jsonArrayTanggapan = new JSONArray();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_detail_komentar);

       v = getWindow().getDecorView().findViewById(android.R.id.content);
        user = SharedPrefManager.getInstance(getApplicationContext() ).getUser();


        recyclerViewKomentar = findViewById(R.id.rv_komentar_children);
        tanggapan = findViewById(R.id.tv_tanggapan);
        namaUser = findViewById(R.id.tv_namaUser);
        imgUser = findViewById(R.id.civ_poto_profile);
        dateCreated =findViewById(R.id.tv_dateCreated);
        txtReply = findViewById(R.id.et_tambahKomentar);
        btnSend = findViewById(R.id.btn_sendKomentar);
        btnDelete = findViewById(R.id.btn_hapus);
        progressBar = findViewById(R.id.progressBar);

        if(getIntent().hasExtra("id")){
            /**
             * Jika ada kiriman "Id"
             */
            thisKomentar = getIntent().getParcelableExtra("id");
            data = new ArrayList<>();

            namaUser.setText(thisKomentar.getUserName());
            dateCreated.setText(thisKomentar.getDateCreated());
            tanggapan.setText(thisKomentar.getKomentar());
            Picasso.with(getApplicationContext())
                    .load(thisKomentar.getImage())
                    .centerCrop()
                    .transform(new CircleTransform(50,0))
                    .fit()
                    .into(imgUser);
            /**
             * inisialisasi data Recycleview daftar tanggapan
             */
            adapterKomentar = new KomentarDetailAdapter(getApplicationContext(),data);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerViewKomentar.setLayoutManager(linearLayoutManager);
            recyclerViewKomentar.setAdapter(adapterKomentar);
            getData();


            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendComment();
                }
            });



        }else{
            /**
             * Apabila Bundle tidak ada, ambil dari Intent
             */
        }
    }

    public void getData(){
        data.clear();
        progressBar.setVisibility(View.VISIBLE);
        String url = "?aspirasi_id="+thisKomentar.getAspirasiId()+"&parent="+thisKomentar.getIdKomentar();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_KOMENTAR+url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("status");
                            //if no error in response
                            if (status) {
                                JSONArray  jsonArray = obj.getJSONArray("data");
                                for (int i =0;i<jsonArray.length();i++){
                                    JSONObject objKmt =  jsonArray.getJSONObject(i);
                                    KomentarModel kmt = new KomentarModel(objKmt);
                                    data.add(kmt);
                                }
                                adapterKomentar.notifyDataSetChanged();
                            }else {
                                Toast.makeText(DetailKomentar.this, "Json Error :" + obj.getString("message") , Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailKomentar.this, "Json Error :" +e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(DetailKomentar.this,"Volley Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(DetailKomentar.this).addToRequestQueue(stringRequest);

    }
    private void sendComment() {
        if (txtReply.getText().length()<10){
            txtReply.setError("Berikan komentar setidaknya 10 karakter");
            txtReply.requestFocus();
            return;
        }
        btnSend.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        final String stringKomentar = txtReply.getText().toString();
        txtReply.setText("");
        txtReply.clearFocus();

        //        hidden keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_KOMENTAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("status");
                            //if no error in response
                            if (status) {
                                //getting the user from the response
                                JSONObject newObj = obj.getJSONObject("data");
                                KomentarModel newKmt = new KomentarModel(newObj);
                                data.add(newKmt);
                                int count = adapterKomentar.getItemCount()+1;
                                adapterKomentar.notifyItemInserted(count);
                                adapterKomentar.notifyItemChanged(count);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error :" +obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            btnSend.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error :" +e.toString(), Toast.LENGTH_SHORT).show();
                            btnSend.setEnabled(true);
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                        btnSend.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("komentar", stringKomentar);
                params.put("user_id", user.getIdUser());
                params.put("parent",thisKomentar.getIdKomentar());
                params.put("aspirasi_id", thisKomentar.getAspirasiId());
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}
