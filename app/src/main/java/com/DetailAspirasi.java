package com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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


public class DetailAspirasi extends AppCompatActivity {
    Aspirasi aspirasi;
    RecyclerView recyclerViewKomentar ;
    List<KomentarModel> komentarList ;

    private RecyclerView.Adapter adapterKomentar;
    private LinearLayoutManager linearLayoutManager;

    ImageView imgUser;
    TextView namaUser,komisiName,aspirasiMessage,dateCreated,tvPenanggun;
    ProgressBar progressBar;
    private JSONArray jsonArrayTanggapan;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_aspirasi);
        progressBar = findViewById(R.id.progressBar);

        jsonArrayTanggapan =new JSONArray();

        recyclerViewKomentar = findViewById(R.id.rv_komentar);
        aspirasiMessage = findViewById(R.id.tv_aspirasi);
        namaUser = findViewById(R.id.tv_namaUser);
        komisiName = findViewById(R.id.tv_komisiName);
        imgUser = findViewById(R.id.iv_user);
        dateCreated =findViewById(R.id.tv_dateCreated);
        tvPenanggun = findViewById(R.id.tv_penanggung);

        if(getIntent().hasExtra("id")){
            /**
             * Jika ada kiriman "Id"
             */
            aspirasi = getIntent().getParcelableExtra("id");
            aspirasiMessage.setText(aspirasi.getMessage());
            namaUser.setText(aspirasi.getUserName());
            komisiName.setText("On : "+aspirasi.getKomisi());
            tvPenanggun.setText(aspirasi.getNmPenanggun());
            Picasso.with(getApplicationContext())
                    .load(aspirasi.getImage())
                    .centerCrop()
                    .transform(new CircleTransform(50,0))
                    .fit()
                    .into(imgUser);
            dateCreated.setText(aspirasi.getDateCreated());

            /**
             * cek apakah jsonArrayTanggapansudahAda
             */
                getJsonArray(aspirasi.getIdAspirasi());
//
//
            komentarList = new ArrayList<KomentarModel>();
            adapterKomentar = new KomentarAdapter(this.getApplicationContext(),komentarList);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerViewKomentar.setLayoutManager(linearLayoutManager);
            recyclerViewKomentar.setAdapter(adapterKomentar);


//            getData("0");
        }else{
            /**
             * Apabila Bundle tidak ada, ambil dari Intent
             */
        }





    }

    public void getJsonArray(String idAspirasi){
        progressBar.setVisibility(View.VISIBLE);
        String url = URLs.URL_KOMENTAR+"?aspirasi_id="+idAspirasi+"&parent=0" +
                "";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("status");
                            //if no error in response
                            if (status) {
                                jsonArrayTanggapan = obj.getJSONArray("data");
                            }else {

                                Toast.makeText(DetailAspirasi.this,  obj.getString("message") , Toast.LENGTH_SHORT).show();
                            }
                            getData("p");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailAspirasi.this, "Json Error :" +e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(DetailAspirasi.this,"Volley Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(DetailAspirasi.this).addToRequestQueue(stringRequest);
    }


    private void getData(String idAspirasi) {
        komentarList.clear();
        for (int i = 0; i < jsonArrayTanggapan.length(); i++) {
            try {
                JSONObject objKomentar = jsonArrayTanggapan.getJSONObject(i);
                KomentarModel komentar = new KomentarModel(objKomentar);
                if (komentar.getParent().equals("0")){
                    komentarList.add(komentar);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapterKomentar.notifyDataSetChanged();

    }

}
