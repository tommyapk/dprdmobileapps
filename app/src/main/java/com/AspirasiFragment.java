package com;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Model.Aspirasi;
import com.Model.Kecamatan_model;
import com.Model.SharedPrefManager;
import com.Model.URLs;
import com.Model.User;
import com.Model.VolleySingleton;
import com.adapter.AspirasiAdapter;
import com.adapter.dprdbottom.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static android.R.layout.simple_spinner_item;


/**
 * A simple {@link Fragment} subclass.
 */
public  class AspirasiFragment  extends Fragment {

    public static Object Datalist;
    private ProgressDialog pDialog ;// membuat loading
    private User user;

    public   List<Aspirasi> listAspirasiJson = new ArrayList<>();


    private  Dialog dialog_aspirasi;
    private RecyclerView aList;
    private LinearLayoutManager linearLayoutManager;

    //    Array LIst
    private ArrayList<Kecamatan_model> kecamatanList;
    private  ArrayList<String> arrayNameKec;

    //Spinner
    Spinner spinner,spinnerStatusAspirasi;


    private List<Aspirasi> AspirasiList;
    private RecyclerView.Adapter adapterAspirasi;

    FloatingActionButton fbaspirasi;
    private  Button hapusasp;
    private  EditText inputaspirasi;
    private Button kirimaspirasi, cancelaspirasi;
    RelativeLayout textKosong;

    public AspirasiFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_aspirasi, container, false);
        user =  SharedPrefManager.getInstance(getActivity()).getUser();


        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        if (listAspirasiJson.size()<1){
            getJsonArray();
            getKecamatan();
        }

        /**
         * inisialisasi Recyclecardview
         */
        AspirasiList = new ArrayList<>();
        //untuk Aspirasi Kosong
        textKosong =root.findViewById(R.id.textViewKosong);
        //---
        aList =(RecyclerView) root.findViewById(R.id.aspirasi_recycler_view);
        aList.setHasFixedSize(true);
         //Kita menggunakan LinearLayoutManager untuk list standar
         //yang hanya berisi daftar item
         //disusun dari atas ke bawah
        linearLayoutManager = new LinearLayoutManager(getActivity());
        aList.setLayoutManager(linearLayoutManager);
        adapterAspirasi = new AspirasiAdapter(getContext(),AspirasiList);
        aList.setAdapter(adapterAspirasi);
        //---==========================================================



        /**
         * setting Spinnner Status Aspirasi
         */
        spinnerStatusAspirasi = root.findViewById(R.id.spOpt);
        spinnerStatusAspirasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             getData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        fbaspirasi = root.findViewById(R.id.fab1);
        fbaspirasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCustomDialog();
            }
        });
        return  root;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    /**
     * inisialisasi json array aspirasi
     */
    public  void  getJsonArray(){
        pDialog.show();
        String url = URLs.URL_ASPIRASI +"/"+user.getIdUser();
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
                                JSONArray json = obj.getJSONArray("data");
                                for (int i=0;i<json.length();i++){
                                    Aspirasi asp = new Aspirasi(json.getJSONObject(i));
                                    listAspirasiJson.add(asp);
                                }
                                getData();
                            } else {
                                Toast.makeText(getActivity(), "Status Error :" +obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Json Error :" +e.toString(), Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Volley Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void getData() {
        AspirasiList.clear();
        textKosong.setVisibility(View.GONE);
        aList.setVisibility(View.VISIBLE);


        for (int i = 0; i < listAspirasiJson.size(); i++) {
            String status = spinnerStatusAspirasi.getSelectedItemPosition()==0 ? "" : spinnerStatusAspirasi.getSelectedItemPosition()==2 ? "1" : "4";
            Aspirasi aspirasi = listAspirasiJson.get(i);
            if (status==""){
                AspirasiList.add(aspirasi);
            } else if(status.matches(aspirasi.getStatus())){
                AspirasiList.add(aspirasi);
            }
        }
        if (AspirasiList.size()<1){
            textKosong.setVisibility(View.VISIBLE);
            return;
        }
        adapterAspirasi.notifyDataSetChanged();

    }


    private String kecId;
    private void initCustomDialog(){
        dialog_aspirasi = new Dialog(getActivity());
        dialog_aspirasi.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_aspirasi.setContentView(R.layout.inputaspirasi_dialog);
        dialog_aspirasi.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog_aspirasi.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog_aspirasi.getWindow().setAttributes(lp);
        dialog_aspirasi.show();
        spinner = dialog_aspirasi.findViewById(R.id.spinnerKecamatan);
        hapusasp = dialog_aspirasi.findViewById(R.id.btn_hapus_aspirasi);
        inputaspirasi = dialog_aspirasi.findViewById(R.id.txt_inputaspirasi);
        kirimaspirasi = dialog_aspirasi.findViewById(R.id.btn_kirimaspirasi);
        cancelaspirasi = dialog_aspirasi.findViewById(R.id.btn_cancelaspirasi);

        List<String> listSpinner = new ArrayList<String>();
        for (int i = 0; i < kecamatanList.size(); i++){
            listSpinner.add(kecamatanList.get(i).getKecamatan().toString());
        }
        ArrayAdapter<String> adapters = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, listSpinner);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapters);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Setting the values to textviews for a selected item
                Kecamatan_model kecModel = (Kecamatan_model) kecamatanList.get(position);
                kecId = kecModel.getId_kecamatan();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        kirimaspirasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAsp();
            }
        });
        cancelaspirasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_aspirasi.dismiss();
            }
        });
    }
    private  void sendAsp(){
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ASPIRASI,
                new Response.Listener<String>() {
                    @Override
                    public  void  onResponse(String response){
                        try {
                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("status");
                            //if no error in response
                            if (status) {
                                JSONArray jsonArray = obj.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        Log.e("Aspirasi : ","Ditambah"+i);
                                        JSONObject objAspirasi = jsonArray.getJSONObject(i);
                                        Aspirasi aspirasi = new Aspirasi(objAspirasi);
                                        listAspirasiJson.add(0,aspirasi);
                                    }catch (JSONException e) {
                                        pDialog.dismiss();
                                        e.printStackTrace();
                                    }
                                }

                                getData();

                                dialog_aspirasi.dismiss();
                                pDialog.dismiss();
                            } else {
                                Toast.makeText(getActivity(),"Error : "+ obj.getString( "message"), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        }catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            Toast.makeText(getActivity(),"Error : "+ e.toString( ), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getActivity(),"Error : " + error.toString() , Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user.getIdUser());
                params.put("message", inputaspirasi.getText().toString());
                params.put("kec_id", kecId);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
    private  void getKecamatan(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_ALLKEC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("status");
                            //if no error in response
                            if (status) {
                                kecamatanList = new ArrayList<Kecamatan_model>();
                                JSONArray dataArray  = obj.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    Kecamatan_model kecamatanModel = new Kecamatan_model(dataobj);
                                    kecamatanList.add(kecamatanModel);
                                }


                            } else
                                Toast.makeText(getActivity(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}

