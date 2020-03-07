package com.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DetailAspirasi;
import com.DetailKomentar;
import com.Model.Aspirasi;
import com.Model.CircleTransform;
import com.Model.KomentarModel;
import com.Model.URLs;
import com.Model.VolleySingleton;
import com.adapter.dprdbottom.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.android.volley.VolleyLog.TAG;

public class KomentarAdapter extends RecyclerView.Adapter<KomentarAdapter.ViewHolder> {

    private Context context;
    private List<KomentarModel> data;


    public KomentarAdapter(Context context, List<KomentarModel> data) {
        this.context = context;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,komentar,balasTanggapan;
        ImageView imgProfile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.tv_namaUser);
            imgProfile = itemView.findViewById(R.id.civ_poto_profile);
            komentar = itemView.findViewById(R.id.tv_tanggapan);
            balasTanggapan = itemView.findViewById(R.id.tv_balasTanggapan);
            balasTanggapan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getKomentar(data.get(getAdapterPosition()));
                }
            });

        }
    }

    private void getKomentar(KomentarModel komentar) {
        List<KomentarModel> listKomentar = new ArrayList<>();
        for (int i=0;i<data.size();i++){
            KomentarModel kmt = data.get(i);
            if(komentar.getIdKomentar().equals(kmt.getParent())){
                listKomentar.add(kmt);
            }
        }
        Intent i =  new Intent(context, DetailKomentar.class);
        i.addFlags(FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("id",komentar);
//        i.putExtra("data", (Serializable) listKomentar);

        context.startActivity(i);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_komentar, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewholder, int i) {
        final KomentarModel pu = data.get(i);
        viewholder.nama.setText(pu.getUserName());
        viewholder.komentar.setText(pu.getKomentar());
        Picasso.with(context)
                .load(pu.getImage())
                    .centerCrop()
                    .transform(new CircleTransform(50,0))
                    .fit()
                .into(viewholder.imgProfile);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public  void menanggapiBalik(String parent){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        Dialog dialogMenanggapi = new Dialog(context);
        dialogMenanggapi = new Dialog(context);
        dialogMenanggapi.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMenanggapi.setCancelable(true);
        dialogMenanggapi.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogMenanggapi.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        dialogMenanggapi.getWindow().setAttributes(lp);
        dialogMenanggapi.setContentView(R.layout.dialog_kirimkomentar);
        dialogMenanggapi.show();


//        btnSend = dialogMenanggapi.findViewById(R.id.btn_sendKomentar);
//        btnSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendTanggapan();
//            }
//        });
    }

    private void sendTanggapan() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("status");
                            //if no error in response
                            if (status==true) {

                                //getting the user from the response

                            } else {
                                Toast.makeText(context, "Error :" +obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error :" +e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("komentar", komentar.getText().toString());
//                params.put("user_id", user.getIdUser());
//                params.put("aspirasi_id",aspirasi.getIdAspirasi());
//                params.put("parent",)
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }
}