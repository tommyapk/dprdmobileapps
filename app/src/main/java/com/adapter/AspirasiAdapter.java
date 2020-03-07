package com.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.AspirasiFragment;
import com.DetailAspirasi;
import com.MainActivity;
import com.Model.Aspirasi;
import com.Model.CircleTransform;
import com.Model.URLs;
import com.Model.VolleySingleton;
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

import static com.android.volley.VolleyLog.TAG;

public class AspirasiAdapter extends RecyclerView.Adapter<AspirasiAdapter.ViewHolder> {

    private Context context;
    private List<Aspirasi> data;



    //    dialog hapus
    TextView dialogMessage;
    Button dialogConfirmDeleted;
    ProgressBar dialogProggress;
    private AdapterView.OnItemClickListener mItemClickListener;


    public AspirasiAdapter(Context context, List<Aspirasi> data) {
        this.context = context;
        this.data = data;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama,komisinama,isi,dateCreated,tvPenanggung;
        Button hapus;
        ImageView imgProfile;
        CardView cardAspirasi ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            hapus = itemView.findViewById(R.id.btn_hapus_aspirasi);
            imgProfile = itemView.findViewById(R.id.imgUser);
            komisinama = itemView.findViewById(R.id.komisiname);
            dateCreated = itemView.findViewById(R.id.tv_dateCreated);
            isi = itemView.findViewById(R.id.isi_aspirasi);
            cardAspirasi = itemView.findViewById(R.id.cardviewaspirasi);
            tvPenanggung = itemView.findViewById(R.id.tv_penanggung);

            cardAspirasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i =  new Intent(context, DetailAspirasi.class);
                    i.putExtra("id",data.get(getAdapterPosition()));
                    context.startActivity(i);
                }
            });

        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_contact_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, final int i) {
        final Aspirasi pu = data.get(i);

        viewholder.nama.setText(pu.getUserName());
        viewholder.komisinama.setText(pu.getKomisi());

        viewholder.dateCreated.setText(pu.getDateCreated());
        Picasso.with(context).load(pu.getImage()).centerCrop()
                .transform(new CircleTransform(50,0))
                .fit()
                .into(viewholder.imgProfile);
        viewholder.isi.setText(pu.getMessage());
        viewholder.tvPenanggung.setText(pu.getNmPenanggun());
        if(pu.getStatus()=="1"){
            viewholder.hapus.setEnabled(false);
        } else {
            viewholder.hapus.setEnabled(true);
            viewholder.hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initHapus(pu.getIdAspirasi(),i);
                }

            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    Button btnCancelDelete;
    public  void initHapus(String idAsppirasi, final int i){
        final  String id = idAsppirasi;
        final Dialog dialog_aspirasi = new Dialog(context);
        dialog_aspirasi.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_aspirasi.setContentView(R.layout.dialog_hapus_aspirasi);
        dialog_aspirasi.show();

        btnCancelDelete = dialog_aspirasi.findViewById(R.id.btn_cancelaspirasi);
        btnCancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_aspirasi.dismiss();
            }
        });
        dialogConfirmDeleted = dialog_aspirasi.findViewById(R.id.btn_confirmDeleted);

        dialogProggress = dialog_aspirasi.findViewById(R.id.progressbar);

        dialogConfirmDeleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogProggress.setVisibility(View.VISIBLE);
                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URLs.URL_ASPIRASI+"/"+id,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //converting response to json object
                                    JSONObject obj = new JSONObject(response);
                                    Boolean status = obj.getBoolean("status");
                                    //if no error in response
                                    if (status) {
//                                        dialog_aspirasi.setContentView(R.layout.my_dialog_succes);
//                                        dialogMessage = dialog_aspirasi.findViewById(R.id.tv_dialogMessage);
//                                        dialogMessage.setText(obj.getString("message"));
                                        new CountDownTimer(1000,500){
                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                            }
                                            @Override
                                            public void onFinish() {
                                                // TODO Auto-generated method stub
                                                dialog_aspirasi.dismiss();
                                            }
                                        }.start();
                                        removeAt(i);
                                    } else {

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
        });


    }



    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }
    public void add(int position, Aspirasi aspirasi) {
        Log.d(TAG, "Aspirasi Di Tambahkan : " + position);
        data.add(position, aspirasi);
        notifyItemInserted(position);
    }

}