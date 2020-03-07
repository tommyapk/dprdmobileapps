package com.adapter;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DetailKomentar;
import com.Model.Aspirasi;
import com.Model.CircleTransform;
import com.Model.KomentarModel;
import com.Model.SharedPrefManager;
import com.Model.URLs;
import com.Model.User;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class KomentarDetailAdapter extends RecyclerView.Adapter<KomentarDetailAdapter.ViewHolder> {

    private Context context;
    private User user;
    private List<KomentarModel> data;

    public KomentarDetailAdapter(Context context, List<KomentarModel> data) {
        this.context = context;
        this.data = data;
        user = SharedPrefManager.getInstance(context).getUser();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama,komentar,tvOption;
        ImageView imgProfile;
        Button btnDelete;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.tv_namaUser);
            imgProfile = itemView.findViewById(R.id.civ_poto_profile);
            komentar = itemView.findViewById(R.id.tv_tanggapan);
            btnDelete = itemView.findViewById(R.id.btn_hapus);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int Position = getAdapterPosition();
                    initHapus(data.get(Position).getIdKomentar(),Position);
                }
            });

            tvOption= itemView.findViewById(R.id.tv_option);

        }
    }

    public  void initHapus(String idKomentar, final int i){
                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URLs.URL_KOMENTAR+"/"+idKomentar,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //converting response to json object
                                    JSONObject obj = new JSONObject(response);
                                    Boolean status = obj.getBoolean("status");
                                    //if no error in response
                                    if (status) {
                                        removeAt(i);
                                    } else {
                                        Toast.makeText(context, "Status False :" +obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Json Error :" +e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context,"Volley Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    public void add(int position, KomentarModel komentar) {
        Log.d(TAG, "Aspirasi Di Tambahkan : " + position);
        data.add(position, komentar);
        notifyItemInserted(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_detail_komentar, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewholder, final int i) {
        final KomentarModel pu = data.get(i);
        viewholder.nama.setText(pu.getUserName());
        viewholder.komentar.setText(pu.getKomentar());
        Picasso.with(context)
                .load(pu.getImage())
                    .centerCrop()
                    .transform(new CircleTransform(50,0))
                    .fit()
                .into(viewholder.imgProfile);
        if (user.getIdUser().equals(pu.getUserId()))
            viewholder.tvOption.setVisibility(View.VISIBLE);
        viewholder.tvOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                Menu delete;
                PopupMenu popup = new PopupMenu(context, viewholder.tvOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.option_detail_komentar);

                    popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleted:
                                initHapus(pu.getIdKomentar(),i);
                                break;
                            case R.id.menu2:
                                //handle menu2 click
                                break;
                            case R.id.menu3:
                                //handle menu3 click
                                break;
                        }
                        return false;
                    }
                });

            }
        });
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