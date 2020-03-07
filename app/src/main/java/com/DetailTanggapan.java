package com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Model.Aspirasi;
import com.Model.CircleTransform;
import com.Model.KomentarModel;
import com.Model.SharedPrefManager;
import com.Model.URLs;
import com.Model.User;
import com.Model.VolleySingleton;
import com.adapter.KomentarAdapter;
import com.adapter.dprdbottom.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DetailTanggapan extends AppCompatActivity {
    Aspirasi aspirasi;
    User user;
    RecyclerView recyclerViewKomentar ;
    List<KomentarModel> komentarList ;

    private RecyclerView.Adapter adapterKomentar;
    private LinearLayoutManager linearLayoutManager;

    ImageView imgUser;
    TextView namaUser,komisiName,aspirasiMessage,dateCreated,tvPenanggung;
    EditText komentar ;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_aspirasi);

        recyclerViewKomentar = findViewById(R.id.rv_komentar);
        aspirasiMessage = findViewById(R.id.tv_aspirasi);
        namaUser = findViewById(R.id.tv_namaUser);
        komisiName = findViewById(R.id.tv_komisiName);
        imgUser = findViewById(R.id.iv_user);
        dateCreated =findViewById(R.id.tv_dateCreated);
        tvPenanggung = findViewById(R.id.tv_penanggung);

        komentar =findViewById(R.id.et_tambahKomentar);
        if(getIntent().hasExtra("id")){
            /**
             * Jika ada kiriman "Id"
             */
            aspirasi = getIntent().getParcelableExtra("id");
            user =  SharedPrefManager.getInstance(getApplication()).getUser();

            aspirasiMessage.setText(aspirasi.getMessage());
            namaUser.setText(aspirasi.getUserName());
            komisiName.setText("On : "+aspirasi.getKomisi());
            Picasso.with(getApplicationContext())
                    .load(aspirasi.getImage())
                    .centerCrop()
                    .transform(new CircleTransform(50,0))
                    .fit()
                    .into(imgUser);
            dateCreated.setText(aspirasi.getDateCreated());
            tvPenanggung.setText(aspirasi.getNmPenanggun());


            komentarList = new ArrayList<KomentarModel>();
            adapterKomentar = new KomentarAdapter(DetailTanggapan.this,komentarList);
            linearLayoutManager = new LinearLayoutManager(this);

            recyclerViewKomentar.setLayoutManager(linearLayoutManager);
            recyclerViewKomentar.setAdapter(adapterKomentar);
            getData(aspirasi.getIdAspirasi());


        }else{
            /**
             * Apabila Bundle tidak ada, ambil dari Intent
             */
        }





    }



    private void getData(String idAspirasi) {
        komentarList.clear();
        String url = URLs.URL_KOMENTAR+"?aspirasi_id="+idAspirasi;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("status");
                            //if no error in response
                            if (status) {
                                JSONArray jsonArrayKomentar = obj.getJSONArray("data");
                                for (int i = 0; i < jsonArrayKomentar.length(); i++) {
                                    try {
                                        JSONObject objKomentar = jsonArrayKomentar.getJSONObject(i);
                                        KomentarModel komentar = new KomentarModel(objKomentar);
                                        komentarList.add(komentar);
                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapterKomentar.notifyDataSetChanged();
                            } else {
                                Toast.makeText(DetailTanggapan.this, "Json Error :" +obj.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailTanggapan.this, "Json Error :" +e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailTanggapan.this,"Volley Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(DetailTanggapan.this).addToRequestQueue(stringRequest);
    }

}
